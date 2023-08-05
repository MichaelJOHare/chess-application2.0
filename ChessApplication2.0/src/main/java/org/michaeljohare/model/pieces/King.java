package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.pieces.movementstrategy.KingMovementStrategy;
import org.michaeljohare.model.player.Player;

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
