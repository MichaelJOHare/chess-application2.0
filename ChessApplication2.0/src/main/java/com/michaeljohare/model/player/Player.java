package com.michaeljohare.model.player;

import java.util.Objects;

public class Player {
    private final PlayerColor color;
    private final PlayerType type;
    private final String name;

    public Player(PlayerColor color, PlayerType type, String name) {
        this.color = color;
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public boolean isWhite() {
        return color == PlayerColor.WHITE;
    }
    public boolean isStockfish(){
        return type == PlayerType.AI;
    }

    public Player copy() {
        return new Player(this.color, this.type, this.name);
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
        return color == player.color && type == player.type && Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, name, type);
    }

}
