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
     *  Implement clear/change square color on piece selection + "ghost" piece when dragging maybe?
     *  Still occasionally get drag and drop bug when piece is dragged off frame
     *  Implement 50 move rule/insufficient material stalemate
     *  Refactor and clean up
     *      - Check unused methods (Player, GameState, etc)
     *      - Clean up ChessBoardPanel
     *      - Clean up StockfishController (split setup/util methods into separate StockfishSetup class in utils)
     */
}