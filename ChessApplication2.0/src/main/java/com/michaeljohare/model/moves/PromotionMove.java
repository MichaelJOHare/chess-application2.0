package com.michaeljohare.model.moves;

import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.pieces.PieceType;

public class PromotionMove extends Move {
    private final ChessPiece originalPiece;
    private PieceType promotionType;
    private ChessPiece promotedPiece;

    public PromotionMove(ChessPiece pawn, Square startSquare, Square endSquare, ChessPiece capturedPiece,
                         PieceType promotionType, ChessBoard board) {
        super(pawn, startSquare, endSquare, capturedPiece, board);
        this.promotionType = promotionType;
        this.originalPiece = pawn;
    }

    public void setPromotionType(PieceType promotionType) {
        this.promotionType = promotionType;
    }

    public PieceType getPromotionType() {
        return this.promotionType;
    }

    public ChessPiece getPromotedPiece() {
        return this.promotedPiece;
    }

    public ChessPiece getOriginalPiece() {
        return this.originalPiece;
    }

    @Override
    public void execute() {
        if (capturedPiece != null) {
            capturedPiece.kill();
            board.removePiece(capturedPiece);
        }

        PieceType chosenPromotion = getPromotionType();

        promotedPiece = originalPiece.promotePiece(chosenPromotion, originalPiece.getPlayer(), endSquare);
        board.removePiece(originalPiece);
        piece = promotedPiece;
        board.addPiece(promotedPiece);

    }

    @Override
    public void undo() {
        board.removePiece(promotedPiece);

        originalPiece.setCurrentSquare(startSquare);
        board.addPiece(originalPiece);

        if (capturedPiece != null) {
            capturedPiece.revive();
            board.removePiece(capturedPiece);
            capturedPiece.setCurrentSquare(endSquare);
            board.addPiece(capturedPiece);
        }
    }
}