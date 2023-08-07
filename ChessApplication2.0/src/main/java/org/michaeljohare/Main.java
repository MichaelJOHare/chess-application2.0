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

        GameManager gm = new GameManager();
    }

    /*
     * TODO
     *  Implement stockfish
     */
}