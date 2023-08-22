package org.michaeljohare.view;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.model.board.ChessBoard;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.pieces.PieceType;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;

public class ChessGUI extends JFrame {

    private ChessBoardPanel chessBoardPanel;
    private GameLogPanel gameLogPanel;
    private GUIController guiController;


    public ChessGUI(ChessBoard board) {
        super("Chess");

        chessBoardPanel = new ChessBoardPanel(board);
        gameLogPanel = new GameLogPanel();

        initializeGUI();
    }

    private void initializeGUI() {
        updateFrame(this);

        setSize(1250, 1000);
        setLayout(new BorderLayout());

        add(chessBoardPanel, BorderLayout.CENTER);
        add(gameLogPanel, BorderLayout.EAST);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (guiController != null) {
                    guiController.onWindowClosing();
                }
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void showGUI() {
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

    public PieceType createPromotionPane(ChessPiece playerPiece, JButton[][] chessButtons) {
        PieceType[] options = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
        String[] optionNames = Arrays.stream(options)
                .map(PieceType::name)
                .map(String::toLowerCase)
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .toArray(String[]::new);

        int selectedIndex = JOptionPane.showOptionDialog(
                chessButtons[playerPiece.getCurrentSquare().getRow()][playerPiece.getCurrentSquare().getCol()],
                "Select a piece to promote the pawn to:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                optionNames,
                optionNames[0]
        );

        // If the user closes the pane, automatically select the queen.
        if (selectedIndex == -1) {
            selectedIndex = 0;
        }

        return options[selectedIndex];
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

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }
}
