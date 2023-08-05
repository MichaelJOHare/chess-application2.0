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
    private boolean isPlayer1AbleToCastleShort;
    private boolean isPlayer1AbleToCastleLong;
    private boolean isPlayer2AbleToCastleShort;
    private boolean isPlayer2AbleToCastleLong;
    private boolean isCheck;
    private boolean isCheckmate;
    private int moveNumber;
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
        isPlayer1AbleToCastleLong = true;
        isPlayer1AbleToCastleShort = true;
        isPlayer2AbleToCastleLong = true;
        isPlayer2AbleToCastleShort = true;
        player1CapturedPieces = new ArrayList<>();
        player2CapturedPieces = new ArrayList<>();
        isCheck = false;
        isCheckmate = false;
        moveNumber = 1;
    }

    public int getMoveNumber() {
        return moveNumber;
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

    public boolean isPlayer1AbleToCastleShort() {
        return isPlayer1AbleToCastleShort;
    }

    public boolean isPlayer1AbleToCastleLong() {
        return isPlayer1AbleToCastleLong;
    }

    public boolean isPlayer2AbleToCastleShort() {
        return isPlayer2AbleToCastleShort;
    }

    public boolean isPlayer2AbleToCastleLong() {
        return isPlayer2AbleToCastleLong;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public List<ChessPiece> getPlayer1CapturedPieces() {
        return player1CapturedPieces;
    }

    public List<ChessPiece> getPlayer2CapturedPieces() {
        return player2CapturedPieces;
    }

    public void swapPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opposingPlayer;
        opposingPlayer = temp;
    }

    public void setPlayer1AbleToCastleShort(boolean player1AbleToCastleShort) {
        isPlayer1AbleToCastleShort = player1AbleToCastleShort;
    }

    public void setPlayer1AbleToCastleLong(boolean player1AbleToCastleLong) {
        isPlayer1AbleToCastleLong = player1AbleToCastleLong;
    }

    public void setPlayer2AbleToCastleShort(boolean player2AbleToCastleShort) {
        isPlayer2AbleToCastleShort = player2AbleToCastleShort;
    }

    public void setPlayer2AbleToCastleLong(boolean player2AbleToCastleLong) {
        isPlayer2AbleToCastleLong = player2AbleToCastleLong;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    public void incrementMoveNumber() {
        this.moveNumber++;
    }

    public void decrementMoveNumber() {
        this.moveNumber--;
    }

    public void addPlayer1CapturedPieces(ChessPiece piece) {
        this.player1CapturedPieces.add(piece);
    }

    public void removePlayer1CapturedPieces(ChessPiece piece) {
        this.player1CapturedPieces.remove(piece);
    }

    public void addPlayer2CapturedPieces(ChessPiece piece) {
        this.player2CapturedPieces.add(piece);
    }
    public void removePlayer2CapturedPieces(ChessPiece piece) {
        this.player2CapturedPieces.remove(piece);
    }
}
