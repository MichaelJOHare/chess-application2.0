package org.michaeljohare.view.setup;

import org.michaeljohare.model.player.Player;
import org.michaeljohare.model.player.PlayerColor;
import org.michaeljohare.utils.PlayerSetup;

import javax.swing.*;

public class GameSetup {

    public static PlayerSetup getPlayerSetup() {
        JFrame tempFrame = new JFrame();
        StartupDialog playerDialog = new StartupDialog(tempFrame);
        playerDialog.setVisible(true);

        String playerName1 = playerDialog.getPlayerName1();
        PlayerColor playerColor1 = playerDialog.getPlayerColor1();
        Player player1 = null, player2 = null;
        int elo = -1;

        if (playerName1 != null && !playerName1.isEmpty()) {
            player1 = new Player(playerColor1, playerName1, true);
        }

        if (playerDialog.isPlayWithStockfish()) {
            player2 = new Player(playerColor1 == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE, "Stockfish", false);
            elo = playerDialog.getStockfishElo();
        } else {
            String playerName2 = playerDialog.getPlayerName2();
            PlayerColor playerColor2 = playerDialog.getPlayerColor2();
            if (playerName2 != null && !playerName2.isEmpty()) {
                player2 = new Player(playerColor2, playerName2, false);
            }
        }

        return new PlayerSetup(player1, player2, elo);
    }
}
