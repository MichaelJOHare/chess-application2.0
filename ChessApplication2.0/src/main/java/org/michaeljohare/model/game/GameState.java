package org.michaeljohare.model.game;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;
import org.michaeljohare.model.player.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Player currentPlayer;
    private Player opposingPlayer;
    private Player player1;
    private Player player2;
    private final ChessBoard board;
    private List<ChessPiece> capturedPieces;
    private List<ChessPiece> player1CapturedPieces;
    private List<ChessPiece> player2CapturedPieces;

    public GameState(ChessBoard board) {
        this.board = board;
        init();
    }

    public void init() {
        player1 = new Player(PlayerColor.WHITE, "Mike");
        player2 = new Player(PlayerColor.BLACK, "Bob");
        board.init(player1, player2);
        currentPlayer = player1;
        opposingPlayer = player2;
        capturedPieces = new ArrayList<>();
        player1CapturedPieces = new ArrayList<>();
        player2CapturedPieces = new ArrayList<>();
    }

    public GameStateMemento createMemento() {
        return new GameStateMemento(
                currentPlayer, opposingPlayer,
                player1, player2, capturedPieces,
                player1CapturedPieces,
                player2CapturedPieces
        );
    }

    public void restoreFromMemento(GameStateMemento memento) {
        currentPlayer = memento.getCurrentPlayer();
        opposingPlayer = memento.getOpposingPlayer();
        player1 = memento.getPlayer1();
        player2 = memento.getPlayer2();
        capturedPieces = new ArrayList<>(memento.getCapturedPieces());
        player1CapturedPieces = new ArrayList<>(memento.getPlayer1CapturedPieces());
        player2CapturedPieces = new ArrayList<>(memento.getPlayer2CapturedPieces());
    }

    public void swapPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opposingPlayer;
        opposingPlayer = temp;
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

    public List<ChessPiece> getCapturedPieces() {
        return capturedPieces;
    }

    public void updateCapturedPieces() {
        for (ChessPiece piece : capturedPieces) {
            if (piece.getPlayer().equals(player1) && !player1CapturedPieces.contains(piece)) {
                player1CapturedPieces.add(piece);
            } else if (piece.getPlayer().equals(player2) && !player2CapturedPieces.contains(piece)) {
                player2CapturedPieces.add(piece);
            }
        }
    }

    public List<ChessPiece> getPlayer1CapturedPieces() {
        return player1CapturedPieces;
    }

    public List<ChessPiece> getPlayer2CapturedPieces() {
        return player2CapturedPieces;
    }
}
