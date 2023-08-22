package org.michaeljohare.utils;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.view.ChessBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ChessMouseListener extends MouseAdapter implements MouseMotionListener {
    private final GUIController guiController;
    private final ChessBoardPanel chessBoardPanel;
    private Cursor dragCursor = null;
    private Point pressedPoint;
    private Icon pieceIcon;
    private boolean wasDragged = false;
    private boolean dragInitiated = false;
    private static final int DRAG_THRESHOLD = 15;
    private static final int CLICK_THRESHOLD = 5;

    public ChessMouseListener(GUIController guiController, ChessBoardPanel chessBoardPanel) {
        this.guiController = guiController;
        this.chessBoardPanel = chessBoardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton source = (JButton) e.getSource();
        wasDragged = false;

        pieceIcon = source.getIcon();
        pressedPoint = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        JButton source = (JButton) e.getComponent();
        // Check if drag was long enough to be considered a mouse drag vs a click with slight movement
        if (pressedPoint != null && e.getPoint().distance(pressedPoint) > DRAG_THRESHOLD) {
            wasDragged = true;
            source.setIcon(null);

            if (!dragInitiated) {
                String[] rowAndCol = source.getName().split(",");
                int startRow = Integer.parseInt(rowAndCol[0]);
                int startCol = Integer.parseInt(rowAndCol[1]);
                dragInitiated = guiController.onDragStart(startRow, startCol);
            }

            if (dragCursor == null && pieceIcon != null) {
                ImageIcon icon = (ImageIcon) pieceIcon;
                Image image = icon.getImage();
                Point hotspot = new Point(icon.getIconWidth() / 2, icon.getIconHeight() / 2);
                dragCursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, "dragCursor");
                chessBoardPanel.setCursor(dragCursor);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JButton source = (JButton) e.getSource();

        if (dragInitiated && wasDragged) {
            Point point = e.getPoint();
            SwingUtilities.convertPointToScreen(point, e.getComponent());
            SwingUtilities.convertPointFromScreen(point, chessBoardPanel);

            int squareHeight = chessBoardPanel.getHeight() / 8;
            int squareWidth = chessBoardPanel.getWidth() / 8;
            int endRow = point.y / squareHeight;
            int endCol = point.x / squareWidth;

            boolean validMove = guiController.onDragDrop(endRow, endCol);
            if (!validMove) {
                // Restore the icon if the move was invalid
                source.setIcon(pieceIcon);
            }

            wasDragged = false;
            dragInitiated = false;

        } else if (pressedPoint.distance(e.getPoint()) <= CLICK_THRESHOLD) {
            String[] rowAndCol = source.getName().split(",");
            int row = Integer.parseInt(rowAndCol[0]);
            int col = Integer.parseInt(rowAndCol[1]);
            guiController.onSquareClick(row, col);
        } else if (!dragInitiated && wasDragged) {
            // Handles case where user drags opponents piece (which causes dragInitiated to be false but wasDragged to be true)
            source.setIcon(pieceIcon);
        }

        if (dragCursor != null) {
            chessBoardPanel.setCursor(Cursor.getDefaultCursor());
            dragCursor = null;
        }
    }
}
