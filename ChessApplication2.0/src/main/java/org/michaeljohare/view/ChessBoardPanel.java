package org.michaeljohare.view;


import org.michaeljohare.controller.GUIController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.pieces.PieceType;
import org.michaeljohare.model.player.Player;
import org.michaeljohare.utils.ChessButton;
import org.michaeljohare.utils.ChessMouseListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    private static final Color LIGHT_SQUARE = new Color(248, 240, 198);
    private static final Color DARK_SQUARE = new Color(156, 98, 69);
    private static final Color LIGHT_SQUARE_PREVIOUS_MOVE = new Color(205,210,106,255);
    private static final Color DARK_SQUARE_PREVIOUS_MOVE = new Color(170,162,58,255);

    private final JPanel chessBoardPanel;
    private final ChessBoard board;
    private final List<Square> highlightedSquares = new ArrayList<>();
    private final List<Square> previousMoveHighlightedSquares = new ArrayList<>();
    private ChessButton[][] chessButtons;
    private GUIController guiController;
    private boolean boardFlipped = false;

    public ChessBoardPanel(ChessBoard board) {
        this.board = board;
        chessBoardPanel = new JPanel();
    }

    public void init(GUIController guiController) {
        this.guiController = guiController;
        setLayout(new BorderLayout());
        add(createChessboardPanel(), BorderLayout.CENTER);
    }

    private JPanel createChessboardPanel() {
        chessBoardPanel.setLayout(new GridLayout(8, 8));

        int startRow = boardFlipped ? 7 : 0;
        int endRow = boardFlipped ? -1 : 8;
        int rowIncrement = boardFlipped ? -1 : 1;

        int startCol = boardFlipped ? 7 : 0;
        int endCol = boardFlipped ? -1 : 8;
        int colIncrement = boardFlipped ? -1 : 1;

        chessButtons = new ChessButton[8][8];
        for (int row = startRow; row != endRow; row += rowIncrement) {
            for (int col = startCol; col != endCol; col += colIncrement) {
                chessButtons[row][col] = new ChessButton();
                chessButtons[row][col].setLayout(new BorderLayout());

                setSquareColor(row, col);

                updateButton(row, col);
                chessButtons[row][col].setName(row + "," + col);

                ChessMouseListener listener = new ChessMouseListener(guiController, this);
                chessButtons[row][col].addMouseListener(listener);
                chessButtons[row][col].addMouseMotionListener(listener);

                createButtonLabels(row, col, chessButtons);

                chessBoardPanel.add(chessButtons[row][col]);
            }
        }
        return chessBoardPanel;
    }

    private void createButtonLabels(int row, int col, JButton[][] chessButtons) {

        // Letter labels for files a-h
        if ((boardFlipped && row == 0) || (!boardFlipped && row == 7)) {
            JLabel letterLabel = new JLabel("abcdefgh".substring(col, col + 1));
            letterLabel.setFont(new Font("Roboto", Font.BOLD, 16));
            if (col % 2 == 0) {
                if (boardFlipped) {
                    letterLabel.setForeground(DARK_SQUARE);
                } else {
                    letterLabel.setForeground(LIGHT_SQUARE);
                }
            } else {
                if (boardFlipped) {
                    letterLabel.setForeground(LIGHT_SQUARE);
                } else {
                    letterLabel.setForeground(DARK_SQUARE);
                }
            }
            int leftPadding = 5;
            letterLabel.setBorder(BorderFactory.createEmptyBorder(0, leftPadding, 0, 0));
            chessButtons[row][col].add(letterLabel, BorderLayout.SOUTH);
        }

        // Number labels for ranks 1-8
        if ((boardFlipped && col == 0) || (!boardFlipped && col == 7)) {
            JLabel numberLabel = new JLabel("87654321".substring(row, row + 1), SwingConstants.RIGHT);
            numberLabel.setFont(new Font("Roboto", Font.BOLD, 16));
            if (row % 2 == 1) {
                if (boardFlipped) {
                    numberLabel.setForeground(LIGHT_SQUARE);
                } else {
                    numberLabel.setForeground(DARK_SQUARE);
                }
            } else {
                if (boardFlipped) {
                    numberLabel.setForeground(DARK_SQUARE);
                } else {
                    numberLabel.setForeground(LIGHT_SQUARE);
                }
            }
            int rightPadding = 5;
            numberLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, rightPadding));
            chessButtons[row][col].add(numberLabel, BorderLayout.NORTH);
        }
    }

    private void setSquareColor(int row, int col) {
        if (row % 2 == 0) {
            if (col % 2 == 0) {
                chessButtons[row][col].setBackground(LIGHT_SQUARE);
            } else {
                chessButtons[row][col].setBackground(DARK_SQUARE);
            }
        } else {
            if (col % 2 == 1) {
                chessButtons[row][col].setBackground(LIGHT_SQUARE);
            } else {
                chessButtons[row][col].setBackground(DARK_SQUARE);
            }
        }
    }

    public void updateButton(int row, int col) {
        chessButtons[row][col].setBorder(null);
        if (board.getPieceAt(row, col) != null) {
            String imagePath = getImagePath(board.getPieceAt(row, col).getType(), board.getPieceAt(row, col).getPlayer());
            try {
                Image pieceImage = ImageIO.read(ChessGUI.class.getResource(imagePath));
                chessButtons[row][col].setIcon(new ImageIcon(pieceImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            chessButtons[row][col].setIcon(null);
        }
    }

    private String getImagePath(PieceType type, Player player) {
        return "/" + (player.isWhite() ? "White_" : "Black_") + type + ".png";
    }

    public void setHighlightedSquares(List<Move> moves) {
        for (Move move : moves) {
            if (!board.isOccupiedByOpponent(move.getEndSquare().getRow(), move.getEndSquare().getCol(), move.getPiece().getPlayer())) {
                chessButtons[move.getEndSquare().getRow()][move.getEndSquare().getCol()].setHighlightMode(ChessButton.HighlightMode.DOT);
            } else {
                chessButtons[move.getEndSquare().getRow()][move.getEndSquare().getCol()].setHighlightMode(ChessButton.HighlightMode.CORNERS);
            }
            highlightedSquares.add(move.getEndSquare());
        }
    }

    public void setHighlightedSquaresStockfish(Move move) {
        Square startSquare = move.getStartSquare();
        Square endSquare = move.getEndSquare();

        chessButtons[startSquare.getRow()][startSquare.getCol()].setHighlightMode(ChessButton.HighlightMode.CORNERS);
        chessButtons[endSquare.getRow()][endSquare.getCol()].setHighlightMode(ChessButton.HighlightMode.CORNERS);

        highlightedSquares.add(startSquare);
        highlightedSquares.add(endSquare);
    }

    public void setHighlightedSquaresPreviousMove(Move move) {
        if (move == null) {
            return;
        }

        Square[] squares = {move.getStartSquare(), move.getEndSquare()};

        for (Square square : squares) {
            JButton chessButton = chessButtons[square.getRow()][square.getCol()];
            Color currentColor = chessButton.getBackground();

            if (currentColor.equals(DARK_SQUARE)) {
                chessButton.setBackground(DARK_SQUARE_PREVIOUS_MOVE);
            } else if (currentColor.equals(LIGHT_SQUARE)) {
                chessButton.setBackground(LIGHT_SQUARE_PREVIOUS_MOVE);
            }

            previousMoveHighlightedSquares.add(square);
        }
    }

    public void clearHighlightedSquares() {
        for (Square square : highlightedSquares) {
            chessButtons[square.getRow()][square.getCol()].setHighlightMode(ChessButton.HighlightMode.NONE);
        }
        highlightedSquares.clear();
    }

    public void clearPreviousMoveHighlightedSquares() {
        for (Square square : previousMoveHighlightedSquares) {
            if (chessButtons[square.getRow()][square.getCol()].getBackground().equals(DARK_SQUARE_PREVIOUS_MOVE)) {
                chessButtons[square.getRow()][square.getCol()].setBackground(DARK_SQUARE);
            } else if (chessButtons[square.getRow()][square.getCol()].getBackground().equals(LIGHT_SQUARE_PREVIOUS_MOVE)) {
                chessButtons[square.getRow()][square.getCol()].setBackground(LIGHT_SQUARE);
            }
        }
    }

    public JButton[][] getChessButtons() {
        return chessButtons;
    }

    public void flipBoard() {
        boardFlipped = !boardFlipped;
        chessBoardPanel.removeAll();
        createChessboardPanel();
        chessBoardPanel.revalidate();
        chessBoardPanel.repaint();
    }

}
