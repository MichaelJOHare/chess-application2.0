package org.michaeljohare;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.michaeljohare.model.game.GameManager;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        new GameManager();
    }

    /*
     * TODO
     *  Implement 50 move rule
     *  Refactor and clean up
     *  Implement stockfish as player
     *  Implement letting player choose name/opponent
     *  Try to reproduce checkmate bug? (see desktop picture)
     */
}