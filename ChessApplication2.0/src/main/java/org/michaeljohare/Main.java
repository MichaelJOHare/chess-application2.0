package org.michaeljohare;

import org.michaeljohare.model.game.GameManager;
import org.michaeljohare.utils.GUILookAndFeel;


import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.setProperty( "apple.awt.application.appearance", "system" );
        GUILookAndFeel.setLookAndFeel();

        SwingUtilities.invokeLater(GameManager::new);

    }

    /*
     * TODO
     *  START IN DEBUG (Very rare NPE.. endgame, maybe after pawn promotion and/or undo?)
     *          - Can't figure out how to reproduce, might be fixed? Suspect piece.copy() in PawnMovementStrategy and/or resetting firstClick in GameManager might've fixed it
     *  Implement 50 move rule
     *  QOL: implement check for clicking on piece you own on second click so that it can be selected instead of invalid square notification
     *  Refactor and clean up
     *      + Maybe separate out some things in GameManager to other classes
     *          - clear previous move highlighted squares on play again
     */
}