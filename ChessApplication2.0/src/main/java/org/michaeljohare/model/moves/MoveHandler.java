package org.michaeljohare.model.moves;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.game.GameState;
import org.michaeljohare.model.game.GameStateMemento;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.PieceManager;

import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

public class MoveHandler {
    private final Stack<GameStateMemento> mementos;
    private final ChessBoard board;
    private final MoveHistory move;
    private final GameState gs;
    private final GUIController guiController;
    private final PieceManager pm;
    private boolean isFirstClick;
    private ChessPiece selectedPiece;
    private List<Move> moves;

    public MoveHandler(ChessBoard board, MoveHistory move, GameState gs, GUIController guiController,
                       Stack<GameStateMemento> mementos, PieceManager pm) {
        isFirstClick = true;
        this.board = board;
        this.move = move;
        this.gs = gs;
        this.guiController = guiController;
        this.mementos = mementos;
        this.pm = pm;
    }

    public void handleSelectPieceClick(int row, int col) {
        selectedPiece = board.getPieceAt(row, col);

        if (selectedPiece == null || selectedPiece.getPlayer() != gs.getCurrentPlayer()) {
            tryAgainPrompt(guiController::invalidPieceSelectionLogText);
            return;
        }

        if (selectedPiece.getPlayer().equals(gs.getCurrentPlayer())) {
            moves = selectedPiece.calculateLegalMoves(board, move);
            if (!moves.isEmpty()) {
                guiController.setHighlightedSquares(moves);
            } else {
                tryAgainPrompt(guiController::noLegalMoveLogText);
                return;
            }
            isFirstClick = false;
        }
    }

    public void handleMovePieceClick(int row, int col) {
        guiController.clearHighlightedSquares();
        Square targetSquare = new Square(row, col);

        // Check if the target square contains a piece owned by the current player to allow selecting a different piece
        // without getting a tryAgainPrompt
        ChessPiece pieceAtTargetSquare = board.getPieceAt(row, col);
        if (pieceAtTargetSquare != null && pieceAtTargetSquare.getPlayer().equals(gs.getCurrentPlayer())) {
            handleSelectPieceClick(row, col);
            return;
        }

        Optional<Move> legalMove = moves.stream().filter(m -> m.getEndSquare().equals(targetSquare)).findFirst();

        if (legalMove.isEmpty()) {
            tryAgainPrompt(guiController::moveIsNotLegalLogText);
            return;
        }

        finalizeMove(legalMove.get());
        handleCheckAndCheckmate();
    }

    public boolean handleDragStart(int row, int col){
        selectedPiece = board.getPieceAt(row, col);

        if(selectedPiece == null || selectedPiece.getPlayer() != gs.getCurrentPlayer()){
            tryAgainPrompt(guiController::invalidPieceSelectionLogText);
            return false;
        }

        if (selectedPiece.getPlayer().equals(gs.getCurrentPlayer())) {
            moves = selectedPiece.calculateLegalMoves(board, move);
            if (!moves.isEmpty()) {
                guiController.setHighlightedSquares(moves);
                return true;
            } else {
                tryAgainPrompt(guiController::noLegalMoveLogText);
                return false;
            }
        }
        return false;
    }

    public MoveResult handleDragDrop(int endRow, int endCol) {
        guiController.clearHighlightedSquares();

        Square endSquare = new Square(endRow, endCol);
        Optional<Move> legalMove = moves.stream().filter(m -> m.getEndSquare().equals(endSquare)).findFirst();

        if (legalMove.isPresent()) {
            Move confirmedMove = legalMove.get();
            finalizeMove(confirmedMove);
            handleCheckAndCheckmate();

            if (confirmedMove.isPromotion) {
                PromotionMove promotionMove = (PromotionMove) confirmedMove;
                ChessPiece promotedPiece = promotionMove.getPromotedPiece();
                return new MoveResult(promotedPiece);
            }
            return new MoveResult(MoveResult.MoveType.NORMAL);
        } else {
            tryAgainPrompt(guiController::moveIsNotLegalLogText);
            return new MoveResult(MoveResult.MoveType.INVALID);
        }
    }

    public void finalizeMove(Move legalMove) {
        mementos.push(gs.createMemento());

        if (legalMove.isPromotion() && !gs.getCurrentPlayer().isStockfish()) {
            ((PromotionMove) legalMove).setPromotionType(guiController.handlePawnPromotion(selectedPiece));
        }

        move.makeMove(legalMove);
        pm.handlePromotion(move.getLastMove());
        handleCapturedPieces(legalMove, false);
        guiController.updateGUI();
        isFirstClick = true;

        gs.swapPlayers();
        guiController.currentPlayerLogText(gs.getCurrentPlayer());
        guiController.setHighlightedSquaresPreviousMove(legalMove);
    }

    public void handleUndoMove() {
        int undoCount = (gs.getCurrentPlayer().equals(gs.getPlayer1()) && gs.getPlayer2().isStockfish()) ? 2 : 1;

        for (int i = 0; i < undoCount; i++) {
            if (mementos.isEmpty()) {
                guiController.nothingLeftToUndoLogText();
                break;
            }
            handleSingleUndo();
        }
        guiController.clearHighlightedSquares();
    }

    private void handleSingleUndo() {
        if (gs.isGameOver()) {
            gs.setGameOver(false);
        }

        handleCapturedPieces(move.getLastMove(), true);
        pm.handleUndoPromotion(move.getLastMove());

        move.undoMove();
        GameStateMemento memento = mementos.pop();
        gs.restoreFromMemento(memento);

        guiController.setHighlightedSquaresPreviousMove(move.getLastMove());
        guiController.currentPlayerLogText(gs.getCurrentPlayer());
        isFirstClick = true;
        guiController.updateGUI();
    }

    public void handleCapturedPieces(Move legalMove, boolean isUndo) {
        ChessPiece capturedPiece = legalMove.getCapturedPiece();

        if (capturedPiece == null) {
            return;
        }

        if (isUndo) {
            gs.removeCapturedPiece(capturedPiece);
        } else {
            gs.addCapturedPiece(capturedPiece);
        }

        guiController.updateCapturedPieceDisplay(gs.getPlayer1CapturedPieces(), gs.getPlayer2CapturedPieces());
    }

    public void handleCheckAndCheckmate() {
        // Add only non-captured pieces to a list
        List<ChessPiece> playerPieces = pm.getPlayerPieces(gs.getCurrentPlayer()).stream()
                .filter(ChessPiece::isAlive)
                .collect(Collectors.toList());

        boolean hasLegalMoves = false;

        // No need to continue searching after one legal move is found
        for (ChessPiece piece : playerPieces) {
            if (!piece.calculateLegalMoves(board, move).isEmpty()) {
                hasLegalMoves = true;
                break;
            }
        }

        if (!hasLegalMoves && board.isKingInCheck(gs.getCurrentPlayer(), move, board)) {
            gs.setGameOver(true);
            guiController.checkmateLogText();
        } else if (!hasLegalMoves) {
            gs.setGameOver(true);
            guiController.stalemateLogText();
        } else if (board.isKingInCheck(gs.getCurrentPlayer(), move, board)) {
            guiController.checkLogText();
        }
    }
    private void tryAgainPrompt(Runnable logTextMethod) {
        logTextMethod.run();
        isFirstClick = true;
    }

    public boolean isFirstClick() {
        return isFirstClick;
    }

    public void setFirstClick(boolean firstClick) {
        isFirstClick = firstClick;
    }
}
