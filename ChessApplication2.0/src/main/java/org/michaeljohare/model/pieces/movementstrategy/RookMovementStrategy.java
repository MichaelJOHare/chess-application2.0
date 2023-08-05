package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class RookMovementStrategy implements MovementStrategy {

    Move tempMove;
    Square currentSquare;
    Square targetSquare;
    ChessPiece capturedPiece;

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        // Rook Vertical (North) Moves
        while (row > 0 && board.isEmpty(row - 1, col)) {
            // Maybe use originSquare variable?
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row--;
        }
        // Rook Vertical (North) Capture
        if (row > 0 && board.isOccupiedByOpponent(row - 1, col, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col);
            capturedPiece = board.getPieceAt(row - 1, col);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();

        // Rook Vertical (South) Moves
        while (row < 7 && board.isEmpty(row + 1, col)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row++;
        }
        // Rook Vertical (South) Capture
        if (row < 7 && board.isOccupiedByOpponent(row + 1, col, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col);
            capturedPiece = board.getPieceAt(row + 1, col);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();

        // Rook Horizontal (East) Moves
        while (col < 7 && board.isEmpty(row, col + 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            col++;
        }
        // Rook Horizontal (East) Capture
        if (col < 7 && board.isOccupiedByOpponent(row, col + 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row, col + 1);
            capturedPiece = board.getPieceAt(row, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();

        // Rook Horizontal (West) Moves
        while (col > 0 && board.isEmpty(row, col - 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            col--;
        }
        // Rook Horizontal (West) Capture
        if (col > 0 && board.isOccupiedByOpponent(row, col - 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row, col - 1);
            capturedPiece = board.getPieceAt(row, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }

        return legalMoves;
    }
}
