package com.michaeljohare.view;


import com.michaeljohare.controller.GUIController;
import com.michaeljohare.model.board.ChessBoard;
import com.michaeljohare.model.board.Square;
import com.michaeljohare.model.moves.EnPassantMove;
import com.michaeljohare.model.moves.Move;
import com.michaeljohare.model.pieces.PieceType;
import com.michaeljohare.model.player.Player;
import com.michaeljohare.utils.ChessMouseHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPanel extends JPanel {
    private static final Color LIGHT_SQUARE = new Color(248, 240, 198);
    private static final Color DARK_SQUARE = new Color(156, 98, 69);
    private static final Color LIGHT_SQUARE_PREVIOUS_MOVE = new Color(205,210,106,255);
    private static final Color DARK_SQUARE_PREVIOUS_MOVE = new Color(170,162,58,255);
    private static final Color LIGHT_SQUARE_HIGHLIGHT_COLOR = new Color(127, 158, 92);
    private static final Color DARK_SQUARE_HIGHLIGHT_COLOR = new Color(123, 138, 50);
    private static final Color LIGHT_SQUARE_STOCKFISH_HIGHLIGHT = new Color(128, 67, 168);
    private static final Color DARK_SQUARE_STOCKFISH_HIGHLIGHT = new Color(115, 65, 163);

    private final ChessBoard board;
    private final List<Square> highlightedSquares = new ArrayList<>();
    private final List<Square> previousMoveHighlightedSquares = new ArrayList<>();
    private ChessButton[][] chessButtons;
    private GUIController guiController;
    private boolean boardFlipped = false;

    public ChessBoardPanel(ChessBoard board) {
        this.board = board;
        setLayout(new GridLayout(8, 8));
    }

    public void init(GUIController guiController) {
        this.guiController = guiController;
        addChessButtons();
    }

    private void addChessButtons() {
        ChessMouseHandler handler = new ChessMouseHandler(guiController, this);

        int startRow = boardFlipped ? 7 : 0;
        int endRow = boardFlipped ? -1 : 8;
        int rowIncrement = boardFlipped ? -1 : 1;

        int startCol = boardFlipped ? 7 : 0;
        int endCol = boardFlipped ? -1 : 8;
        int colIncrement = boardFlipped ? -1 : 1;

        chessButtons = new ChessButton[8][8];
        for (int row = startRow; row != endRow; row += rowIncrement) {
            for (int col = startCol; col != endCol; col += colIncrement) {
                chessButtons[row][col] = new ChessButton(row, col);
                chessButtons[row][col].setLayout(new BorderLayout());

                setSquareColor(row, col);

                updateButton(row, col);

                ChessMouseHandler.ChessButtonMouseListener chessButtonMouseListener = handler.getButtonMouseListener();
                chessButtons[row][col].addMouseListener(chessButtonMouseListener);
                chessButtons[row][col].addMouseMotionListener(chessButtonMouseListener);

                createButtonLabels(row, col, chessButtons);

                this.add(chessButtons[row][col]);
            }
        }
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
            URL imageUrl = ChessBoardPanel.class.getResource(imagePath);
            if (imageUrl != null) {
                try {
                    Image pieceImage = ImageIO.read(imageUrl);
                    chessButtons[row][col].setIcon(new ImageIcon(pieceImage));
                } catch (IOException e) {
                    guiController.imageAccessError();
                }
            } else {
                guiController.imageAccessError();
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
            setSquareHighlight(move.getEndSquare(), ChessButton.HighlightMode.DOT, move.getPiece().getPlayer(), false);
            if (move instanceof EnPassantMove) {
                setSquareHighlight(move.getEndSquare(), ChessButton.HighlightMode.CORNERS, move.getPiece().getPlayer(), false);
            }
            highlightedSquares.add(move.getEndSquare());
        }
    }

    public void setHighlightedSquaresStockfish(Move move) {
        setSquareHighlight(move.getStartSquare(), ChessButton.HighlightMode.CORNERS, null, true);
        setSquareHighlight(move.getEndSquare(), ChessButton.HighlightMode.CORNERS, null, true);
        highlightedSquares.add(move.getStartSquare());
        highlightedSquares.add(move.getEndSquare());
    }

    private void setSquareHighlight(Square square, ChessButton.HighlightMode mode, Player player, boolean isStockfishMove) {
        int row = square.getRow();
        int col = square.getCol();
        Color squareColor = chessButtons[row][col].getBackground();
        ChessButton.HighlightMode highlightMode = mode;
        Color highlightColor;

        if (squareColor.equals(LIGHT_SQUARE) || squareColor.equals(LIGHT_SQUARE_PREVIOUS_MOVE)) {
            if (isStockfishMove) {
                highlightColor = LIGHT_SQUARE_STOCKFISH_HIGHLIGHT;
            } else {
                highlightColor = LIGHT_SQUARE_HIGHLIGHT_COLOR;
            }
        } else if (squareColor.equals(DARK_SQUARE) || squareColor.equals(DARK_SQUARE_PREVIOUS_MOVE)) {
            if (isStockfishMove) {
                highlightColor = DARK_SQUARE_STOCKFISH_HIGHLIGHT;
            } else {
                highlightColor = DARK_SQUARE_HIGHLIGHT_COLOR;
            }
        } else {
            return;
        }

        // If the square is occupied by the opponent, change mode to CORNERS
        if (board.isOccupiedByOpponent(row, col, player) && mode == ChessButton.HighlightMode.DOT) {
            highlightMode = ChessButton.HighlightMode.CORNERS;
        }

        chessButtons[row][col].setHighlightMode(highlightMode, highlightColor);
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
            chessButtons[square.getRow()][square.getCol()].setHighlightMode(ChessButton.HighlightMode.NONE, null);
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

    public ChessButton getChessButtonAt(int row, int col) {
        return chessButtons[row][col];
    }

    public void flipBoard() {
        boardFlipped = !boardFlipped;
        this.removeAll();
        this.addChessButtons();
        this.revalidate();
        this.repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        Component c = getParent();
        if (c != null) {
            int s = Math.min(c.getWidth(), c.getHeight());
            return new Dimension(s, s);
        }
        return d;
    }
}
