package com.michaeljohare.model.game;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.player.PlayerColor;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.player.PlayerSetup;
import com.michaeljohare.view.setup.GameSetup;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private final ChessBoard board;
    private Player currentPlayer;
    private Player opposingPlayer;
    private Player player1;
    private Player player2;
    private List<ChessPiece> capturedPieces;
    private List<ChessPiece> player1CapturedPieces;
    private List<ChessPiece> player2CapturedPieces;
    private boolean isGameOver;
    private boolean isBoardLocked = false;
    private int stockfishElo = -1;

    public GameState(ChessBoard board) {
        this.board = board;
        initializePlayers();
        init();
        isGameOver = false;
    }

    private void initializePlayers() {
        PlayerSetup setup = GameSetup.getPlayerSetup();

        this.player1 = setup.getPlayer1();
        this.player2 = setup.getPlayer2();

        if (setup.isStockfishInPlay()) {
            this.stockfishElo = setup.getElo();
        }
    }

    public void init() {
        board.init(player1, player2);

        if (player1.getColor() == PlayerColor.WHITE) {
            currentPlayer = player1;
            opposingPlayer = player2;
        } else {
            currentPlayer = player2;
            opposingPlayer = player1;
        }

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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isBoardLocked() {
        return isBoardLocked;
    }

    public void lockBoard() {
        isBoardLocked = true;
    }

    public void unlockBoard() {
        isBoardLocked = false;
    }

    public int getStockfishElo() {
        return stockfishElo;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void addCapturedPiece(ChessPiece capturedPiece) {
        capturedPieces.add(capturedPiece);
        updateCapturedPieces();
    }

    public void removeCapturedPiece(ChessPiece capturedPiece) {
        player1CapturedPieces.remove(capturedPiece);
        player2CapturedPieces.remove(capturedPiece);
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
