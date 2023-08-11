package org.michaeljohare.view.setup;

import org.michaeljohare.model.player.Player;
import org.michaeljohare.model.player.PlayerColor;
import org.michaeljohare.utils.Pair;

import javax.swing.*;

public class GameSetup {

    public static Pair<Player, Player> getPlayerDetails() {
        Player player1 = null, player2 = null;

        JFrame tempFrame = new JFrame();

        StartupDialog playerDialog = new StartupDialog(tempFrame);
        playerDialog.setVisible(true);

        String playerName1 = playerDialog.getPlayerName1();
        String playerName2 = playerDialog.getPlayerName2();
        PlayerColor playerColor1 = playerDialog.getPlayerColor1();
        PlayerColor playerColor2 = playerDialog.getPlayerColor2();
        boolean playWithStockfish = playerDialog.isPlayWithStockfish();

        if (playerName1 != null && !playerName1.isEmpty()) {
            player1 = new Player(playerColor1, playerName1, true);
        }

        if (playWithStockfish) {
            player2 = new Player(playerColor1 == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE, "Stockfish", false);
        } else if (playerName2 != null && !playerName2.isEmpty()) {
            player2 = new Player(playerColor2, playerName2, false);
        }

        return new Pair<>(player1, player2);
    }
}
