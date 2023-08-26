package com.michaeljohare.utils;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class GUILookAndFeel {
    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.out.println("Error setting LookAndFeel: " + ex.getMessage());
            if (ex.getCause() != null) {
                System.out.println("Caused by: " + ex.getCause().getMessage());
            }
            ex.printStackTrace();
        }
    }
}
