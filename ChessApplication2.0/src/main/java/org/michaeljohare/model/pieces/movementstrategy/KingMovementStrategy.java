package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.CastlingMove;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.King;
import org.michaeljohare.model.pieces.Rook;

import java.util.ArrayList;
import java.util.List;

import static org.michaeljohare.model.board.ChessBoard.ROOK_COLUMN_1;
import static org.michaeljohare.model.board.ChessBoard.ROOK_COLUMN_2;

public class KingMovementStrategy extends BaseMovementStrategy {


    @Override
    public List<Move> calculateLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = super.calculateLegalMoves(board, piece, move);
        addCastlingMoves(board, piece, legalMoves);
        return legalMoves;
    }

    @Override
    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> rawLegalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        // 8 directions similar to Queen but only one step
        int[][] directions = { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        for (int[] direction : directions) {
            int targetRow = row + direction[0];
            int targetCol = col + direction[1];

            if (targetRow >= 0 && targetRow < 8 && targetCol >= 0 && targetCol < 8) {
                Square currentSquare = piece.getCurrentSquare();
                Square targetSquare = new Square(targetRow, targetCol);

                if (board.isOccupied(targetRow, targetCol)) {
                    if (board.isOccupiedByOpponent(targetRow, targetCol, piece.getPlayer())) {
                        ChessPiece capturedPiece = board.getPieceAt(targetRow, targetCol);
                        Move tempMove = new Move(piece, currentSquare, targetSquare, capturedPiece, board);
                        rawLegalMoves.add(tempMove);
                    }
                } else {
                    Move tempMove = new Move(piece, currentSquare, targetSquare, null, board);
                    rawLegalMoves.add(tempMove);
                }
            }
        }
        return rawLegalMoves;
    }

    private void addCastlingMoves(ChessBoard board, ChessPiece king, List<Move> legalMoves) {
        if (king instanceof King && !((King) king).hasMoved()) {
            addKingSideCastlingMove(board, king, legalMoves);
            addQueenSideCastlingMove(board, king, legalMoves);
        }
    }

    private void addKingSideCastlingMove(ChessBoard board, ChessPiece king, List<Move> legalMoves) {
        // Check if the king-side rook has not moved, and the squares between are empty
        Square rookSquare = new Square(king.getCurrentSquare().getRow(), ROOK_COLUMN_2);
        ChessPiece rook = board.getPieceAt(rookSquare.getRow(), rookSquare.getCol());
        if (rook instanceof Rook && !((Rook) rook).hasMoved() &&
                board.isEmpty(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() + 1) &&
                board.isEmpty(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() + 2)) {

            // Check if the squares that the king passes through are not attacked
            if (!board.isSquareAttackedByOpponent(king.getCurrentSquare().getRow(),
                    king.getCurrentSquare().getCol() + 1, king.getPlayer()) &&
                    !board.isSquareAttackedByOpponent(king.getCurrentSquare().getRow(),
                            king.getCurrentSquare().getCol() + 2, king.getPlayer())) {

                legalMoves.add(new CastlingMove(king, (Rook) rook, king.getCurrentSquare(),
                        new Square(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() + 2),
                        rookSquare, new Square(rookSquare.getRow(), rookSquare.getCol() - 2), board));
            }
        }
    }

    private void addQueenSideCastlingMove(ChessBoard board, ChessPiece king, List<Move> legalMoves) {

        Square rookSquare = new Square(king.getCurrentSquare().getRow(), ROOK_COLUMN_1);
        ChessPiece rook = board.getPieceAt(rookSquare.getRow(), rookSquare.getCol());
        if (rook instanceof Rook && !((Rook) rook).hasMoved() &&
                board.isEmpty(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() - 1) &&
                board.isEmpty(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() - 2)) {

            if (!board.isSquareAttackedByOpponent(king.getCurrentSquare().getRow(),
                    king.getCurrentSquare().getCol() - 1, king.getPlayer()) &&
                    !board.isSquareAttackedByOpponent(king.getCurrentSquare().getRow(),
                            king.getCurrentSquare().getCol() - 2, king.getPlayer())) {

                legalMoves.add(new CastlingMove(king, (Rook) rook, king.getCurrentSquare(),
                        new Square(king.getCurrentSquare().getRow(), king.getCurrentSquare().getCol() - 2),
                        rookSquare, new Square(rookSquare.getRow(), rookSquare.getCol() + 3), board));
            }
        }
    }
}
