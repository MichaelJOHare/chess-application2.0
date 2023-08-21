package org.michaeljohare.view.setup;

import org.michaeljohare.model.player.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartupDialog extends JDialog {
    private JTextField playerName1Field, playerName2Field;
    private JComboBox<String> colorChoice1, colorChoice2;
    private JComboBox<String> opponentChoice, stockfishEloChoice;
    private JLabel stockfishElo;
    private JButton startButton;
    private Font defaultFont = new Font("Roboto", Font.PLAIN, 18);

    private String playerName1, playerName2;
    private PlayerColor playerColor1, playerColor2;
    private boolean playWithStockfish;
    private int stockfishEloNumber;

    public StartupDialog(Frame owner) {
        super(owner, "Start New Game", true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(50, 10, 10, 10);
        JLabel title = new JLabel("Michael's Chess Game");
        title.setFont(new Font("Roboto", Font.BOLD, 36));
        title.setHorizontalAlignment(JLabel.CENTER);
        add(title, gbc);

        gbc.gridy = 1;
        addSpacer(gbc, 1.0);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        // Player 1 Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        addSpacer(gbc, 0.2);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(50,10,10,10);
        JLabel player1Name = new JLabel("Player 1 Name:");
        setFontSize(player1Name);
        add(player1Name, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        playerName1Field = new JTextField(10);
        setFontSize(playerName1Field);
        add(playerName1Field, gbc);

        // Player 1 Color choice
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel player1ColorChoice = new JLabel("Player 1 Color:");
        setFontSize(player1ColorChoice);
        add(player1ColorChoice, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        colorChoice1 = new JComboBox<>(new String[]{"White", "Black"});
        setFontSize(colorChoice1);
        add(colorChoice1, gbc);

        // Player 2 Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10,10,10,10);
        JLabel player2Name = new JLabel("Player 2 Name:");
        setFontSize(player2Name);
        add(player2Name, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;
        playerName2Field = new JTextField(10);
        setFontSize(playerName2Field);
        add(playerName2Field, gbc);

        // Player 2 Color choice
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel player2ColorChoice = new JLabel("Player 2 Color:");
        setFontSize(player2ColorChoice);
        add(player2ColorChoice, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        colorChoice2 = new JComboBox<>(new String[]{"Black", "White"});
        setFontSize(colorChoice2);
        add(colorChoice2, gbc);
        addSpacer(gbc, 0.2);

        // Opponent choice panel
        JPanel opponentPanel = new JPanel(new GridLayout(2, 1));
        setFontSize(opponentPanel);

        JLabel opponent = new JLabel("Opponent", SwingConstants.CENTER);
        setFontSize(opponent);
        opponentPanel.add(opponent);

        opponentChoice = new JComboBox<>(new String[]{"Human", "Stockfish"});
        setFontSize(opponentChoice);
        opponentChoice.addActionListener(e -> onOpponentChoiceChange());
        opponentPanel.add(opponentChoice);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(90,10,10,10);
        gbc.anchor = GridBagConstraints.CENTER;
        add(opponentPanel, gbc);

        // Stockfish Elo choice
        JPanel stockfishEloPanel = new JPanel(new GridLayout(2, 1));
        setFontSize(stockfishEloPanel);

        stockfishElo = new JLabel("Stockfish Elo", SwingConstants.CENTER);
        setFontSize(stockfishElo);
        stockfishElo.setVisible(false);
        stockfishEloPanel.add(stockfishElo);

        stockfishEloChoice = new JComboBox<>(new String[]{"800", "1200", "1600", "2000", "2400", "2800"});
        setFontSize(stockfishEloChoice);
        stockfishEloChoice.setVisible(false);
        stockfishEloPanel.add(stockfishEloChoice);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(20,10,10,10);
        gbc.anchor = GridBagConstraints.CENTER;
        add(stockfishEloPanel, gbc);

        // Spacer for start button
        gbc.gridy = 6;
        gbc.weighty = 0.8;
        add(new JLabel(""), gbc);
        gbc.weighty = 0;

        // Start button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(50,10,20,10);
        gbc.anchor = GridBagConstraints.CENTER;
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Roboto", Font.BOLD, 24));
        add(startButton, gbc);

        pack();
        setSize(800, 800);

        startButton.addActionListener(e -> onGameStart());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void onGameStart() {
        playerName1 = playerName1Field.getText();
        String selectedOpponent = (String) opponentChoice.getSelectedItem();
        playWithStockfish = "Stockfish".equals(selectedOpponent);

        if (playWithStockfish) {
            playerName2 = "Stockfish";
            playerColor1 = getColorChoice(colorChoice1);
            playerColor2 = oppositeColor(playerColor1);
            setupStockfishElo();
        } else {
            playerName2 = playerName2Field.getText();
            if (isInvalidPlayerName(playerName2)) {
                showError("Please enter a valid name for Player 2.");
                return;
            }

            playerColor1 = getColorChoice(colorChoice1);
            playerColor2 = getColorChoice(colorChoice2);
            if (playerColor1 == playerColor2) {
                showError("Both players cannot choose the same color.");
                return;
            }
        }

        if (isInvalidPlayerName(playerName1)) {
            showError("Please enter a valid name for Player 1.");
            return;
        }

        dispose();
    }

    private PlayerColor getColorChoice(JComboBox<String> comboBox) {
        return "White".equals(comboBox.getSelectedItem()) ? PlayerColor.WHITE : PlayerColor.BLACK;
    }

    private PlayerColor oppositeColor(PlayerColor color) {
        return color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
    }

    private boolean isInvalidPlayerName(String playerName) {
        return playerName == null || playerName.trim().isEmpty() || "Stockfish".equals(playerName);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    private void setupStockfishElo() {
        String selectedElo = (String) stockfishEloChoice.getSelectedItem();
        stockfishEloNumber = selectedElo != null ? Integer.parseInt(selectedElo) : 800;
    }

    private void onOpponentChoiceChange() {
        if ("Stockfish".equals(opponentChoice.getSelectedItem())) {
            playerName2Field.setEnabled(false);
            colorChoice2.setEnabled(false);
            stockfishElo.setVisible(true);
            stockfishEloChoice.setVisible(true);
        } else {
            playerName2Field.setEnabled(true);
            colorChoice2.setEnabled(true);
            stockfishElo.setVisible(false);
            stockfishEloChoice.setVisible(false);
        }
    }

    private void setFontSize(Component comp) {
        comp.setFont(defaultFont);
    }

    private void addSpacer(GridBagConstraints gbc, double weight) {
        JLabel spacer = new JLabel("");
        gbc.weighty = weight;
        add(spacer, gbc);
    }

    public String getPlayerName1() {
        return playerName1;
    }

    public String getPlayerName2() {
        return playerName2;
    }

    public PlayerColor getPlayerColor1() {
        return playerColor1;
    }

    public PlayerColor getPlayerColor2() {
        return playerColor2;
    }

    public boolean isPlayWithStockfish() {
        return playWithStockfish;
    }

    public int getStockfishElo() {
        return stockfishEloNumber;
    }
}
