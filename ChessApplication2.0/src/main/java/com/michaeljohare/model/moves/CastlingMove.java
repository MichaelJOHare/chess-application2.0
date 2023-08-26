package com.michaeljohare.model.moves;

import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.Rook;

public class CastlingMove extends Move {
    private final Rook rook;
    private final Square rookStartSquare;
    private final Square rookEndSquare;

    public CastlingMove(ChessPiece king, Rook rook, Square kingFrom, Square kingTo, Square rookFrom, Square rookTo, ChessBoard board) {
        super(king, kingFrom, kingTo, null, board);
        this.rook = rook;
        this.rookStartSquare = rookFrom;
        this.rookEndSquare = rookTo;
    }

    @Override
    public void execute() {
        super.execute();
        board.removePiece(rook);
        rook.setCurrentSquare(rookEndSquare);
        board.addPiece(rook);
        rook.setHasMoved(true);
    }

    @Override
    public void undo() {
        super.undo();
        board.removePiece(rook);
        rook.setCurrentSquare(rookStartSquare);
        board.addPiece(rook);
        rook.setHasMoved(false);
    }

    @Override
    public void redo() {
        super.redo();
        board.removePiece(rook);
        rook.setCurrentSquare(rookEndSquare);
        board.addPiece(rook);
    }
}
