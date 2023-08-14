package org.michaeljohare.utils;

import org.michaeljohare.model.player.Player;

public class PlayerSetup {
    private final Player player1;
    private final Player player2;
    private final int elo;

    public PlayerSetup(Player player1, Player player2, int elo) {
        this.player1 = player1;
        this.player2 = player2;
        this.elo = elo;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getElo() {
        return elo;
    }

    public boolean isStockfishInPlay() {
        return elo != -1;
    }
}
