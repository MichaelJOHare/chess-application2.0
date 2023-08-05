package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.movementstrategy.BishopMovementStrategy;
import org.michaeljohare.model.player.Player;

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
