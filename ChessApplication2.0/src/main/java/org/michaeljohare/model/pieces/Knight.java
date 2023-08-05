package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.movementstrategy.KnightMovementStrategy;
import org.michaeljohare.model.player.Player;

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
