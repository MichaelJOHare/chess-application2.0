package org.michaeljohare.view;


import org.michaeljohare.controller.ChessController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.board.Square;
import org.michaeljohare.model.moves.Move;
import org.michaeljohare.model.pieces.PieceType;
import org.michaeljohare.model.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    private JButton[][] chessButtons;
    JPanel chessBoardPanel;
    private ChessBoard board;
    private ChessController controller;
    private final List<Square> highlightedSquares = new ArrayList<>();
    private static final Color LIGHT_SQUARE = new Color(248, 240, 198);
    private static final Color DARK_SQUARE = new Color(156, 98, 69);

    public ChessBoardPanel(ChessBoard board) {
        this.board = board;
        chessBoardPanel = new JPanel();
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        add(createChessboardPanel(), BorderLayout.CENTER);
    }

    private JPanel createChessboardPanel() {
        chessBoardPanel.setLayout(new GridLayout(8, 8));
        chessButtons = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessButtons[row][col] = new JButton();
                chessButtons[row][col].setLayout(new BorderLayout());

                getSquareColor(row, col);

                updateButton(row, col);
                final int finalRow = row;
                final int finalCol = col;
                chessButtons[row][col].addActionListener(e -> onSquareClick(finalRow, finalCol));

                createButtonLabels(row, col, chessButtons);

                chessBoardPanel.add(chessButtons[row][col]);
            }
        }
        return chessBoardPanel;
    }

    private void createButtonLabels(int row, int col, JButton[][] chessButtons) {
        if (row == 7) {
            JLabel letterLabel = new JLabel("abcdefgh".substring(col, col + 1));
            letterLabel.setFont(new Font("Roboto", Font.BOLD, 16));
            if (col % 2 == 0) {
                letterLabel.setForeground(LIGHT_SQUARE);
            } else {
                letterLabel.setForeground(DARK_SQUARE);
            }
            int leftPadding = 5;
            letterLabel.setBorder(BorderFactory.createEmptyBorder(0, leftPadding, 0, 0));
            chessButtons[row][col].add(letterLabel, BorderLayout.SOUTH);
        }

        if (col == 7) {
            JLabel numberLabel = new JLabel("87654321".substring(row, row + 1), SwingConstants.RIGHT);
            numberLabel.setFont(new Font("Roboto", Font.BOLD, 16));
            if (row % 2 == 1) {
                numberLabel.setForeground(DARK_SQUARE);
            } else {
                numberLabel.setForeground(LIGHT_SQUARE);
            }
            int rightPadding = 5;
            numberLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, rightPadding));
            chessButtons[row][col].add(numberLabel, BorderLayout.NORTH);
        }
    }

    private void getSquareColor(int row, int col) {
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
            chessButtons[move.getEndSquare().getRow()][move.getEndSquare().getCol()].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
            highlightedSquares.add(move.getEndSquare());
        }
    }

    public void setHighlightedSquaresStockfish(Move move) {
        Square startSquare = move.getStartSquare();
        Square endSquare = move.getEndSquare();
        chessButtons[startSquare.getRow()][startSquare.getCol()].setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
        chessButtons[endSquare.getRow()][endSquare.getCol()].setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
        highlightedSquares.add(startSquare);
        highlightedSquares.add(endSquare);
    }

    public void clearHighlightedSquares() {
        for (Square square : highlightedSquares) {
            chessButtons[square.getRow()][square.getCol()].setBorder(null);
        }
        highlightedSquares.clear();
    }
    public void setController(ChessController controller) {
        this.controller = controller;
    }

    public JButton[][] getChessButtons() {
        return chessButtons;
    }

    private void onSquareClick(int row, int col) {
        controller.onSquareClick(row, col);
    }
}
