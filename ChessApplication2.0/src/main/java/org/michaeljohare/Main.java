package org.michaeljohare;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.michaeljohare.model.game.GameManager;


import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

/*        TimerTask memoryMonitor = new TimerTask() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Used memory: " + usedMemory + " bytes");
            }
        };

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(memoryMonitor, 0, 1000);*/

        new GameManager();

    }

    /*
     * TODO
     *  Implement 50 move rule
     *  Refactor and clean up
     *  Implement stockfish as player
     *  Implement letting player choose name/opponent
     */
}