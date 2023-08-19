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
     *  QOL: implement check for clicking on piece you own on second click so that it can be selected instead of invalid square notification
     *  Refactor and clean up
     *      - Maybe separate out some things in GameController to other classes (state pattern maybe?)
     *      - Check unused methods (Player, GameState, etc)
     *      - Clean up ChessBoardPanel
     */
}