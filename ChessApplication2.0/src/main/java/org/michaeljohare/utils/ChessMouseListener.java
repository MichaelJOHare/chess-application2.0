package org.michaeljohare.utils;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.view.ChessBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ChessMouseListener extends MouseAdapter implements MouseMotionListener {
    private static final int DRAG_THRESHOLD = 12;

    private final GUIController guiController;
    private final ChessBoardPanel chessBoardPanel;
    private Cursor dragCursor = null;
    private Point pressedPoint;
    private Icon pieceIcon;
    private boolean wasDragged = false;
    private boolean dragInitiated = false;

    public ChessMouseListener(GUIController guiController, ChessBoardPanel chessBoardPanel) {
        this.guiController = guiController;
        this.chessBoardPanel = chessBoardPanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!(e.getSource() instanceof JButton)) return;

        JButton source = (JButton) e.getSource();
        wasDragged = false;
        dragInitiated = false;
        dragCursor = null;

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
                Point rowAndCol = getRowColFromButtonName(source);
                dragInitiated = guiController.onDragStart(rowAndCol.x, rowAndCol.y);
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
        resetCursor();
        if (!(e.getSource() instanceof JButton)) return;

        JButton source = (JButton) e.getSource();

        if (dragInitiated) {
            handleDragRelease(e, source);
        } else if (wasDragged) {
            source.setIcon(pieceIcon);
        } else {
            Point point = getRowColFromButtonName(source);
            guiController.onSquareClick(point.x, point.y);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (dragInitiated) {
            cancelDrag(e);
        }
    }

    private void handleDragRelease(MouseEvent e, JButton source) {
        Point point = e.getPoint();
        SwingUtilities.convertPointToScreen(point, e.getComponent());
        SwingUtilities.convertPointFromScreen(point, chessBoardPanel);

        int squareHeight = chessBoardPanel.getHeight() / 8;
        int squareWidth = chessBoardPanel.getWidth() / 8;
        int endRow = point.y / squareHeight;
        int endCol = point.x / squareWidth;

        boolean validMove = guiController.onDragDrop(endRow, endCol);
        if (!validMove) {
            source.setIcon(pieceIcon);
        }

        wasDragged = false;
        dragInitiated = false;
    }

    private void resetCursor() {
        if (dragCursor != null) {
            chessBoardPanel.setCursor(Cursor.getDefaultCursor());
            dragCursor = null;
        }
    }

    private void cancelDrag(MouseEvent e) {
        if (!(e.getSource() instanceof JButton)) return;

        JButton source = (JButton) e.getSource();
        source.setIcon(pieceIcon);
        resetCursor();
        dragInitiated = false;
        wasDragged = false;
    }

    private Point getRowColFromButtonName(JButton button) {
        String[] rowAndCol = button.getName().split(",");
        int row = Integer.parseInt(rowAndCol[0]);
        int col = Integer.parseInt(rowAndCol[1]);
        return new Point(row, col);
    }
}
