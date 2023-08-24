package org.michaeljohare.utils;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.view.ChessBoardPanel;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessMouseHandler {
    private static final int DRAG_THRESHOLD = 12;

    private final GUIController guiController;
    private final ChessBoardPanel chessBoardPanel;
    private Cursor dragCursor = null;
    private Point pressedPoint;
    private Icon pieceIcon;
    private boolean wasDragged = false;
    private boolean dragInitiated = false;
    private int visualDragStartRow = -1;
    private int visualDragStartCol = -1;
    private int dragStartRow = -1;
    private int dragStartCol = -1;

    public ChessMouseHandler(GUIController guiController, ChessBoardPanel chessBoardPanel) {
        this.guiController = guiController;
        this.chessBoardPanel = chessBoardPanel;
    }

    public ChessButtonMouseListener getButtonMouseListener() {
        return new ChessButtonMouseListener();
    }

    public AppFrameMouseListener getAppFrameMouseListener() {
        return new AppFrameMouseListener();
    }

    private void resetCursor() {
        if (dragCursor != null) {
            chessBoardPanel.setCursor(Cursor.getDefaultCursor());
            dragCursor = null;
        }
    }

    public class ChessButtonMouseListener extends MouseAdapter implements MouseInputListener {
        @Override
        public void mousePressed(MouseEvent e) {
            if (!(e.getSource() instanceof ChessButton)) return;

            // Visual point is same as logical point when board is not flipped
            ChessButton source = (ChessButton) e.getSource();
            visualDragStartRow = source.getRow();
            visualDragStartCol = source.getCol();

            // Logical point is conversion from visual representation to underlying logical representation of board
            // when board is flipped (since underlying logical board remains the same after visual flip)
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
            if (!(e.getSource() instanceof ChessButton)) return;

            ChessButton source = (ChessButton) e.getSource();
            // Check if drag was long enough to be considered a mouse drag vs a click with slight movement
            if (pressedPoint != null && e.getPoint().distance(pressedPoint) > DRAG_THRESHOLD) {
                wasDragged = true;
                source.setIcon(null);

                if (!dragInitiated) {
                    dragInitiated = guiController.onDragStart(source.getRow(), source.getCol());
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
            // Convert the release point to the chessBoardPanel's coordinate system.
            Point releasePointRelativeToChessBoard = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), chessBoardPanel);
            Component releasedComponent = chessBoardPanel.getComponentAt(releasePointRelativeToChessBoard);

            resetCursor();

            if (!(releasedComponent instanceof ChessButton)) {
                return;
            }

            ChessButton targetButton = (ChessButton) releasedComponent;
            ChessButton originalButton = getButtonFromOriginalPosition();

            int endRow = targetButton.getRow();
            int endCol = targetButton.getCol();

            if (dragInitiated) {
                handleDragRelease(endRow, endCol, originalButton);
            } else if (wasDragged) {
                if (originalButton != null) {
                    originalButton.setIcon(pieceIcon);
                }
            } else {
                guiController.onSquareClick(endRow, endCol);
            }
        }

        private void handleDragRelease(int endRow, int endCol, ChessButton originalButton) {

            boolean validMove = guiController.onDragDrop(endRow, endCol);
            if (validMove) {
                ChessButton destinationButton = chessBoardPanel.getChessButtonAt(endRow, endCol);
                destinationButton.setIcon(pieceIcon);
            } else {
                originalButton.setIcon(pieceIcon);
            }

            wasDragged = false;
            dragInitiated = false;
        }

        private ChessButton getButtonFromOriginalPosition() {
            if (visualDragStartRow >= 0 && visualDragStartCol >= 0 && visualDragStartRow < 8 && visualDragStartCol < 8) {
                return chessBoardPanel.getChessButtonAt(visualDragStartRow, visualDragStartCol);
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

    public class AppFrameMouseListener extends MouseAdapter {

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
