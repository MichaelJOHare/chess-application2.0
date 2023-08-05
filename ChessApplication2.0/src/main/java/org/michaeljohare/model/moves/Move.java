package org.michaeljohare.model.moves;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;

public class Move implements Movable{

    ChessPiece piece;
    Square startSquare;
    Square endSquare;
    ChessPiece capturedPiece;
    ChessBoard board;

    public Move(ChessPiece piece, Square from, Square to, ChessPiece capturedPiece, ChessBoard board) {
        this.piece = piece;
        this.startSquare = from;
        this.endSquare = to;
        this.capturedPiece = capturedPiece;
        this.board = board;
    }

    @Override
    public void execute() {
        if (capturedPiece != null) {
            board.removePiece(capturedPiece);
            capturedPiece.setCurrentSquare(null);
        }
        board.removePiece(piece);
        piece.setCurrentSquare(endSquare);
        board.addPiece(piece);
    }

    @Override
    public void undo() {
        board.removePiece(piece);
        piece.setCurrentSquare(startSquare);
        board.addPiece(piece);
        if (capturedPiece != null) {
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

    public Square getStartSquare() {
        return startSquare;
    }

    public Square getEndSquare() {
        return endSquare;
    }

    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
}
