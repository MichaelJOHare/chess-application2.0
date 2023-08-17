package org.michaeljohare.model.game;

import org.michaeljohare.controller.ChessController;
import org.michaeljohare.controller.StockfishController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.moves.PromotionMove;
import org.michaeljohare.model.pieces.*;
import org.michaeljohare.model.player.PieceManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;

import static org.michaeljohare.model.pieces.PieceType.convertIntToPieceType;

public class GameManager {

    private ChessController controller;
    private StockfishController sfController;
    private ChessBoard board;
    private GameState gs;
    private ChessPiece selectedPiece;
    private boolean isFirstClick;
    private boolean isGameOver;
    private List<Move> moves;
    private Stack<GameStateMemento> mementos = new Stack<>();
    private MoveHistory move;
    private PieceManager pm;

    public GameManager() {
        this.board = new ChessBoard();
        this.gs = new GameState(board);
        this.pm = board.getPieceManager();

        isFirstClick = true;
        isGameOver = false;
        move = new MoveHistory();
        sfController = new StockfishController(board, move, gs);

        this.controller = new ChessController(board, this);
        controller.showGUI();

        initiateGame();
    }

    private void initiateGame() {
        if (gs.getCurrentPlayer().getName().equals("Stockfish")) {
            makeStockfishMove();
        }
    }

    public void handleSquareClick(int row, int col) {
        if (isFirstClick) {
            handleFirstClick(row, col);
        } else {
            handleSecondClick(row, col);
        }
    }

    public void handleFirstClick(int row, int col) {
        selectedPiece = board.getPieceAt(row, col);

        if (selectedPiece == null || selectedPiece.getPlayer() != gs.getCurrentPlayer()) {
            tryAgainPrompt(controller::invalidPieceSelectionLogText);
            return;
        }

        if (selectedPiece.getPlayer().equals(gs.getCurrentPlayer())) {
            moves = selectedPiece.calculateLegalMoves(board, move);
            if (moves.size() > 0) {
                controller.setHighlightedSquares(moves);
            } else {
                tryAgainPrompt(controller::noLegalMoveLogText);
                return;
            }
            isFirstClick = false;
        }
    }

    public void handleSecondClick(int row, int col) {
        Square targetSquare = new Square(row, col);
        Move legalMove = null;
        Move legalPromotionMove = null;

        for (Move m : moves) {
            if (m.getEndSquare().equals(targetSquare)) {
                if(m.isPromotion()){
                    legalPromotionMove = m;
                } else {
                    legalMove = m;
                }
            } else {
                tryAgainPrompt(controller::moveIsNotLegalLogText);
            }
        }

        if(legalPromotionMove != null) {
            finalizeMove(legalPromotionMove);
        } else if (legalMove != null) {
            finalizeMove(legalMove);
        } else {
            tryAgainPrompt(controller::moveIsNotLegalLogText);
        }

        controller.clearHighlightedSquares();
        handleCheckAndCheckmate();

        if (gs.getCurrentPlayer().getName().equals("Stockfish")) {
            makeStockfishMove();
        }
    }

    public void handleUndoButtonClick() {
        if (gs.getCurrentPlayer().equals(gs.getPlayer1()) && gs.getPlayer2().getName().equals("Stockfish")) {

            if (mementos.size() < 2) {
                controller.nothingLeftToUndoLogText();
                return;
            }

            // Undo Stockfish's move and player's move
            handleSingleUndo();
            handleSingleUndo();
        } else {
            if (mementos.isEmpty()) {
                controller.nothingLeftToUndoLogText();
            } else {
                handleSingleUndo();
            }
        }
    }

    public void handleAskStockfishButtonClick() {
        askStockFish();
    }

    public void handlePlayAgainButtonClick() {
        controller.clearPreviousMoveHighlightedSquares();
        gs.init();
        board.init(gs.getPlayer1(), gs.getPlayer2());
        this.pm = board.getPieceManager();
        isFirstClick = true;
        move.resetMoveHistory();
        mementos.clear();
        updateGUI();
        initiateGame();
    }

    public void finalizeMove(Move legalMove) {
        mementos.push(gs.createMemento());

        if (legalMove.isPromotion()) {
            int promotionChoice = controller.handlePawnPromotion(selectedPiece);
            PieceType chosenPromotion = convertIntToPieceType(promotionChoice);
            ((PromotionMove) legalMove).setPromotionType(chosenPromotion);
        }

        move.makeMove(legalMove);
        pm.handlePromotion(move.getLastMove());
        handleCapturedPieces(legalMove, false);
        updateGUI();
        isFirstClick = true;

        gs.swapPlayers();
        controller.currentPlayerLogText(gs.getCurrentPlayer());

        controller.clearPreviousMoveHighlightedSquares();
        controller.setHighlightedSquaresPreviousMove(legalMove);
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

            isGameOver = true;
            controller.checkmateLogText();
            controller.updatePlayAgainButton();
        } else if (currentPlayerLegalMoves.stream()
                .allMatch(List::isEmpty)) {

            isGameOver = true;
            controller.stalemateLogText();
            controller.updatePlayAgainButton();
        } else if (board.isKingInCheck(gs.getCurrentPlayer(), move, board)) {

            controller.checkLogText();
        }
    }

    private void handleSingleUndo() {
        if (isGameOver) {
            isGameOver = false;
        }

        handleCapturedPieces(move.getLastMove(), true);
        pm.handleUndoPromotion(move.getLastMove());

        move.undoMove();
        GameStateMemento memento = mementos.pop();
        gs.restoreFromMemento(memento);

        controller.clearPreviousMoveHighlightedSquares();
        controller.setHighlightedSquaresPreviousMove(move.getLastMove());
        controller.currentPlayerLogText(gs.getCurrentPlayer());
        isFirstClick = true;
        updateGUI();
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

        controller.updateCapturedPieceDisplay(gs.getPlayer1CapturedPieces(), gs.getPlayer2CapturedPieces());
    }

    private void askStockFish() {
        if (isGameOver) {
            return;
        }

        controller.stockfishWaitingButtonText();
        CompletableFuture.supplyAsync(() -> sfController.getMove())
                .thenAccept(move -> {
                    if (move != null) {
                        SwingUtilities.invokeLater(() -> {
                            controller.resetStockfishButtonText();
                            controller.clearHighlightedSquares();
                            controller.setHighlightedSquaresStockfish(move);
                        });
                    } else {
                        // controller.stockfishGameOverButtonText();
                    }
                })
                .exceptionally(ex -> {
                    System.err.println("Error fetching move from Stockfish: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    private void makeStockfishMove() {
        if (isGameOver) {
            return;
        }

        controller.stockfishThinkingButtonText();
        CompletableFuture.supplyAsync(() -> sfController.getMove())
                .thenAccept(stockfishMove -> {
                    if (stockfishMove != null) {
                        SwingUtilities.invokeLater(() -> {
                            controller.resetStockfishButtonText();

                            finalizeMove(stockfishMove);

                            handleCheckAndCheckmate();
                        });
                    } else {
                        // controller.stockfishGameOverButtonText();
                    }
                })
                .exceptionally(ex -> {
                    System.err.println("Error executing move for Stockfish: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    public void tryAgainPrompt(Runnable logTextMethod) {
        logTextMethod.run();
        isFirstClick = true;
    }

    private void updateGUI() {
        controller.updateGUI();
    }

    public void cleanup() {
        sfController.cleanup();
    }
}
