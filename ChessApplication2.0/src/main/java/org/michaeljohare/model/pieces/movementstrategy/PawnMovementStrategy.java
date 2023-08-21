package org.michaeljohare.model.pieces.movementstrategy;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.EnPassantMove;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.moves.PromotionMove;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

public class PawnMovementStrategy extends BaseMovementStrategy {

    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        addNormalMoves(row, col, piece, board, legalMoves);
        addEnPassantMoves(row, col, piece, board, move, legalMoves);
        addPromotionMoves(row, col, piece, board, legalMoves);
        return legalMoves;
    }

    private void addNormalMoves(int row, int col, ChessPiece piece, ChessBoard board, List<Move> legalMoves) {
        int direction = piece.getPlayer().isWhite() ? -1 : 1;
        int backRank = piece.getPlayer().isWhite() ? 0 : 7;
        int startingRow = piece.getPlayer().isWhite() ? 6 : 1;

        handleNormalMove(row, col, direction, backRank, piece, board, legalMoves);
        handleDoubleMove(row, col, direction, startingRow, piece, board, legalMoves);
        // Left capture
        handleCapture(row, col, direction, -1, piece, board, legalMoves);
        // Right capture
        handleCapture(row, col, direction, 1, piece, board, legalMoves);
    }

    private void handleNormalMove(int row, int col, int direction, int backRank, ChessPiece piece, ChessBoard board, List<Move> legalMoves) {
        if ((direction == -1 && row > backRank) || (direction == 1 && row < backRank)) {
            int newRow = row + direction;
            if (board.isEmpty(newRow, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(newRow, col), null, board));
            }
        }
    }

    private void handleDoubleMove(int row, int col, int direction, int startingRow, ChessPiece piece, ChessBoard board, List<Move> legalMoves) {
        if (row == startingRow && board.isEmpty(row + direction, col) && board.isEmpty(row + 2 * direction, col)) {
            legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 2 * direction, col), null, board));
        }
    }

    private void handleCapture(int row, int col, int direction, int colOffset, ChessPiece piece, ChessBoard board, List<Move> legalMoves) {
        int newRow = row + direction;
        int newCol = col + colOffset;
        if (newCol >= 0 && newCol <= 7 && board.isOccupiedByOpponent(newRow, newCol, piece.getPlayer())) {
            legalMoves.add(new Move(piece, new Square(row, col), new Square(newRow, newCol), board.getPieceAt(newRow, newCol), board));
        }
    }

    private void addEnPassantMoves(int row, int col, ChessPiece piece, ChessBoard board, MoveHistory move, List<Move> legalMoves) {
        int capturedPawnStartingRow = piece.getPlayer().isWhite() ? 1 : 6; // Better variable name?
        int capturedPawnEndRow = piece.getPlayer().isWhite() ? 3 : 4;
        int direction = piece.getPlayer().isWhite() ? -1 : 1;
        Move lastMove = move.getLastMove();

        if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                lastMove.getStartSquare().getRow() == capturedPawnStartingRow && // Kind of confusing
                lastMove.getEndSquare().getRow() == capturedPawnEndRow &&
                row == capturedPawnEndRow &&
                Math.abs(col - lastMove.getEndSquare().getCol()) == 1) {

            Square currentSquare = new Square(row, col);
            Square targetSquare = new Square(row + direction, lastMove.getEndSquare().getCol());
            ChessPiece capturedPiece = lastMove.getPiece();
            Square originalSquareBeforeCapture = lastMove.getPiece().getCurrentSquare();
            Move tempMove = new EnPassantMove(piece, currentSquare, targetSquare, originalSquareBeforeCapture, capturedPiece, board);
            legalMoves.add(tempMove);
        }
    }

    private void addPromotionMoves(int row, int col, ChessPiece piece, ChessBoard board, List<Move> legalMoves) {
        int direction = piece.getPlayer().isWhite() ? -1 : 1;
        int rowBeforePromotionRow = piece.getPlayer().isWhite() ? 1 : 6;

        // Captures with promotion
        if (row == rowBeforePromotionRow && col < 7 && board.isOccupiedByOpponent(row + direction, col + 1, piece.getPlayer())) {
            for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row + direction, col + 1), board.getPieceAt(row + direction, col + 1), promotionType, board);
                promotionMove.setPromotion(true);
                legalMoves.add(promotionMove);
            }
        }
        if (row == rowBeforePromotionRow && col > 0 && board.isOccupiedByOpponent(row + direction, col - 1, piece.getPlayer())) {
            for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row  + direction, col - 1), board.getPieceAt(row + direction, col - 1), promotionType, board);
                promotionMove.setPromotion(true);
                legalMoves.add(promotionMove);
            }
        }
        // Normal move with promotion
        if (row == rowBeforePromotionRow && board.isEmpty(row + direction, col)) {
            for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row + direction, col), null, promotionType, board);
                promotionMove.setPromotion(true);
                legalMoves.add(promotionMove);
            }
        }
    }
}
