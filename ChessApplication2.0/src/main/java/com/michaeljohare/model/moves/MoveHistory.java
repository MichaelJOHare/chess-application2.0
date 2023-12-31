package com.michaeljohare.model.moves;

import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.Pawn;
import com.michaeljohare.model.pieces.PieceType;
import com.michaeljohare.model.pieces.PieceWithMoveStatus;
import com.michaeljohare.model.player.PlayerColor;

import java.util.Stack;

public class MoveHistory {
    private final Stack<Move> history = new Stack<>();
    private final Stack<Move> undone = new Stack<>();
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    public void makeMove(Move move) {
        move.execute();
        if (move.getPiece().getType().equals(PieceType.PAWN) || move.isCapture()) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
        if (move.getPiece().getPlayer().getColor() == PlayerColor.BLACK) {
            fullMoveNumber++;
        }
        history.push(move);
        undone.clear();
    }

    public void undoMove() {
        if (!history.isEmpty()) {
            Move lastMove = history.pop();
            resetMoveClocksForUndo(lastMove);
            // ***REMOVED*** if (lastMove instanceof CastlingMove)
            //   unsure if was necessary and caused bug involving king move -> undo -> no longer able to castle
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
        return !history.isEmpty() ? history.peek() : null;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public int getFullMoveNumber() {
        return fullMoveNumber;
    }

    public Square getEnPassantTarget() {
        Move lastMove = getLastMove();
        if (lastMove != null && lastMove.getPiece() instanceof Pawn) {
            int difference = lastMove.getEndSquare().getRow() - lastMove.getStartSquare().getRow();
            if (Math.abs(difference) == 2) {
                return new Square((lastMove.getEndSquare().getRow() + lastMove.getStartSquare().getRow()) / 2, lastMove.getStartSquare().getCol());
            }
        }
        return null;
    }

    private void resetMoveClocksForUndo(Move lastMove) {
        if (lastMove.getPiece().getType().equals(PieceType.PAWN) || lastMove.isCapture()) {
            halfMoveClock = calculateHalfMoveClockFromHistory();
        } else {
            halfMoveClock--;
        }

        if (lastMove.getPiece().getPlayer().getColor() == PlayerColor.BLACK) {
            fullMoveNumber--;
        }
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

    private int calculateHalfMoveClockFromHistory() {
        int counter = 0;
        for (int i = history.size() - 1; i >= 0; i--) {
            Move move = history.get(i);
            if (move.getPiece().getType().equals(PieceType.PAWN) || move.isCapture()) {
                break;
            }
            counter++;
        }
        return counter;
    }

    public MoveHistory copy() {
        MoveHistory copiedHistory = new MoveHistory();

        for (Move move : this.history) {
            // Other memory hotspot, need different idea for fix here I think - may only need previous few moves?
            copiedHistory.history.push(move.copy());
        }

        for (Move move : this.undone) {
            copiedHistory.undone.push(move.copy());
        }

        copiedHistory.halfMoveClock = this.halfMoveClock;
        copiedHistory.fullMoveNumber = this.fullMoveNumber;

        return copiedHistory;
    }

    public void resetMoveHistory() {
        halfMoveClock = 0;
        fullMoveNumber = 1;
        history.clear();
        undone.clear();
    }
}
