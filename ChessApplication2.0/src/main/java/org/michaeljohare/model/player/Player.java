package org.michaeljohare.model.player;

import java.util.Objects;

import static org.michaeljohare.model.player.PlayerColor.WHITE;

public class Player {
    private PlayerColor color;
    private String name;
    private boolean isPlayer1;

    public Player(PlayerColor color, String name, boolean isPlayer1) {
        this.name = name;
        this.color = color;
        this.isPlayer1 = isPlayer1;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isWhite() {
        return color == WHITE;
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public Player copy() {
        return new Player(this.color, this.name, this.isPlayer1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return color == player.color && Objects.equals(name, player.name) && isPlayer1 == player.isPlayer1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, name);
    }

}
