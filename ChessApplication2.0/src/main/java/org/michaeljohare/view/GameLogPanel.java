package org.michaeljohare.view;

import org.michaeljohare.controller.ChessController;
import org.michaeljohare.model.pieces.ChessPiece;
import org.michaeljohare.model.player.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameLogPanel extends JPanel {

    private JButton playAgainButton;
    private JButton askStockfishButton;
    private Color defaultButtonColor;
    private JTextArea logTextArea;
    private JScrollPane logScrollPane;
    private JTextArea player1CapturedArea;
    private JTextArea player2CapturedArea;
    private ChessController controller;
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

        askStockfishButton = new JButton("Ask Stockfish");
        askStockfishButton.setFont(new Font("Roboto", Font.BOLD, 24));
        askStockfishButton.addActionListener(e -> onAskStockfishButtonClick());

        JPanel playAgainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playAgainButtonPanel.add(playAgainButton);

        JPanel undoButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        undoButtonPanel.add(undoButton);

        JPanel askStockfishButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        askStockfishButtonPanel.add(askStockfishButton);

        JPanel undoAndAskStockfishPanel = new JPanel(new BorderLayout());
        undoAndAskStockfishPanel.add(askStockfishButtonPanel, BorderLayout.WEST);
        undoAndAskStockfishPanel.add(undoButtonPanel, BorderLayout.EAST);
        undoAndAskStockfishPanel.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));

        JPanel logPanelWithButtons = new JPanel(new BorderLayout());
        logPanelWithButtons.add(logScrollPane, BorderLayout.CENTER);
        logPanelWithButtons.add(playAgainButtonPanel, BorderLayout.NORTH);
        logPanelWithButtons.add(undoAndAskStockfishPanel, BorderLayout.SOUTH);

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

    public void currentPlayersTurnLogText(Player currentPlayer) {
        String name = currentPlayer.getName();
        String pieceColor = currentPlayer.getColor().toString();
        String pieceColorFormatted = pieceColor.charAt(0) + pieceColor.substring(1).toLowerCase();
        logTextArea.setText(lineBreaks + " It is " + name + "'s turn! (" + pieceColorFormatted + " pieces).");
    }

    public void stockfishWaitingButtonText() {
        askStockfishButton.setText("Waiting...");
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

    public void updateCapturedPiecesDisplay(List<ChessPiece> player1CapturedPieces, List<ChessPiece> player2CapturedPieces) {
        player1CapturedArea.removeAll();
        player2CapturedArea.removeAll();

        addCapturedPiecesTitle(player1CapturedArea);
        addCapturedPiecesTitle(player2CapturedArea);

        Font capturedPieceFont = new Font("Roboto", Font.PLAIN, 26);
        for (ChessPiece piece : player2CapturedPieces) {
            JLabel blackCapturedPieceLabel = new JLabel(piece.getBlackChessPieceSymbol());
            blackCapturedPieceLabel.setFont(capturedPieceFont);
            player1CapturedArea.add(blackCapturedPieceLabel);
        }

        for (ChessPiece piece : player1CapturedPieces) {
            JLabel whiteCapturedPieceLabel = new JLabel(piece.getWhiteChessPieceSymbol());
            whiteCapturedPieceLabel.setFont(capturedPieceFont);
            player2CapturedArea.add(whiteCapturedPieceLabel);
        }

        player1CapturedArea.revalidate();
        player1CapturedArea.repaint();
        player2CapturedArea.revalidate();
        player2CapturedArea.repaint();
    }

    private void addCapturedPiecesTitle(JTextArea capturedArea) {
        Font capturedPiecesTitleFont = new Font("Roboto", Font.BOLD, 24);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 70, 5, 70);
        Border lineBorder = BorderFactory.createMatteBorder(0, 0, 2, 0, Color.gray);
        Border compoundBorder = BorderFactory.createCompoundBorder(lineBorder, paddingBorder);

        JLabel capturedPiecesTitle = new JLabel("Captured Pieces");
        capturedPiecesTitle.setFont(capturedPiecesTitleFont);
        capturedPiecesTitle.setBorder(compoundBorder);
        capturedArea.add(capturedPiecesTitle);
    }

    public void updateCapturedPiecesDisplay() {
        updateCapturedPiecesDisplay(new ArrayList<>(), new ArrayList<>());
    }

    private void onPlayAgainButtonClick() {
        controller.handlePlayAgainButtonClick();
        playAgainButton.setBackground(defaultButtonColor);
        playAgainButton.setForeground(null);
        controller.clearHighlightedSquares();
        updateCapturedPiecesDisplay();
        controller.updateGUI();
    }

    public void setController(ChessController controller) {
        this.controller = controller;
    }

    private void onUndoButtonClick() {
        controller.handleUndoButtonClick();
        if (playAgainButton.getForeground() != null) {
            playAgainButton.setForeground(null);
            playAgainButton.setBackground(defaultButtonColor);
        }
    }

    private void onAskStockfishButtonClick() {
        controller.handleAskStockfishButtonClick();
    }
}
