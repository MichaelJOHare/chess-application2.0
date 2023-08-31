package com.michaeljohare.controller;

import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.PieceType;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.moves.MoveResult;
import com.michaeljohare.view.ChessGUI;

import java.awt.*;
import java.util.List;

public class GUIController {
    private final ChessGUI gui;
    private final GameController gc;

    public GUIController(ChessBoard board, GameController gc) {
        this.gui = new ChessGUI(board);
        this.gc = gc;
        gui.getChessBoardPanel().init(this);
        gui.getGameLogPanel().setGuiController(this);
        gui.setGuiController(this);
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
        gc.handleClickToMove(row, col);
    }

    public boolean onDragStart(int row, int col) {
        return gc.handleDragStart(row, col);
    }

    public MoveResult onDragDrop(int endRow, int endCol){
        return gc.handleDragDrop(endRow, endCol);
    }

    public void onWindowClosing() {
        gc.cleanup();
    }

    public void handleUndoButtonClick() {
        gc.handleUndoButtonClick();
    }

    public void handleAskStockfishButtonClick() {
        gc.askStockFish();
    }

    public void handlePlayAgainButtonClick() {
        gc.handlePlayAgainButtonClick();
    }

    public PieceType handlePawnPromotion(ChessPiece pawn) {
        return gui.createPromotionPane(pawn, gui.getChessBoardPanel().getChessButtons());
    }

    public void setHighlightedSquares(List<Move> moves) {
        gui.getChessBoardPanel().setHighlightedSquares(moves);
    }

    public void setHighlightedSquaresPreviousMove(Move move) {
        clearPreviousMoveHighlightedSquares();
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
        updatePlayAgainButton();
    }

    public void stalemateLogText() {
        gui.getGameLogPanel().stalemateLogText();
        updatePlayAgainButton();
    }

    public void drawLogText() {
        gui.getGameLogPanel().drawLogText();
        updatePlayAgainButton();
    }

    public void stockfishWaitingButtonText() {
        gui.getGameLogPanel().stockfishWaitingButtonText();
    }

    public void stockfishThinkingButtonText() {
        gui.getGameLogPanel().stockfishThinkingButtonText();
    }

    public void stockfishGameOverButtonText(){gui.getGameLogPanel().stockfishGameOverButtonText();}

    public void resetStockfishButtonText() {
        gui.getGameLogPanel().resetStockfishButtonText();
    }

    public void imageAccessError() {gui.getGameLogPanel().imageAccessError();}
}
