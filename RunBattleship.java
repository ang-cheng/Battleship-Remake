package org.cis1200.battleship;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunBattleship implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Battleship");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // Ships panel
        final ShipsPanel shipsPanel = new ShipsPanel(board.getGame());
        frame.add(shipsPanel, BorderLayout.LINE_START);

        board.setShipsPanel(shipsPanel);

        // Reset button
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new GridLayout(2, 3));
        frame.add(control_panel, BorderLayout.NORTH);

        // Instructions
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Battleship Explanation and Instructions:\n\n" +
                            "Battleship is a strategic game where each of the two players\n" +
                            "place their ships and try to use strikes to sink the other\n" +
                            "player's ships. The catch? You don't know where the other\n" +
                            "player's ships are! Furthermore, each turn you get to try and\n" +
                            "sink the other player's ships, you get anywhere from 1 to 6\n" +
                            "missile stikes!\n\n" +
                            "During the ship-placing stage, use your mouse to click on the \n" +
                            "beginning and end of where you'd like your ship to be, for all\n" +
                            "five ships. Once you're done placing your ships, click the\n" +
                            "'Confirm Ships' button. This will begin the striking phase of\n" +
                            "the game. Each turn, you will have anywhere from 1 to 6\n" +
                            "strikes available. The number of strikes is randomized. Use\n" +
                            "your mouse to click the cell where you would like to strike,\n" +
                            "and click the 'Confirm Strike' button to fire. A green circle\n" +
                            "in the cell means that you have struck a ship. To win, strike\n" +
                            "all parts of your opponent's ships before your opponent\n" +
                            "sinks all of your ships. Good luck!",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
//            board.focus();
        });

        final JButton saveGame = new JButton("Save Game");
        saveGame.addActionListener(e -> {
            try {
                JFileChooser chooser =
                        new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                chooser.setDialogTitle("Save Game");
                int returnVal = chooser.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    board.saveGame(new FileWriter(file));
                }
            } catch (IOException ex) {
                status.setText("File not valid.");
            }
//            finally {
//                board.focus();
//            }
        });

        final JButton loadGame = new JButton("Load Game");
        loadGame.addActionListener(e -> {
            try {
                JFileChooser chooser =
                        new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                chooser.setDialogTitle("Load Game");
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    board.loadGame(new FileReader(file)); // Pass the file reader
                }

                Battleship game = board.getGame();
                if (game.getIsPlacingShips()) {
                    shipsPanel.setVisible(true);
                    shipsPanel.repaint();
                    frame.pack();
                    frame.repaint();
                } else {
                    shipsPanel.setVisible(false);
                    frame.pack();
                    frame.repaint();
                }
            } catch (FileNotFoundException ex) {
                status.setText("File not found.");
            }
        });

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            board.reset();
            shipsPanel.setVisible(true);
            shipsPanel.reset();
            shipsPanel.repaint();
            frame.pack();
        });

        final JButton confirmShips = new JButton("Confirm Ships");
        confirmShips.addActionListener(e -> {
            board.confirmShipPlacement();

            if (!board.getGame().getIsPlacingShips()) {
                shipsPanel.setVisible(false);
                frame.pack();
                frame.repaint();
            }
        });

        final JButton confirmStrike = new JButton("Confirm Strike");
        confirmStrike.addActionListener(e -> board.confirmStrikePlacement());

        // add buttons in corresponding order
        control_panel.add(saveGame);
        control_panel.add(confirmShips);
        control_panel.add(reset);
        control_panel.add(loadGame);
        control_panel.add(confirmStrike);
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
