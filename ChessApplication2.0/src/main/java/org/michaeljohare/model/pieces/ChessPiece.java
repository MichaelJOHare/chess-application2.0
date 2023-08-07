package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.movementstrategy.MovementStrategy;
import org.michaeljohare.model.player.Player;

import java.util.List;

public abstract class ChessPiece {

    protected Square currentSquare;
    private final MovementStrategy movementStrategy;

    protected Player player;
    protected PieceType type;
    protected boolean isAlive;

    public ChessPiece(Square currentSquare, Player player, PieceType type, MovementStrategy movementStrategy) {
        this.currentSquare = currentSquare;
        this.player = player;
        this.type = type;
        this.movementStrategy = movementStrategy;
        this.isAlive = true;
    }

    public abstract String getWhiteChessPieceSymbol();
    public abstract String getBlackChessPieceSymbol();
    public List<Move> calculateLegalMoves(ChessBoard board, MoveHistory move) {
        return movementStrategy.calculateLegalMoves(board, this, move);
    }
    public List<Move> calculateRawLegalMoves(ChessBoard board, MoveHistory move) {
        return movementStrategy.calculateRawLegalMoves(board, this, move);
    }

    public Square getCurrentSquare() {
        return currentSquare;
    }

    public void setCurrentSquare(Square currentSquare) {
        this.currentSquare = currentSquare;
    }

    public Player getPlayer() {
        return player;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void kill() {
        isAlive = false;
    }

    public void revive() {
        isAlive = true;
    }
}
