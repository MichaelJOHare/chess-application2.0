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
            System.out.println("Error setting LookAndFeel: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.out.println("Caused by: " + ex.getCause().getMessage());
            }
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

        SwingUtilities.invokeLater(GameManager::new);

    }

    /*
     * TODO
     *  Implement 50 move rule
     *  Refactor and clean up
     *  Recompile and run another JFR
     */
}