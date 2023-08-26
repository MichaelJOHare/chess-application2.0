package com.michaeljohare.model.pieces;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.movementstrategy.KnightMovementStrategy;

public class Knight extends ChessPiece {

    public static final String WHITE_KNIGHT = "♞";
    public static final String BLACK_KNIGHT = "♘";

    public Knight(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.KNIGHT, new KnightMovementStrategy());
    }

    @Override
    public String getWhiteChessPieceSymbol() {
        return WHITE_KNIGHT;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_KNIGHT;
    }
}
