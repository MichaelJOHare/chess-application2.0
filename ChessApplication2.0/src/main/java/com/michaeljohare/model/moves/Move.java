package com.michaeljohare.model.moves;

import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.PieceWithMoveStatus;

public class Move implements Movable{
    protected final Square startSquare;
    protected final Square endSquare;
    protected final ChessPiece capturedPiece;
    protected final ChessBoard board;
    protected ChessPiece piece;
    protected boolean isPromotion;
    protected boolean isCapture;

    public Move(ChessPiece piece, Square from, Square to, ChessPiece capturedPiece, ChessBoard board) {
        this.piece = piece;
        this.startSquare = from;
        this.endSquare = to;
        this.capturedPiece = capturedPiece;
        this.board = board;
        this.isPromotion = false;
        this.isCapture = false;
    }

    @Override
    public void execute() {
        if (capturedPiece != null) {
            capturedPiece.kill();
            board.removePiece(capturedPiece);
            isCapture = true;
        }
        board.removePiece(piece);
        piece.setCurrentSquare(endSquare);
        board.addPiece(piece);

        if (piece instanceof PieceWithMoveStatus) {
            ((PieceWithMoveStatus) piece).setHasMoved(true);
        }
    }

    @Override
    public void undo() {
        board.removePiece(piece);
        piece.setCurrentSquare(startSquare);
        board.addPiece(piece);
        if (capturedPiece != null) {
            capturedPiece.revive();
            capturedPiece.setCurrentSquare(endSquare);
            board.addPiece(capturedPiece);
        }
    }

    @Override
    public void redo() {
        execute();
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
    public boolean isPromotion() {
        return isPromotion;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public void setPromotion(boolean promotion) {
        this.isPromotion = promotion;
    }

    public Move copy() {
        ChessPiece copiedPiece = this.piece.copy();
        ChessPiece copiedCapturedPiece = this.capturedPiece != null ? this.capturedPiece.copy() : null;
        ChessBoard copiedBoard = this.board.copy();

        Move copiedMove = new Move(copiedPiece, this.startSquare, this.endSquare, copiedCapturedPiece, copiedBoard);

        copiedMove.isPromotion = this.isPromotion;
        copiedMove.isCapture = this.isCapture;

        return copiedMove;
    }
}
