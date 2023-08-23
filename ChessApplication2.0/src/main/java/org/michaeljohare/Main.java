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
     *  Start in debug, random NPE when stockfish tried to castle (extremely rare, can't reproduce)
     *  Implement 50 move rule/insufficient material stalemate
     *  Refactor and clean up
     *      - Check unused methods (Player, GameState, etc)
     *      - Clean up ChessBoardPanel
     *      - Clean up StockfishController (split setup/util methods into separate StockfishSetup class in utils)
     */
}