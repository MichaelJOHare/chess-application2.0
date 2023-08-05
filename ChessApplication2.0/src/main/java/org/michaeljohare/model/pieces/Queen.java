package org.michaeljohare.model.pieces;


import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.movementstrategy.QueenMovementStrategy;
import org.michaeljohare.model.player.Player;

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
