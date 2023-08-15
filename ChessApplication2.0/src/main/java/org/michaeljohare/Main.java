package org.michaeljohare;

import org.michaeljohare.model.game.GameManager;
import org.michaeljohare.utils.GUILookAndFeel;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        GUILookAndFeel.setLookAndFeel();

        SwingUtilities.invokeLater(GameManager::new);

    }

    /*
     * TODO
     *  START IN DEBUG (Very rare NPE.. endgame, maybe after pawn promotion?)
     *  Implement 50 move rule
     *  Refactor and clean up
     */
}