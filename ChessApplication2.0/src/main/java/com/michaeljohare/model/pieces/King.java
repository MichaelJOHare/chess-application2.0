package com.michaeljohare.model.pieces;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.movementstrategy.KingMovementStrategy;

public class King extends ChessPiece implements PieceWithMoveStatus {

    public static final String BLACK_KING = "♔";
    public static final String WHITE_KING = "♚";
    private boolean hasMoved;

    public King(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.KING, new KingMovementStrategy());
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public String getWhiteChessPieceSymbol() {
        return WHITE_KING;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_KING;
    }
}
