package org.michaeljohare.utils;

import java.util.Timer;
import java.util.TimerTask;

public class MemoryTracker {
    public void memoryTimer() {
        TimerTask memoryMonitor = new TimerTask() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Used memory: " + usedMemory + " bytes");
            }
        };

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(memoryMonitor, 0, 1000);
    }
}
