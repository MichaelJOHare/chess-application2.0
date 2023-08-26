package com.michaeljohare.model.pieces;


import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.pieces.movementstrategy.QueenMovementStrategy;

public class Queen extends ChessPiece {

    public static final String WHITE_QUEEN = "♛";
    public static final String BLACK_QUEEN = "♕";

    public Queen(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.QUEEN, new QueenMovementStrategy());
    }

    @Override
    public String getWhiteChessPieceSymbol() {
        return WHITE_QUEEN;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_QUEEN;
    }
}
