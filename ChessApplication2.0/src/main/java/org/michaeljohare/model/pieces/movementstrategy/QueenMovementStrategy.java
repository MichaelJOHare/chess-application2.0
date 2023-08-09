package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class QueenMovementStrategy implements MovementStrategy {

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
        ChessPiece copiedPiece = copiedBoard.getPieceAt(piece.getCurrentSquare().getRow(), piece.getCurrentSquare().getCol());;
        Player copiedPlayer = copiedPiece.getPlayer().copy();

        Move copiedMove = new Move(copiedPiece, m.getStartSquare(), m.getEndSquare(), copiedBoard.getPieceAt(m.getEndSquare().getRow(), m.getEndSquare().getCol()), copiedBoard);

        copiedMoveHistory.makeMove(copiedMove);
        copiedBoard.initializePieceManager();

        return copiedBoard.isKingInCheck(copiedPlayer, copiedMoveHistory, copiedBoard);
    }
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
