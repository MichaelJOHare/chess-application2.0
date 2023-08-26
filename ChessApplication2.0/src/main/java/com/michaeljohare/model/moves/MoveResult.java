package com.michaeljohare.model.moves;

import com.michaeljohare.model.pieces.ChessPiece;

public class MoveResult {
    public enum MoveType {
        NORMAL,
        INVALID,
        PROMOTION
    }

    private final MoveType moveType;
    private final ChessPiece promotedPiece;

    // Constructor for normal or invalid moves
    public MoveResult(MoveType moveType) {
        if(moveType == MoveType.PROMOTION) {
            throw new IllegalArgumentException("Use the other constructor for promotion moves.");
        }
        this.moveType = moveType;
        this.promotedPiece = null;
    }

    // Constructor for promotion moves
    public MoveResult(ChessPiece promotedPiece) {
        this.moveType = MoveType.PROMOTION;
        this.promotedPiece = promotedPiece;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public boolean isPromotion() {
        return moveType == MoveType.PROMOTION;
    }
}
