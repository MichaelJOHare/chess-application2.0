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
     *  Promoted pawn not giving check correctly? (Can't reproduce? Wondering if profiler/debugger causes or masks race condition?)
     *  Still occasionally get drag and drop bug when piece is dragged off frame
     *  Implement insufficient material stalemate
     *  Implement continuous analysis with Ask Stockfish button
     *  Refactor and clean up
     *      - Clean up StockfishController (split setup/util methods into separate StockfishSetup class in utils)
     *      - Clean up MoveResult (unnecessary instance variables, constructors, etc.)
     */
}