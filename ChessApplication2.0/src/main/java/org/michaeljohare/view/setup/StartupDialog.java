package org.michaeljohare.view.setup;

import org.michaeljohare.model.player.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class StartupDialog extends JDialog {
    private JTextField playerName1Field, playerName2Field;
    private JComboBox<String> colorChoice1, colorChoice2;
    private JComboBox<String> opponentChoice;
    private JButton startButton;
    private Font defaultFont = new Font("Roboto", Font.PLAIN, 18);

    private String playerName1, playerName2;
    private PlayerColor playerColor1, playerColor2;
    private boolean playWithStockfish;

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

        if ("Stockfish".equals(opponentChoice.getSelectedItem())) {
            playerName2 = "Stockfish";
        } else {
            playerName2 = playerName2Field.getText();
            if (playerName2 == null || playerName2.trim().isEmpty() || playerName2.equals("Stockfish")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid name for Player 2.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (playerName1 == null || playerName1.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name for Player 1.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object selectedColor1 = colorChoice1.getSelectedItem();
        playerColor1 = "White".equals(selectedColor1) ? PlayerColor.WHITE : PlayerColor.BLACK;

        if ("Stockfish".equals(opponentChoice.getSelectedItem())) {
            playerColor2 = playerColor1 == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        } else {
            Object selectedColor2 = colorChoice2.getSelectedItem();
            playerColor2 = "White".equals(selectedColor2) ? PlayerColor.WHITE : PlayerColor.BLACK;

            if (playerColor1 == playerColor2) {
                JOptionPane.showMessageDialog(this, "Both players cannot choose the same color.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Object selectedOpponent = opponentChoice.getSelectedItem();
        playWithStockfish = "Stockfish".equals(selectedOpponent);

        dispose();
    }

    private void onOpponentChoiceChange() {
        if ("Stockfish".equals(opponentChoice.getSelectedItem())) {
            playerName2Field.setEnabled(false);
            colorChoice2.setEnabled(false);
        } else {
            playerName2Field.setEnabled(true);
            colorChoice2.setEnabled(true);
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
}
