package org.michaeljohare.view;

import org.michaeljohare.controller.GUIController;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GameLogPanel extends JPanel {

    private JButton playAgainButton;
    private JButton askStockfishButton;
    private Color defaultButtonColor;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private GUIController guiController;
    private final JPanel rightPanel;
    private final String lineBreaks = "\n\n\n\n\n";


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
        Font capturedPieceFont = new Font("Roboto", Font.PLAIN, 26);

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

        logTextArea.setText(lineBreaks + "    It is " + name + "'s turn! (" + pieceColorFormatted + " pieces).");
    }

    public void stockfishWaitingButtonText() {
        askStockfishButton.setText("Waiting...");
    }

    public void stockfishThinkingButtonText() {
        askStockfishButton.setText("Thinking...");
    }

    public void resetStockfishButtonText() {
        askStockfishButton.setText("Ask Stockfish");
    }

    public void noLegalMoveLogText() {
        logTextArea.setText(lineBreaks + " The piece you selected does not\n have any legal moves");
    }

    public void moveIsNotLegalLogText() {
        logTextArea.setText(lineBreaks + " The square you chose to move to is\n not a legal move, choose a " +
                "piece\n and try again.");
    }

    public void invalidPieceSelectionLogText() {
        logTextArea.setText(lineBreaks + " The piece you selected was invalid\n try again.");
    }

    public void nothingLeftToUndoLogText() {
        logTextArea.setText(lineBreaks + " There are no previous moves left\n to undo!");
    }

    public void checkLogText() {
        logTextArea.setText(lineBreaks + " \t Check!");
    }

    public void checkmateLogText() {
        logTextArea.setText(lineBreaks + "\tCheckmate!");
    }

    public void stalemateLogText() {
        logTextArea.setText(lineBreaks + "\tStalemate!");
    }

    private void onPlayAgainButtonClick() {
        guiController.handlePlayAgainButtonClick();
        playAgainButton.setBackground(defaultButtonColor);
        playAgainButton.setForeground(null);
        guiController.clearHighlightedSquares();
        setEmptyCapturedPiecesDisplay();
        guiController.updateGUI();
    }

    private void onUndoButtonClick() {
        guiController.handleUndoButtonClick();
        if (playAgainButton.getForeground() != null) {
            playAgainButton.setForeground(null);
            playAgainButton.setBackground(defaultButtonColor);
        }
    }

    private void onAskStockfishButtonClick() {
        guiController.handleAskStockfishButtonClick();
    }

    private void onFlipBoardButtonClick() {
        guiController.onUserRequestFlipBoard();
    }
}
