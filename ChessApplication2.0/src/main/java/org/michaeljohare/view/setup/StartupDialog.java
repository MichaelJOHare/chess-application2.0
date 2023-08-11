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

    private String playerName1, playerName2;
    private PlayerColor playerColor1, playerColor2;
    private boolean playWithStockfish;

    public StartupDialog(Frame owner) {
        super(owner, "Start New Game", true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Player 1 Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10,10,10,40);
        add(new JLabel("Player 1 Name: "), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        playerName1Field = new JTextField(10);
        add(playerName1Field, gbc);

        // Player 1 Color choice
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Player 1 Color: "), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        colorChoice1 = new JComboBox<>(new String[]{"White", "Black"});
        add(colorChoice1, gbc);

        // Player 2 Name
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Player 2 Name: "), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10,10,10,10);
        playerName2Field = new JTextField(10);
        add(playerName2Field, gbc);

        // Player 2 Color choice
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Player 2 Color: "), gbc);

        gbc.gridx = 3;
        gbc.anchor = GridBagConstraints.WEST;
        colorChoice2 = new JComboBox<>(new String[]{"White", "Black"});
        add(colorChoice2, gbc);

        // Opponent choice
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(40,10,10,10);
        add(new JLabel("Opponent: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10,10,10,10);
        opponentChoice = new JComboBox<>(new String[]{"Human", "Stockfish"});
        add(opponentChoice, gbc);
        opponentChoice.addActionListener(e -> onOpponentChoiceChange());

        // Start button
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(40,10,10,10);
        startButton = new JButton("Start Game");
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
