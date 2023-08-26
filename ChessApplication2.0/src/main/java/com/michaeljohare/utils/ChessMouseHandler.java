package com.michaeljohare.utils;

import com.michaeljohare.controller.GUIController;
import com.michaeljohare.model.moves.MoveResult;
import com.michaeljohare.view.ChessBoardPanel;
import com.michaeljohare.view.ChessButton;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessMouseHandler {
    private static final int DRAG_THRESHOLD = 12;
    private static final int BOARD_SIZE = 8;

    private final GUIController guiController;
    private final ChessBoardPanel chessBoardPanel;
    private Cursor dragCursor = null;
    private Point pressedPoint;
    private Icon pieceIcon;
    private boolean wasDragged = false;
    private boolean dragInitiated = false;
    private int visualDragStartRow = -1;
    private int visualDragStartCol = -1;

    public ChessMouseHandler(GUIController guiController, ChessBoardPanel chessBoardPanel) {
        this.guiController = guiController;
        this.chessBoardPanel = chessBoardPanel;
    }

    public ChessButtonMouseListener getButtonMouseListener() {
        return new ChessButtonMouseListener();
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
            guiController.clearHighlightedSquares();

            ChessButton source = (ChessButton) e.getSource();
            visualDragStartRow = source.getRow();
            visualDragStartCol = source.getCol();


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

            ChessButton originalButton = getButtonFromOriginalPosition();
            resetCursor();

            if (!(releasedComponent instanceof ChessButton)) {
                if (originalButton != null) {
                    originalButton.setIcon(pieceIcon);
                }
                return;
            }

            ChessButton targetButton = (ChessButton) releasedComponent;

            int endRow = targetButton.getRow();
            int endCol = targetButton.getCol();

            if (dragInitiated) {
                handleDragRelease(endRow, endCol, originalButton);
            } else if (wasDragged && originalButton != null) {
                originalButton.setIcon(pieceIcon);
            } else {
                guiController.onSquareClick(endRow, endCol);
            }
        }

        private void handleDragRelease(int endRow, int endCol, ChessButton originalButton) {

            MoveResult result = guiController.onDragDrop(endRow, endCol);
            ChessButton destinationButton = chessBoardPanel.getChessButtonAt(endRow, endCol);

            if (result.getMoveType() == MoveResult.MoveType.NORMAL) {
                destinationButton.setIcon(pieceIcon);

                if (result.isPromotion()) {
                    chessBoardPanel.updateButton(endRow, endCol);
                }

            } else if (result.getMoveType() == MoveResult.MoveType.INVALID) {
                originalButton.setIcon(pieceIcon);
            }

            wasDragged = false;
            dragInitiated = false;
        }

        private ChessButton getButtonFromOriginalPosition() {
            if (visualDragStartRow >= 0 && visualDragStartCol >= 0 && visualDragStartRow < BOARD_SIZE && visualDragStartCol < BOARD_SIZE) {
                return chessBoardPanel.getChessButtonAt(visualDragStartRow, visualDragStartCol);
            }
            return null;
        }
    }
}
