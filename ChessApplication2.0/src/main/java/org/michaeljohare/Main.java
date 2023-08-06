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
     *  Play Again not open new window
     *  Clicking square with no piece > NPE
     *  Clicking square of opponent piece should prompt about
     *  EN PASSANT LIVES RENT FREE IN MY HEAD
     *  Test undo castling
     *  Test and debug en passant + undoing en passant
     */
}