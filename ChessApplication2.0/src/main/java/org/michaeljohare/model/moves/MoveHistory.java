package org.michaeljohare.model.moves;

import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceWithMoveStatus;

import java.util.Stack;

public class MoveHistory {
    private final Stack<Move> history = new Stack<>();
    private final Stack<Move> undone = new Stack<>();

    public void makeMove(Move move) {
        move.execute();
        history.push(move);
        undone.clear();
    }

    public void undoMove() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            resetHasMovedFlagForUndo(lastMove);
            lastMove.undo();
            undone.push(lastMove);
        }
    }

    public void redoMove() {
        if (!undone.isEmpty()) {
            Move redoMove = undone.pop();
            resetHasMovedFlagForRedo(redoMove);
            redoMove.execute();
            history.push(redoMove);
        }
    }

    public Move getLastMove() {
        return history.size() > 0 ? history.peek() : null;
    }

    private void resetHasMovedFlagForUndo(Move move) {
        ChessPiece piece = move.getPiece();
        if (piece instanceof PieceWithMoveStatus) {
            ((PieceWithMoveStatus) piece).setHasMoved(false);
        }
    }

    private void resetHasMovedFlagForRedo(Move move) {
        ChessPiece piece = move.getPiece();
        if (piece instanceof PieceWithMoveStatus) {
            ((PieceWithMoveStatus) piece).setHasMoved(true);
        }
    }
}
