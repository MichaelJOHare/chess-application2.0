package org.michaeljohare.utils;

import javax.swing.*;
import java.awt.*;

public class ChessButton extends JButton {

    public enum HighlightMode {
        DOT, CORNERS, NONE
    }

    private HighlightMode mode = HighlightMode.NONE;
    private static final Color DARK_SQUARE_HIGHLIGHT_COLOR = new Color(132,120,69);
    private static final Color LIGHT_SQUARE_HIGHLIGHT_COLOR = new Color(129,150,105);

    public ChessButton() {
        super();
    }

    public void setHighlightMode(HighlightMode mode) {
        this.mode = mode;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (mode) {
            case DOT:
                paintDot(g);
                break;
            case CORNERS:
                paintCorners(g);
                break;
            case NONE:
                break;
        }
    }

    private void paintDot(Graphics g) {
        int radius = 12;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        g.setColor(LIGHT_SQUARE_HIGHLIGHT_COLOR);
        g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    private void paintCorners(Graphics g) {
        g.setColor(LIGHT_SQUARE_HIGHLIGHT_COLOR);
        int diameter = 40;

        // Top Left
        g.fillOval(-diameter/2, -diameter/2, diameter, diameter);
        // Top Right
        g.fillOval(getWidth() - diameter + diameter/2, -diameter/2, diameter, diameter);
        // Bottom Left
        g.fillOval(-diameter/2, getHeight() - diameter + diameter/2, diameter, diameter);
        // Bottom Right
        g.fillOval(getWidth() - diameter + diameter/2, getHeight() - diameter + diameter/2, diameter, diameter);
    }
}
