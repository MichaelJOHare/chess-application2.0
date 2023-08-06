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
import java.util.Stack;

public class GameManager {

    private ChessController controller;
    private ChessBoard board;
    private GameState gs;
    private ChessPiece selectedPiece;
    private boolean isFirstClick;
    private List<Move> moves;
    private Stack<GameStateMemento> mementos = new Stack<>();
    private MoveHistory move;
    PieceManager pm;

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
                } else {
                    controller.noLegalMoveLogText();
                    isFirstClick = true;
                    return;
                }
            }
            isFirstClick = false;
        } else {
            Square targetSquare = new Square(row, col);
            Move legalMove = null;
            for (Move m : moves) {
                if (m.getEndSquare().equals(targetSquare)) {
                    legalMove = m;
                    break;
                }
            }
            if (legalMove != null) {
                mementos.push(gs.createMemento());
                if (selectedPiece instanceof PieceWithMoveStatus) {
                    ((PieceWithMoveStatus) selectedPiece).setHasMoved(true);
                }
                move.makeMove(legalMove);
                updateGUI();
                isFirstClick = true;
                gs.swapPlayers();
            } else {
                controller.moveIsNotLegalLogText();
                isFirstClick = true;
            }
            controller.clearHighlightedSquares();
        }
    }

    public void handleUndoButtonClick() {
        if (!mementos.isEmpty()) {
            move.undoMove();
            GameStateMemento memento = mementos.pop();
            gs.restoreFromMemento(memento);
            updateGUI();
        } else {
            controller.nothingLeftToUndoLogText();
        }
    }

    public void handlePlayAgainButtonClick() {
        this.board = new ChessBoard();
        this.gs = new GameState(board);
        this.pm = board.getPieceManager();
        this.controller = new ChessController(board, this);
        isFirstClick = true;
        move = new MoveHistory();
        mementos = new Stack<>();
    }

    private void updateGUI() {
        controller.updateGUI();
    }
}
