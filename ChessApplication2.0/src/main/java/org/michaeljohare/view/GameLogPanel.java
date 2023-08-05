package org.michaeljohare.view;

import org.michaeljohare.controller.ChessController;
import org.michaeljohare.model.pieces.ChessPiece;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameLogPanel extends JPanel {

    private JButton playAgainButton;
    private Color defaultButtonColor;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private ChessController controller;
    private final List<ChessPiece> player1CapturedPieces = new ArrayList<>();
    private final List<ChessPiece> player2CapturedPieces = new ArrayList<>();
    private JPanel rightPanel;


    public GameLogPanel() {
        rightPanel = new JPanel();
        init();
    }

    public void init() {
        logTextArea = createLogTextArea();
        logTextArea.setFont(new Font("Roboto", Font.PLAIN, 20));
        logTextArea.setText("\n\n\n Welcome to Michael's Chess Game! \n Use the undo button to undo a \n previous move. " +
                "\n\n It is White's turn to move first.");
        logScrollPane = new JScrollPane(logTextArea);

        player1CapturedArea = createCapturedArea();
        player2CapturedArea = createCapturedArea();
        updateCapturedPiecesDisplay();

        setLayout(new BorderLayout());
        add(createRightPanel(), BorderLayout.CENTER);
    }

    private JPanel createRightPanel() {
        rightPanel.setLayout(new BorderLayout());

        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Roboto", Font.BOLD, 24));
        playAgainButton.addActionListener(e -> onPlayAgainButtonClick());
        defaultButtonColor = playAgainButton.getBackground();

        JButton undoButton = new JButton("Undo");
        undoButton.setFont(new Font("Roboto", Font.BOLD, 24));
        undoButton.addActionListener(e -> onUndoButtonClick());

        JPanel playAgainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playAgainButtonPanel.add(playAgainButton);

        JPanel undoButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        undoButtonPanel.add(undoButton);

        JPanel logPanelWithButtons = new JPanel(new BorderLayout());
        logPanelWithButtons.add(logScrollPane, BorderLayout.CENTER);
        logPanelWithButtons.add(playAgainButtonPanel, BorderLayout.NORTH);
        logPanelWithButtons.add(undoButtonPanel, BorderLayout.SOUTH);

        rightPanel.add(player1CapturedArea, BorderLayout.SOUTH);
        rightPanel.add(logPanelWithButtons, BorderLayout.CENTER);
        rightPanel.add(player2CapturedArea, BorderLayout.NORTH);

        return rightPanel;
    }

    private JTextArea createLogTextArea() {
        JTextArea logTextArea = new JTextArea(5, 20);
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        logTextArea.setEditable(false);
        return logTextArea;
    }

    private JTextArea createCapturedArea() {
        JTextArea capturedArea = new JTextArea(15, 8);

        capturedArea.setEditable(false);
        capturedArea.setLayout(new FlowLayout());
        capturedArea.setLineWrap(true);
        capturedArea.setWrapStyleWord(true);

        return capturedArea;
    }

    public void updatePlayAgainButton(Color backgroundColor, Color foregroundColor) {
        playAgainButton.setBackground(backgroundColor);
        playAgainButton.setForeground(foregroundColor);
    }

    public void updateLogTextArea(String string) {
        logTextArea.setText(string);
    }

    public void updateCapturedPiecesDisplay() {
        player1CapturedArea.removeAll();
        player2CapturedArea.removeAll();

        Font capturedPieceFont = new Font("Roboto", Font.PLAIN, 26);
        Font capturedPiecesTitleFont = new Font("Roboto", Font.BOLD, 24);

        Border paddingBorder = BorderFactory.createEmptyBorder(5, 70, 5, 70);
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.gray);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

        JLabel capturedPiecesTitle1 = new JLabel("Captured Pieces");
        capturedPiecesTitle1.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle1.setBorder(compoundBorder);
        JLabel capturedPiecesTitle2 = new JLabel("Captured Pieces");
        capturedPiecesTitle2.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle2.setBorder(compoundBorder);
        player1CapturedArea.add(capturedPiecesTitle1);
        player2CapturedArea.add(capturedPiecesTitle2);

        for (ChessPiece piece : player1CapturedPieces) {
            JLabel blackCapturedPieceLabel = new JLabel(piece.getBlackChessPieceSymbol());
            blackCapturedPieceLabel.setFont(capturedPieceFont);
            player1CapturedArea.add(blackCapturedPieceLabel);
        }

        for (ChessPiece piece : player2CapturedPieces) {
            JLabel whiteCapturedPieceLabel = new JLabel(piece.getWhiteChessPieceSymbol());
            whiteCapturedPieceLabel.setFont(capturedPieceFont);
            player2CapturedArea.add(whiteCapturedPieceLabel);
        }

        player1CapturedArea.revalidate();
        player1CapturedArea.repaint();
        player2CapturedArea.revalidate();
        player2CapturedArea.repaint();
    }

    private void onPlayAgainButtonClick() {
        controller.handlePlayAgainButtonClick();
        player1CapturedPieces.clear();
        player2CapturedPieces.clear();
        playAgainButton.setBackground(defaultButtonColor);
        playAgainButton.setForeground(null);
        controller.clearHighlightedSquares();
        updateCapturedPiecesDisplay();
        //updateGUI();
    }

    public void setController(ChessController controller) {
        this.controller = controller;
    }

    private void onUndoButtonClick() {

    }
}
