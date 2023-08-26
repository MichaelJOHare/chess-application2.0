package com.michaeljohare.model.pieces;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.pieces.movementstrategy.RookMovementStrategy;

public class Rook extends ChessPiece implements PieceWithMoveStatus {

    public static final String WHITE_ROOK = "♜";
    public static final String BLACK_ROOK = "♖";
    private boolean hasMoved;

    public Rook(Square currentSquare, Player player) {
        super(currentSquare, player, PieceType.ROOK, new RookMovementStrategy());
        this.hasMoved = false;
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
        return WHITE_ROOK;
    }

    @Override
    public String getBlackChessPieceSymbol() {
        return BLACK_ROOK;
    }
}
