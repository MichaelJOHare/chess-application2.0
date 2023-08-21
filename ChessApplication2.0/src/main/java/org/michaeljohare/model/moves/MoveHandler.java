package org.michaeljohare.model.moves;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.game.GameState;
import org.michaeljohare.model.game.GameStateMemento;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.PieceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class MoveHandler {
    private boolean isFirstClick;
    private List<Move> moves;
    private Stack<GameStateMemento> mementos;
    private ChessPiece selectedPiece;
    private ChessBoard board;
    private MoveHistory move;
    private GameState gs;
    private GUIController guiController;
    private PieceManager pm;

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

    public void handleSquareClick(int row, int col) {
        if (isFirstClick) {
            handleSelectPiece(row, col);
        } else {
            handleMovePiece(row, col);
        }
    }

    private void handleSelectPiece(int row, int col) {
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

    private void handleMovePiece(int row, int col) {
        Square targetSquare = new Square(row, col);
        Optional<Move> legalMove = moves.stream().filter(m -> m.getEndSquare().equals(targetSquare)).findFirst();

        guiController.clearHighlightedSquares();

        if (legalMove.isEmpty()) {
            tryAgainPrompt(guiController::moveIsNotLegalLogText);
            isFirstClick = true;
            return;
        }

        finalizeMove(legalMove.get());
        handleCheckAndCheckmate();
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
        setFirstClick(true);
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
        List<ChessPiece> playerPieces = pm.getPlayerPieces(gs.getCurrentPlayer());
        List<List<Move>> currentPlayerLegalMoves = new ArrayList<>();

        for (ChessPiece piece : playerPieces) {
            if (piece.isAlive()) {
                currentPlayerLegalMoves.add(piece.calculateLegalMoves(board, move));
            }
        }

        if (currentPlayerLegalMoves.stream()
                .allMatch(List::isEmpty) && board.isKingInCheck(gs.getCurrentPlayer(), move, board)) {

            gs.setGameOver(true);
            guiController.checkmateLogText();
        } else if (currentPlayerLegalMoves.stream()
                .allMatch(List::isEmpty)) {

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

    public void setFirstClick(boolean firstClick) {
        isFirstClick = firstClick;
    }
}
