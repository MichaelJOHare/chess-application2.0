package org.michaeljohare.view;

import javax.swing.*;
import java.awt.*;

public class ChessButton extends JButton {

    public enum HighlightMode {
        DOT, CORNERS, NONE
    }

    private HighlightMode mode = HighlightMode.NONE;
    private Color highlightColor;
    private final int row;
    private final int col;

    public ChessButton(int row, int col) {
        super();
        this. row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setHighlightMode(HighlightMode mode, Color color) {
        this.mode = mode;
        this.highlightColor = color;
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
        g.setColor(highlightColor);
        g.fillOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
    }

    private void paintCorners(Graphics g) {
        g.setColor(highlightColor);
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
