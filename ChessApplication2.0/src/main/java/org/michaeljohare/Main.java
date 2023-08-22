package org.michaeljohare;

import org.michaeljohare.controller.GameController;
import org.michaeljohare.utils.GUILookAndFeel;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.setProperty( "apple.awt.application.appearance", "system" );
        GUILookAndFeel.setLookAndFeel();

        SwingUtilities.invokeLater(GameController::new);

    }

    /*
     * TODO
     *  Implement 50 move rule
     *  Refactor and clean up
     *      - Check unused methods (Player, GameState, etc)
     *      - Clean up ChessBoardPanel
     *      - Clean up MoveHandler (no need for updateGUI() when finalizing drag and drop move)
     */
}