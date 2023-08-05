package org.michaeljohare.model.player;

import java.util.Objects;

import static org.michaeljohare.model.player.PlayerColor.WHITE;

public class Player {
    private PlayerColor color;
    private String name;

    public Player(PlayerColor color, String name) {
        this.name = name;
        this.color = color;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return color == player.color && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, name);
    }

}
