package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class BishopMovementStrategy implements MovementStrategy{

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = calculateRawLegalMoves(board, piece, move);
        List<Move> legalMoves = new ArrayList<>();
        for (Move m : rawLegalMoves) {
            if (!wouldResultInCheck(board, piece, move, m)) {
                legalMoves.add(m);
            }
        }
        return legalMoves;
    }

    public boolean wouldResultInCheck(ChessBoard board, ChessPiece piece, MoveHistory move, Move m) {
        ChessBoard copiedBoard = board.copy();
        MoveHistory copiedMoveHistory = move.copy();
        Move copiedMove = m.copy();

        copiedMoveHistory.makeMove(copiedMove);

        return copiedBoard.isKingInCheck(piece.getPlayer(), move);
    }
    @Override
    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {;
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        // Four diagonal directions
        int[][] directions = { {1, 1}, {1, -1}, {-1, -1}, {-1, 1} };

        for (int[] direction : directions) {
            int newRow = row, newCol = col;
            while (true) {
                newRow += direction[0];
                newCol += direction[1];

                if (newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) {
                    break;
                }

                Move tempMove;
                Square currentSquare = piece.getCurrentSquare();
                Square targetSquare = new Square(newRow, newCol);
                if (board.isEmpty(newRow, newCol)) {
                    tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                    legalMoves.add(tempMove);
                } else {
                    if (board.isOccupiedByOpponent(newRow, newCol, piece.getPlayer())) {
                        ChessPiece capturedPiece = board.getPieceAt(newRow, newCol);
                        tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                        legalMoves.add(tempMove);
                    }
                    break;
                }
            }
        }
        return legalMoves;
    }
}
