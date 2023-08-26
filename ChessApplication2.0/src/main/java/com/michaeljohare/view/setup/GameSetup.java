package com.michaeljohare.view.setup;

import com.michaeljohare.model.player.Player;
import com.michaeljohare.model.player.PlayerColor;
import com.michaeljohare.model.player.PlayerType;
import com.michaeljohare.model.player.PlayerSetup;

import javax.swing.*;

public class GameSetup {

    public static PlayerSetup getPlayerSetup() {
        StartupDialog playerDialog = createPlayerDialog();
        playerDialog.setVisible(true);

        Player player1 = createPlayer1(playerDialog);
        Player player2 = createPlayer2(playerDialog, player1.getColor());

        int elo = playerDialog.isPlayWithStockfish() ? playerDialog.getStockfishElo() : -1;

        return new PlayerSetup(player1, player2, elo);
    }

    private static StartupDialog createPlayerDialog() {
        JFrame tempFrame = new JFrame();
        return new StartupDialog(tempFrame);
    }

    private static Player createPlayer1(StartupDialog playerDialog) {
        String playerName1 = playerDialog.getPlayerName1();
        PlayerColor playerColor1 = playerDialog.getPlayerColor1();
        return new Player(playerColor1, PlayerType.HUMAN, playerName1);
    }

    private static Player createPlayer2(StartupDialog playerDialog, PlayerColor playerColor1) {
        if (playerDialog.isPlayWithStockfish()) {
            PlayerColor playerColor2 = (playerColor1 == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
            return new Player(playerColor2, PlayerType.AI, "Stockfish");
        } else {
            String playerName2 = playerDialog.getPlayerName2();
            PlayerColor playerColor2 = playerDialog.getPlayerColor2();
            return new Player(playerColor2, PlayerType.HUMAN, playerName2);
        }
    }
}
