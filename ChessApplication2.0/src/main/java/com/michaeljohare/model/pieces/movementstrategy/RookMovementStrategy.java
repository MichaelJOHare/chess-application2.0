package com.michaeljohare.model.pieces.movementstrategy;

import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.moves.MoveHistory;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class RookMovementStrategy extends BaseMovementStrategy {

    @Override
    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };

        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];

            while (currentRow >= 0 && currentRow < 8 && currentCol >= 0 && currentCol < 8) {
                Square currentSquare = piece.getCurrentSquare();
                Square targetSquare = new Square(currentRow, currentCol);

                if (board.isOccupied(currentRow, currentCol)) {
                    if (board.isOccupiedByOpponent(currentRow, currentCol, piece.getPlayer())) {
                        ChessPiece capturedPiece = board.getPieceAt(currentRow, currentCol);
                        Move tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                        rawLegalMoves.add(tempMove);
                    }
                    break;
                }

                Move tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                rawLegalMoves.add(tempMove);

                currentRow += direction[0];
                currentCol += direction[1];
            }
        }

        return rawLegalMoves;
    }
}
