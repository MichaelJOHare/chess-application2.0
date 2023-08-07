package org.michaeljohare.model.moves;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.Pawn;

public class EnPassantMove extends Move {
    private final Square originalSquareBeforeCapture;

    public EnPassantMove(ChessPiece piece, Square from, Square to, Square originalSquareBeforeCapture, ChessPiece capturedPiece, ChessBoard board) {
        super(piece, from, to, capturedPiece, board);
        this.originalSquareBeforeCapture = originalSquareBeforeCapture;
    }

    @Override
    public void undo() {
        if (capturedPiece != null && capturedPiece instanceof Pawn) {
            capturedPiece.revive();
            board.removePiece(capturedPiece);
            capturedPiece.setCurrentSquare(originalSquareBeforeCapture);
            board.addPiece(capturedPiece);
        }
        board.removePiece(piece);
        piece.setCurrentSquare(startSquare);
        board.addPiece(piece);
    }

    @Override
    public void redo() {
        execute();
    }
}
