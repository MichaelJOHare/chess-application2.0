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

public class PawnMovementStrategy implements MovementStrategy {

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

    public List<Move> calculateRawLegalMoves(ChessBoard board, ChessPiece piece, MoveHistory move) {
        Move tempMove;
        Square currentSquare;
        Square targetSquare;
        ChessPiece capturedPiece;
        Square originalSquareBeforeCapture;
        List<Move> legalMoves = new ArrayList<>();
        int row = piece.getCurrentSquare().getRow(), col = piece.getCurrentSquare().getCol();

        /*
         * White Pieces
         */

        if (piece.getPlayer().isWhite()) {
            // Normal moves moves
            if (row > 0 && board.isEmpty(row - 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col), null, board));
            }
            if (row == 6 && board.isEmpty(row - 2, col) && board.isEmpty(row - 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 2, col), null, board));
            }

            // Normal captures
            if (row > 0 && col < 7 && board.isOccupiedByOpponent(row - 1, col + 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col + 1), board.getPieceAt(row - 1, col + 1), board));
            }
            if (row > 0 && col > 0 && board.isOccupiedByOpponent(row - 1, col - 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row - 1, col - 1), board.getPieceAt(row - 1, col - 1), board));
            }
            // En Passant Capture
            // Separate this out at some point (DRY)
            Move lastMove = move.getLastMove();
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    !lastMove.getPiece().getPlayer().isWhite() &&
                    lastMove.getStartSquare().getRow() == 1 && lastMove.getEndSquare().getRow() == 3 &&
                    row == 3 &&
                    Math.abs(col - lastMove.getEndSquare().getCol()) == 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row - 1, lastMove.getEndSquare().getCol());
                capturedPiece = lastMove.getPiece();
                originalSquareBeforeCapture = lastMove.getPiece().getCurrentSquare();
                tempMove = new EnPassantMove(piece, currentSquare, targetSquare, originalSquareBeforeCapture, capturedPiece, board);
                legalMoves.add(tempMove);
            }
            // Promotions
            // Captures with promotion
            if (row == 1 && col < 7 && board.isOccupiedByOpponent(row - 1, col + 1, piece.getPlayer())) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row - 1, col + 1), board.getPieceAt(row - 1, col + 1), promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
            if (row == 1 && col > 0 && board.isOccupiedByOpponent(row - 1, col - 1, piece.getPlayer())) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row - 1, col - 1), board.getPieceAt(row - 1, col - 1), promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
            // Normal move with promotion
            if (row == 1) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row - 1, col), null, promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
        }else {

            /*
            Black Pieces
            */

            // Normal moves
            if (row < 7 && board.isEmpty(row + 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col), null, board));
            }
            if (row == 1 && board.isEmpty(row + 2, col) && board.isEmpty(row + 1, col)) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 2, col), null, board));
            }

            // Normal captures
            if (row < 7 && col < 7 && board.isOccupiedByOpponent(row + 1, col + 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col + 1), board.getPieceAt(row + 1, col + 1), board));
            }
            if (row < 7 && col > 0 && board.isOccupiedByOpponent(row + 1, col - 1, piece.getPlayer())) {
                legalMoves.add(new Move(piece, new Square(row, col), new Square(row + 1, col - 1), board.getPieceAt(row + 1, col - 1), board));
            }
            // En Passant Capture
            Move lastMove = move.getLastMove();
            if (lastMove != null && lastMove.getPiece().getType().equals(PieceType.PAWN) &&
                    lastMove.getPiece().getPlayer().isWhite() &&
                    lastMove.getStartSquare().getRow() == 6 && lastMove.getEndSquare().getRow() == 4 &&
                    row == 4 &&
                    Math.abs(col - lastMove.getEndSquare().getCol()) == 1) {
                currentSquare = new Square(row, col);
                targetSquare = new Square(row + 1, lastMove.getEndSquare().getCol());
                capturedPiece = lastMove.getPiece();
                originalSquareBeforeCapture = lastMove.getPiece().getCurrentSquare();
                tempMove = new EnPassantMove(piece, currentSquare, targetSquare, originalSquareBeforeCapture, capturedPiece, board);
                legalMoves.add(tempMove);
            }
            // Promotions
            if (row == 6 && col < 7 && board.isOccupiedByOpponent(row + 1, col + 1, piece.getPlayer())) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row + 1, col + 1), board.getPieceAt(row + 1, col + 1), promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
            if (row == 6 && col > 0 && board.isOccupiedByOpponent(row + 1, col - 1, piece.getPlayer())) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row + 1, col - 1), board.getPieceAt(row + 1, col - 1), promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
            if (row == 6) {
                for (PieceType promotionType : new PieceType[] {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT}) {
                    PromotionMove promotionMove = new PromotionMove(piece, new Square(row, col), new Square(row + 1, col),null, promotionType, board);
                    promotionMove.setPromotion(true);
                    legalMoves.add(promotionMove);
                }
            }
        }
        return legalMoves;
    }
}
