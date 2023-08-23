package org.michaeljohare.model.pieces;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.moves.MoveHistory;
import org.michaeljohare.model.pieces.movementstrategy.MovementStrategy;
import org.michaeljohare.model.player.Player;
import org.michaeljohare.model.player.PlayerColor;

import java.util.List;
import java.util.Objects;

public abstract class ChessPiece implements Cloneable {

    protected Square currentSquare;

    protected Player player;
    protected PieceType type;
    protected boolean isAlive;
    private final MovementStrategy movementStrategy;

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

    public ChessPiece promotePiece(PieceType type, Player player, Square square) {
        switch (type) {
            case QUEEN:
                return new Queen(square, player);
            case ROOK:
                return new Rook(square, player);
            case BISHOP:
                return new Bishop(square, player);
            case KNIGHT:
                return new Knight(square, player);
            default:
                throw new IllegalArgumentException("Invalid piece type");
        }
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

    public char pieceToFEN() {
        switch (type) {
            case KING:
                return player.getColor() == PlayerColor.WHITE ? 'K' : 'k';
            case QUEEN:
                return player.getColor() == PlayerColor.WHITE ? 'Q' : 'q';
            case ROOK:
                return player.getColor() == PlayerColor.WHITE ? 'R' : 'r';
            case BISHOP:
                return player.getColor() == PlayerColor.WHITE ? 'B' : 'b';
            case KNIGHT:
                return player.getColor() == PlayerColor.WHITE ? 'N' : 'n';
            case PAWN:
                return player.getColor() == PlayerColor.WHITE ? 'P' : 'p';
            default:
                throw new IllegalArgumentException("Unknown piece type");
        }
    }

    public ChessPiece copy() {
        try {
            ChessPiece copiedPiece = (ChessPiece) super.clone();
            copiedPiece.currentSquare = this.currentSquare.copy();
            return copiedPiece;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, player.getColor(), currentSquare);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessPiece that = (ChessPiece) obj;

        return type == that.type &&
                player.getColor() == that.player.getColor() &&
                Objects.equals(currentSquare, that.currentSquare);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+ "[" + player.getColor() + "]";
    }
}
