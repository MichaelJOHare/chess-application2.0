package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.movementstrategy.PawnMovementStrategy;
import org.michaeljohare.model.player.Player;

public class Pawn extends ChessPiece {

    public static final String WHITE_PAWN = "♟";
    public static final String BLACK_PAWN = "♙";

    public Pawn(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.PAWN, new PawnMovementStrategy());
    }

    @Override
    public String getWhiteChessPieceSymbol() {
        return WHITE_PAWN;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_PAWN;
    }
}
