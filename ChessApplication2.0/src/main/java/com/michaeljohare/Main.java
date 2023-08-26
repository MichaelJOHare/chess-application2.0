package com.michaeljohare;

import com.michaeljohare.controller.GameController;
import com.michaeljohare.utils.GUILookAndFeel;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.setProperty( "apple.awt.application.appearance", "system" );
        GUILookAndFeel.setLookAndFeel();

        SwingUtilities.invokeLater(GameController::new);

    }

    /*
     * TODO
     *  *****Run profile and change stockfish square highlight****
     *  Start in debug, random NPE when stockfish tried to castle (extremely rare, can't reproduce)
     *  Still occasionally get drag and drop bug when piece is dragged off frame
     *  Implement 50 move rule/insufficient material stalemate
     *  Refactor and clean up
     *      - Check unused methods (Player, GameState, etc)
     *      - Clean up ChessBoardPanel
     *      - Clean up StockfishController (split setup/util methods into separate StockfishSetup class in utils)
     */
}