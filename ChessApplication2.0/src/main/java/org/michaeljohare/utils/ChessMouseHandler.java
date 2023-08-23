package org.michaeljohare.utils;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.view.ChessBoardPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class ChessMouseHandler {
    private static final int DRAG_THRESHOLD = 12;

    private final GUIController guiController;
    private final ChessBoardPanel chessBoardPanel;
    private Cursor dragCursor = null;
    private Point pressedPoint;
    private Icon pieceIcon;
    private boolean wasDragged = false;
    private boolean dragInitiated = false;
    private int dragStartRow = -1;
    private int dragStartCol = -1;

    public ChessMouseHandler(GUIController guiController, ChessBoardPanel chessBoardPanel) {
        this.guiController = guiController;
        this.chessBoardPanel = chessBoardPanel;
    }

    public ChessButtonMouseListener getButtonMouseListener() {
        return new ChessButtonMouseListener();
    }

    public ChessBoardMouseListener getBoardMouseListener() {
        return new ChessBoardMouseListener();
    }

    private void resetCursor() {
        if (dragCursor != null) {
            chessBoardPanel.setCursor(Cursor.getDefaultCursor());
            dragCursor = null;
        }
    }

    public class ChessButtonMouseListener extends MouseAdapter implements MouseMotionListener {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!(e.getSource() instanceof JButton)) return;

            JButton source = (JButton) e.getSource();

            // Visual point is same as logical point when board is not flipped (after user presses flip board)
            Point visualPoint = getRowColFromButtonName(source);
            dragStartRow = visualPoint.x;
            dragStartCol = visualPoint.y;

            // Logical point is conversion from visual representation to underlying logical representation of board
            // when board is flipped (since underlying logical remains the same after visual flip)
            Point logicalPoint = convertVisualPointToLogicalPoint(dragStartRow, dragStartCol);
            dragStartRow = logicalPoint.x;
            dragStartCol = logicalPoint.y;


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

            JButton originalButton = (JButton) e.getSource();
            Point mouseLocationOnBoard = SwingUtilities.convertPoint(originalButton, e.getPoint(), chessBoardPanel);

            int squareHeight = chessBoardPanel.getHeight() / 8;
            int squareWidth = chessBoardPanel.getWidth() / 8;

            int visualEndRow = mouseLocationOnBoard.y / squareHeight;
            int visualEndCol = mouseLocationOnBoard.x / squareWidth;

            Point logicalEnd = convertVisualPointToLogicalPoint(visualEndRow, visualEndCol);

            if (dragInitiated) {
                handleDragRelease(logicalEnd.x, logicalEnd.y, originalButton);
            } else if (wasDragged) {
                originalButton = getButtonFromOriginalPosition();
                if (originalButton != null) {
                    originalButton.setIcon(pieceIcon);
                }
            } else {
                guiController.onSquareClick(logicalEnd.x, logicalEnd.y);
            }
        }

        private void handleDragRelease(int endRow, int endCol, JButton source) {

            boolean validMove = guiController.onDragDrop(endRow, endCol);
            if (!validMove) {
                source.setIcon(pieceIcon);
            }

            wasDragged = false;
            dragInitiated = false;
        }

        private Point getRowColFromButtonName(JButton button) {
            String[] rowAndCol = button.getName().split(",");
            int row = Integer.parseInt(rowAndCol[0]);
            int col = Integer.parseInt(rowAndCol[1]);
            return new Point(row, col);
        }

        private JButton getButtonFromOriginalPosition() {
            if (dragStartRow >= 0 && dragStartCol >= 0 &&
                    dragStartRow < 8 && dragStartCol < 8) {
                return chessBoardPanel.getChessButtonAt(dragStartRow, dragStartCol);
            }
            return null;
        }

        private Point convertVisualPointToLogicalPoint(int row, int col) {
            if (chessBoardPanel.isBoardFlipped()) {
                return new Point(7 - row, 7 - col);
            }
            return new Point(row, col);
        }
    }

    public class ChessBoardMouseListener extends MouseAdapter {

        @Override
        public void mouseExited(MouseEvent e) {
            if (dragInitiated) {
                cancelDrag(e);
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
    }
}
