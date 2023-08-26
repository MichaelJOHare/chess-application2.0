package com.michaeljohare.model.pieces;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.movementstrategy.BishopMovementStrategy;

public class Bishop extends ChessPiece {

    public static final String WHITE_BISHOP = "♝";
    public static final String BLACK_BISHOP = "♗";

    public Bishop(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.BISHOP, new BishopMovementStrategy());
    }

    @Override
    public String getWhiteChessPieceSymbol() {
        return WHITE_BISHOP;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_BISHOP;
    }
}
