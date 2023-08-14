package org.michaeljohare.controller;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.game.GameManager;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;
import org.michaeljohare.view.ChessGUI;

import java.awt.*;
import java.util.List;

public class ChessController {
    private final ChessGUI gui;
    private final GameManager gm;

    public ChessController(ChessBoard board, GameManager gm) {
        this.gui = new ChessGUI(board);
        this.gm = gm;
        gui.getChessBoardPanel().setController(this);
        gui.getGameLogPanel().setController(this);
        gui.setController(this);
    }

    public void showGUI() {
        gui.showGUI();
    }

    public void onUserRequestFlipBoard() {
        gui.getChessBoardPanel().flipBoard();
    }

    public void updateGUI() {
        gui.updateGUI();
    }

    public void onSquareClick(int row, int col) {
        gm.handleSquareClick(row, col);
    }

    public void onWindowClosing() {
        gm.cleanup();
    }

    public void handleUndoButtonClick() {
        gm.handleUndoButtonClick();
    }

    public void handleAskStockfishButtonClick() {
        gm.handleAskStockfishButtonClick();
    }

    public void handlePlayAgainButtonClick() {
        gm.handlePlayAgainButtonClick();
    }

    public int handlePawnPromotion(ChessPiece pawn) {
        return gui.createPromotionPane(pawn, gui.getChessBoardPanel().getChessButtons());
    }

    public void setHighlightedSquares(List<Move> moves) {
        gui.getChessBoardPanel().setHighlightedSquares(moves);
    }

    public void setHighlightedSquaresPreviousMove(Move move) {
        gui.getChessBoardPanel().setHighlightedSquaresPreviousMove(move);
    }

    public void setHighlightedSquaresStockfish(Move move) {
        gui.getChessBoardPanel().setHighlightedSquaresStockfish(move);
    }

    public void clearHighlightedSquares() {
        gui.getChessBoardPanel().clearHighlightedSquares();
    }

    public void clearPreviousMoveHighlightedSquares() {
        gui.getChessBoardPanel().clearPreviousMoveHighlightedSquares();
    }

    public void updatePlayAgainButton() {
        gui.getGameLogPanel().updatePlayAgainButton(Color.GREEN, Color.BLACK);
    }

    public void updateCapturedPieceDisplay(List<ChessPiece> player1CapturedPieces, List<ChessPiece> player2CapturedPieces) {
        gui.getGameLogPanel().updateCapturedPiecesDisplay(player1CapturedPieces, player2CapturedPieces);
    }

    public void currentPlayerLogText(Player currentPlayer) {
        gui.getGameLogPanel().currentPlayersTurnLogText(currentPlayer);
    }

    public void noLegalMoveLogText() {
        gui.getGameLogPanel().noLegalMoveLogText();
    }

    public void moveIsNotLegalLogText() {
        gui.getGameLogPanel().moveIsNotLegalLogText();
    }

    public void invalidPieceSelectionLogText() {
        gui.getGameLogPanel().invalidPieceSelectionLogText();
    }

    public void nothingLeftToUndoLogText() {
        gui.getGameLogPanel().nothingLeftToUndoLogText();
    }

    public void checkLogText() {
        gui.getGameLogPanel().checkLogText();
    }

    public void checkmateLogText() {
        gui.getGameLogPanel().checkmateLogText();
    }

    public void stalemateLogText() {
        gui.getGameLogPanel().stalemateLogText();
    }

    public void stockfishWaitingButtonText() {
        gui.getGameLogPanel().stockfishWaitingButtonText();
    }

    public void stockfishThinkingButtonText() {
        gui.getGameLogPanel().stockfishThinkingButtonText();
    }

    public void resetStockfishButtonText() {
        gui.getGameLogPanel().resetStockfishButtonText();
    }
}
