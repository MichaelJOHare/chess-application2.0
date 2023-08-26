package com.michaeljohare.model.moves;

import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.Pawn;

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

}
