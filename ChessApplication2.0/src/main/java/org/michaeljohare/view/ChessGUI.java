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
import java.net.URL;
import java.util.Arrays;

public class ChessGUI extends JFrame {

    private final ChessBoardPanel chessBoardPanel;
    private final GameLogPanel gameLogPanel;
    private GUIController guiController;


    public ChessGUI(ChessBoard board) {
        super("Chess");

        chessBoardPanel = new ChessBoardPanel(board);
        gameLogPanel = new GameLogPanel();
        initializeGUI();
    }

    public void initializeGUI() {
        updateFrame(this);

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(chessBoardPanel);
        centerPanel.setPreferredSize(new Dimension(800, 800));

        add(centerPanel, BorderLayout.CENTER);
        add(gameLogPanel, BorderLayout.EAST);

        pack();

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (ChessGUI.this.guiController != null) {
                    ChessGUI.this.guiController.onWindowClosing();
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
        URL imageUrl = ChessGUI.class.getResource("/Frame_Icon.png");
        if (imageUrl != null) {
            try {
                Image frameIcon = ImageIO.read(imageUrl);
                frame.setIconImage(frameIcon);
            } catch (IOException e) {
                guiController.imageAccessError();
            }
        } else {
            guiController.imageAccessError();
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

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }

    public ChessBoardPanel getChessBoardPanel() {
        return chessBoardPanel;
    }

    public GameLogPanel getGameLogPanel() {
        return gameLogPanel;
    }

}
