package org.michaeljohare.model.moves;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceType;

public class PromotionMove extends Move {
    private PieceType promotionType;

    public PromotionMove(ChessPiece pawn, Square startSquare, Square endSquare, ChessPiece capturedPiece,
                         PieceType promotionType, ChessBoard board) {
        super(pawn, startSquare, endSquare, capturedPiece, board);
        this.promotionType = promotionType;
    }

    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }
}