package org.michaeljohare.model.moves;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceWithMoveStatus;

public class Move implements Movable{

    ChessPiece piece;
    Square startSquare;
    Square endSquare;
    ChessPiece capturedPiece;
    ChessBoard board;
    boolean isPromotion;
    boolean isCapture;

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
        Square copiedStartSquare = new Square(this.startSquare.getRow(), this.startSquare.getCol());
        Square copiedEndSquare = new Square(this.endSquare.getRow(), this.endSquare.getCol());
        ChessPiece copiedCapturedPiece = this.capturedPiece != null ? this.capturedPiece.copy() : null;
        ChessBoard copiedBoard = this.board.copy();

        Move copiedMove = new Move(copiedPiece, copiedStartSquare, copiedEndSquare, copiedCapturedPiece, copiedBoard);

        copiedMove.isPromotion = this.isPromotion;
        copiedMove.isCapture = this.isCapture;

        return copiedMove;
    }
}
