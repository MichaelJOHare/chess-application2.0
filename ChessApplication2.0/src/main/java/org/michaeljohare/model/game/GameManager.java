package org.michaeljohare.model.game;

import org.michaeljohare.controller.ChessController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceWithMoveStatus;
import org.michaeljohare.model.player.PieceManager;

import java.util.List;

public class GameManager {

    ChessController controller;
    ChessBoard board;
    GameState gs;
    PieceManager pm;
    ChessPiece selectedPiece;
    Square targetSquare;
    boolean isFirstClick;
    List<Move> moves;
    MoveHistory move;

    public GameManager() {
        this.board = new ChessBoard();
        this.gs = new GameState(board);
        this.pm = board.getPieceManager();
        this.controller = new ChessController(board, this);
        isFirstClick = true;
        move = new MoveHistory();
    }

    public void handleSquareClick(int row, int col) {
        if (isFirstClick) {
            selectedPiece = board.getPieceAt(row, col);
            if (selectedPiece.getPlayer().equals(gs.getCurrentPlayer())) {
                moves = selectedPiece.calculateLegalMoves(board, move);
                if (moves.size() > 0) {
                    controller.setHighlightedSquares(moves);
                }
            }
            isFirstClick = false;
        } else {
            targetSquare = new Square(row, col);
            for (Move m : moves) {
                if (m.getEndSquare().equals(targetSquare)) {
                    if (selectedPiece instanceof PieceWithMoveStatus) {
                        ((PieceWithMoveStatus) selectedPiece).setHasMoved(true);
                    }
                    if (board.isOccupiedByOpponent(row, col, gs.getOpposingPlayer())) {
                        ChessPiece capturedPiece = board.getPieceAt(row, col);
                        Move legalCapture = new Move(selectedPiece, selectedPiece.getCurrentSquare(), targetSquare, capturedPiece, board);
                        move.makeMove(legalCapture);
                    } else {
                        Move legalNonCapture = new Move(selectedPiece, selectedPiece.getCurrentSquare(), targetSquare, null, board);
                        move.makeMove(legalNonCapture);
                    }
                    updateGUI();
                } else {
                    //update log text
                }
            }
            isFirstClick = true;
            gs.swapPlayers();
        }
    }

    private void updateGUI() {
        controller.updateGUI();
    }
}
