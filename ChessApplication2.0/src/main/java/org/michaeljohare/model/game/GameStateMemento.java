package org.michaeljohare.model.game;

import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class GameStateMemento {
    private final Player currentPlayer;
    private final Player opposingPlayer;
    private final Player player1;
    private final Player player2;
    private final boolean isCheck;
    private final boolean isCheckmate;
    private final int moveNumber;
    private final List<ChessPiece> player1CapturedPieces;
    private final List<ChessPiece> player2CapturedPieces;

    public GameStateMemento(
            Player currentPlayer, Player opposingPlayer,
            Player player1, Player player2,
            boolean isCheck, boolean isCheckmate,
            int moveNumber,
            List<ChessPiece> player1CapturedPieces,
            List<ChessPiece> player2CapturedPieces) {
        this.currentPlayer = currentPlayer;
        this.opposingPlayer = opposingPlayer;
        this.player1 = player1;
        this.player2 = player2;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.moveNumber = moveNumber;
        this.player1CapturedPieces = new ArrayList<>(player1CapturedPieces);
        this.player2CapturedPieces = new ArrayList<>(player2CapturedPieces);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpposingPlayer() {
        return opposingPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public List<ChessPiece> getPlayer1CapturedPieces() {
        return player1CapturedPieces;
    }

    public List<ChessPiece> getPlayer2CapturedPieces() {
        return player2CapturedPieces;
    }
}
