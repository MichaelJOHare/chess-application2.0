package org.michaeljohare.controller;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.game.GameManager;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.view.ChessGUI;

import java.util.List;

public class ChessController {
    ChessBoard board;
    ChessGUI gui;
    GameManager gm;

    public ChessController(ChessBoard board, GameManager gm) {
        this.board = board;
        this.gui = new ChessGUI(board);
        this.gm = gm;
        gui.getChessBoardPanel().setController(this);
        gui.getGameLogPanel().setController(this);
    }

    public void updateGUI() {
        gui.updateGUI();
    }

    public void onSquareClick(int row, int col) {
        gm.handleSquareClick(row, col);
    }

    public void handleUndoButtonClick() {
        gm.handleUndoButtonClick();
    }

    public void handlePlayAgainButtonClick() {
        gm.handlePlayAgainButtonClick();
    }

    public void setHighlightedSquares(List<Move> moves) {
        gui.getChessBoardPanel().setHighlightedSquares(moves);
    }

    public void clearHighlightedSquares() {
        gui.getChessBoardPanel().clearHighlightedSquares();
    }

    public void updateLogTextArea(String message) {
        gui.getGameLogPanel().updateLogTextArea(message);
    }

    public void updatePlayAgainButton() {
    }

    public void noLegalMoveLogText() {
        gui.getGameLogPanel().noLegalMoveLogText();
    }

    public void moveIsNotLegalLogText() {
        gui.getGameLogPanel().moveIsNotLegalLogText();
    }

    public void nothingLeftToUndoLogText() {
        gui.getGameLogPanel().nothingLeftToUndoLogText();
    }
}
