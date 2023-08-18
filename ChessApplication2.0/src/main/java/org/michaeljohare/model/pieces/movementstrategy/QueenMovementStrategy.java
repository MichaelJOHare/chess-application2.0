package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;

public class QueenMovementStrategy extends BaseMovementStrategy {
    @Override
    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        // 8 directions combining rook and bishop movement
        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

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
