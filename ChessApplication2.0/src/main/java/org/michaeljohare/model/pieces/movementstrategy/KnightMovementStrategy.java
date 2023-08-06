package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class KnightMovementStrategy implements MovementStrategy {

    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = calculateRawLegalMoves(board, piece, move);
        List<Move> legalMoves = new ArrayList<>();
        for (Move m : rawLegalMoves) {
            move.makeMove(m);
            if (!board.isKingInCheck(piece.getPlayer(), move)) {
                legalMoves.add(m);
            }
            move.undoMove();
        }
        return legalMoves;
    }

    @Override
    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        int[][] knightMoves = {
                { -2, 1 }, { -1, 2 }, { 2, 1 }, { 1, 2 },
                { -2, -1 }, { -1, -2 }, { 2, -1 }, { 1, -2 }
        };

        for (int[] m : knightMoves) {
            int targetRow = row + m[0];
            int targetCol = col + m[1];
            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                Square currentSquare = piece.getCurrentSquare();
                Square targetSquare = new Square(targetRow, targetCol);
                ChessPiece capturedPiece = null;
                if (board.isOccupied(targetRow, targetCol)) {
                    if (board.isOccupiedByOpponent(targetRow, targetCol, piece.getPlayer())) {
                        capturedPiece = board.getPieceAt(targetRow, targetCol);
                    } else {
                        continue;
                    }
                }
                Move tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                rawLegalMoves.add(tempMove);
            }
        }
        return rawLegalMoves;
    }
}