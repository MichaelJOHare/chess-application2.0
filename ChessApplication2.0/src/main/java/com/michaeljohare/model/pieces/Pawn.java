package com.michaeljohare.model.pieces;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.movementstrategy.PawnMovementStrategy;

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
