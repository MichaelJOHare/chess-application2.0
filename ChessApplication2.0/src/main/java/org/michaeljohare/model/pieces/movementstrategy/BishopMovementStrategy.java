package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class BishopMovementStrategy implements MovementStrategy{

    Move tempMove;
    Square currentSquare;
    Square targetSquare;
    ChessPiece capturedPiece;

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        // Bishop Diagonal (NorthEast) Moves
        while (row < 7 && col < 7 && board.isEmpty(row + 1, col + 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row++;
            col++;
        }
        // Bishop Diagonal (NorthEast) Capture
        if (row < 7 && col < 7 && board.isOccupiedByOpponent(row + 1, col + 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col + 1);
            capturedPiece = board.getPieceAt(row + 1, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();
        col = piece.getCurrentSquare().getCol();

        // Bishop Diagonal (SouthWest) Moves
        while (row < 7 && col > 0 && board.isEmpty(row + 1, col - 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row++;
            col--;
        }
        // Bishop Diagonal (SouthWest) Capture
        if (row < 7 && col > 0 && board.isOccupiedByOpponent(row + 1, col - 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row + 1, col - 1);
            capturedPiece = board.getPieceAt(row + 1, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();
        col = piece.getCurrentSquare().getCol();

        // Bishop Diagonal (NorthWest) Moves
        while (row > 0 && col > 0 && board.isEmpty(row - 1, col - 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row--;
            col--;
        }
        // Bishop Diagonal (NorthWest) Capture
        if (row > 0 && col > 0 && board.isOccupiedByOpponent(row - 1, col - 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col - 1);
            capturedPiece = board.getPieceAt(row - 1, col - 1);
            tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
        }
        row = piece.getCurrentSquare().getRow();
        col = piece.getCurrentSquare().getCol();

        // Bishop Diagonal (SouthEast) Moves
        while (row > 0 && col < 7 && board.isEmpty(row - 1, col + 1)) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col + 1);
            tempMove = new Move(piece, currentSquare, targetSquare, null, board);
            move.makeMove(tempMove);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(tempMove);
            }
            move.undoMove();
            row--;
            col++;
        }
        // Bishop Diagonal (SouthEast) Capture
        if (row > 0 && col < 7 && board.isOccupiedByOpponent(row - 1, col + 1, piece.getPlayer())) {
            currentSquare = piece.getCurrentSquare();
            targetSquare = new Square(row - 1, col + 1);
            capturedPiece = board.getPieceAt(row - 1, col + 1);
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
