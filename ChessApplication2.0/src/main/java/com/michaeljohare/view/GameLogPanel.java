package com.michaeljohare.view;

import com.michaeljohare.controller.GUIController;
import com.michaeljohare.model.pieces.ChessPiece;
import com.michaeljohare.model.player.Player;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GameLogPanel extends JPanel {

    private JButton playAgainButton;
    private JButton askStockfishButton;
    private Color defaultButtonColor;
    private JTextPane logTextPane;
    private JScrollPane logScrollPane;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private GUIController guiController;
    private final JPanel rightPanel;


    public GameLogPanel() {
        rightPanel = new JPanel();
        init();
    }

    public void init() {
        logTextPane = createLogTextPane();
        logTextPane.setFont(new Font("Roboto", Font.PLAIN, 20));
        logTextPane.setOpaque(false);
        logTextPane.setPreferredSize(new Dimension(394, 150));

        StyledDocument doc = logTextPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        logTextPane.setText("Welcome to Michael's Chess Game! Use the undo button to undo a previous move. It is White's turn to move first.");

        logScrollPane = new JScrollPane(logTextPane);

        player1CapturedArea = createCapturedArea();
        player2CapturedArea = createCapturedArea();
        setEmptyCapturedPiecesDisplay();

        setLayout(new BorderLayout());
        add(createRightPanel(), BorderLayout.CENTER);
    }

    public void setGuiController(GUIController guiController) {
        this.guiController = guiController;
    }

    private JPanel createRightPanel() {
        rightPanel.setLayout(new BorderLayout());

        playAgainButton = createStyledButton("Play Again", e -> onPlayAgainButtonClick());
        defaultButtonColor = playAgainButton.getBackground();

        JButton undoButton = createStyledButton("Undo", e -> onUndoButtonClick());
        askStockfishButton = createStyledButton("Ask Stockfish", e -> onAskStockfishButtonClick());
        JButton flipBoardButton = createStyledButton("Flip Board", e -> onFlipBoardButtonClick());

        JPanel undoAndAskStockfishPanel = createButtonGroupPanel(askStockfishButton, undoButton);
        JPanel playAgainAndFlipBoardPanel = createButtonGroupPanel(flipBoardButton, playAgainButton);

        JPanel logPanelWithButtons = new JPanel(new BorderLayout());
        logPanelWithButtons.add(logScrollPane, BorderLayout.CENTER);
        logPanelWithButtons.add(playAgainAndFlipBoardPanel, BorderLayout.NORTH);
        logPanelWithButtons.add(undoAndAskStockfishPanel, BorderLayout.SOUTH);

        rightPanel.add(player1CapturedArea, BorderLayout.SOUTH);
        rightPanel.add(logPanelWithButtons, BorderLayout.CENTER);
        rightPanel.add(player2CapturedArea, BorderLayout.NORTH);

        return rightPanel;
    }

    private JButton createStyledButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Roboto", Font.BOLD, 24));
        button.addActionListener(action);
        return button;
    }

    private JPanel createButtonPanel(JButton... buttons) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JButton button : buttons) {
            buttonPanel.add(button);
        }
        return buttonPanel;
    }

    private JPanel createButtonGroupPanel(JButton button1, JButton button2) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createButtonPanel(button1), BorderLayout.WEST);
        panel.add(createButtonPanel(button2), BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));
        return panel;
    }

    private JTextPane createLogTextPane() {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setFocusable(false);
        return pane;
    }

    private JTextArea createCapturedArea() {
        JTextArea capturedArea = new JTextArea(15, 8);

        capturedArea.setEditable(false);
        capturedArea.setFocusable(false);
        capturedArea.setLayout(new FlowLayout());
        capturedArea.setLineWrap(true);
        capturedArea.setWrapStyleWord(true);

        return capturedArea;
    }

    private void addCapturedPiecesTitle(JTextArea capturedArea) {
        Font capturedPiecesTitleFont = new Font("Roboto", Font.BOLD, 24);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 90, 5, 90);
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.gray);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

        JLabel capturedPiecesTitle = new JLabel("Captured Pieces");
        capturedPiecesTitle.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle.setBorder(compoundBorder);
        capturedArea.add(capturedPiecesTitle);
    }

    public void updateCapturedPiecesDisplay(List<ChessPiece> player1CapturedPieces, List<ChessPiece> player2CapturedPieces) {
        updatePlayerCapturedArea(player1CapturedArea, player2CapturedPieces, ChessPiece::getBlackChessPieceSymbol);
        updatePlayerCapturedArea(player2CapturedArea, player1CapturedPieces, ChessPiece::getWhiteChessPieceSymbol);
    }

    private void updatePlayerCapturedArea(JTextArea capturedArea, List<ChessPiece> capturedPieces, Function<ChessPiece, String> pieceSymbolExtractor) {
        capturedArea.removeAll();
        addCapturedPiecesTitle(capturedArea);
        Font capturedPieceFont = new Font("Roboto", Font.PLAIN, 48);

        for (ChessPiece piece : capturedPieces) {
            JLabel capturedPieceLabel = new JLabel(pieceSymbolExtractor.apply(piece));
            capturedPieceLabel.setFont(capturedPieceFont);
            capturedArea.add(capturedPieceLabel);
        }

        capturedArea.revalidate();
        capturedArea.repaint();
    }

    public void setEmptyCapturedPiecesDisplay() {
        updateCapturedPiecesDisplay(new ArrayList<>(), new ArrayList<>());
    }

    public void updatePlayAgainButton(Color backgroundColor, Color foregroundColor) {
        playAgainButton.setBackground(backgroundColor);
        playAgainButton.setForeground(foregroundColor);
    }

    public void currentPlayersTurnLogText(Player currentPlayer) {
        String name = currentPlayer.getName();
        String pieceColor = currentPlayer.getColor().toString();
        String pieceColorFormatted = pieceColor.charAt(0) + pieceColor.substring(1).toLowerCase();

        logTextPane.setText("It is " + name + "'s turn! (" + pieceColorFormatted + " pieces).");
    }

    public void stockfishWaitingButtonText() {
        askStockfishButton.setText("Waiting...");
    }

    public void stockfishThinkingButtonText() {
        askStockfishButton.setText("Thinking...");
    }

    public void stockfishGameOverButtonText(){askStockfishButton.setText("Game Over");}

    public void resetStockfishButtonText() {
        askStockfishButton.setText("Ask Stockfish");
    }

    public void noLegalMoveLogText() {
        logTextPane.setText("The piece you selected does not have any legal moves");
    }

    public void moveIsNotLegalLogText() {
        logTextPane.setText("The square you chose to move to is not a legal move, choose a " +
                "piece and try again.");
    }

    public void invalidPieceSelectionLogText() {
        logTextPane.setText("The piece you selected was invalid try again.");
    }

    public void nothingLeftToUndoLogText() {
        logTextPane.setText("There are no previous moves left to undo!");
    }

    public void checkLogText() {
        logTextPane.setText("Check!");
    }

    public void checkmateLogText() {
        logTextPane.setText("Checkmate!");
    }

    public void stalemateLogText() {
        logTextPane.setText("Stalemate!");
    }

    public void imageAccessError(){logTextPane.setText("Unable to find images resources.");}

    private void onPlayAgainButtonClick() {
        guiController.handlePlayAgainButtonClick();
        playAgainButton.setBackground(defaultButtonColor);
        playAgainButton.setForeground(null);
        resetStockfishButtonText();
        guiController.clearHighlightedSquares();
        setEmptyCapturedPiecesDisplay();
        guiController.updateGUI();
    }

    private void onUndoButtonClick() {
        guiController.handleUndoButtonClick();
        if (playAgainButton.getForeground() != null) {
            playAgainButton.setForeground(null);
            playAgainButton.setBackground(defaultButtonColor);
            resetStockfishButtonText();
        }
    }

    private void onAskStockfishButtonClick() {
        guiController.handleAskStockfishButtonClick();
    }

    private void onFlipBoardButtonClick() {
        guiController.onUserRequestFlipBoard();
    }
}
