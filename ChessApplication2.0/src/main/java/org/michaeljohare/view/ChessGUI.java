package org.michaeljohare.view;

import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.pieces.ChessPiece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ChessGUI extends JFrame {

    private ChessBoardPanel chessBoardPanel;
    private GameLogPanel gameLogPanel;


    public ChessGUI(ChessBoard board) {
        super("Chess");

        chessBoardPanel = new ChessBoardPanel(board);
        gameLogPanel = new GameLogPanel();

        initializeGUI();
    }

    private void initializeGUI() {
        updateFrame(this);

        setSize(1200, 1000);
        setLayout(new BorderLayout());

        add(chessBoardPanel, BorderLayout.CENTER);
        add(gameLogPanel, BorderLayout.EAST);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        updateGUI();
    }

    private void updateFrame(JFrame frame) {
        try {
            Image frameIcon = ImageIO.read(ChessGUI.class.getResource("/Frame_Icon.png"));
            frame.setIconImage(frameIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int createPromotionPane(ChessPiece playerPiece, JButton[][] chessButtons) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        return JOptionPane.showOptionDialog(
                chessButtons[playerPiece.getCurrentSquare().getRow()][playerPiece.getCurrentSquare().getCol()],
                "Select a piece to promote the pawn to:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
    }

    public void updateGUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                chessBoardPanel.updateButton(row, col);
            }
        }
    }

    public ChessBoardPanel getChessBoardPanel() {
        return chessBoardPanel;
    }

    public GameLogPanel getGameLogPanel() {
        return gameLogPanel;
    }
}
