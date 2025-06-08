package org.cis1200.battleship;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.LinkedList;

/**
 * This class instantiates a Battleship object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Battleship game; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    int clickCount = 0;
    Point firstClick = new Point();

    private int strikeCol = -1;
    private int strikeRow = -1;

    private ShipsPanel shipsPanel;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        game = new Battleship(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                clickCount++;

                if (game.getIsPlacingShips()) { // if players are placing ships
                    if (clickCount == 2) {
                        Point p = e.getPoint();
                        Integer length = game.canPlaceShip(firstClick.x / 50,
                                firstClick.y / 50, p.x / 50, p.y / 50);

                        if (length != -1) {
                            game.placeShip(firstClick.x / 50, firstClick.y / 50,
                                    p.x / 50, p.y / 50);
                        } else {
                            status.setText("Cannot place ship here.");
                        }

                        clickCount = 0;

                        updateStatus(); // updates the status JLabel
                        repaint(); // repaints the game board
                    } else {
                        firstClick = e.getPoint();

                        updateStatus(); // updates the status JLabel
                        repaint(); // repaints the game board
                    }
                } else { // if players are launching strikes
                    firstClick = e.getPoint();

                    if (game.canPlayTurn(firstClick.x / 50, firstClick.y / 50)) {
                        strikeCol = firstClick.x / 50;
                        strikeRow = firstClick.y / 50;
                        repaint();
                    } else {
                        status.setText("Cannot strike at this location.");
                        strikeCol = -1;
                        strikeRow = -1;
                    }
                    clickCount = 0;

                    updateStatus(); // updates the status JLabel
                    repaint(); // repaints the game board
                }

                if (shipsPanel != null) {
                    shipsPanel.repaint();
                }
            }
        });
    }

    /**
     * setShipsPanel links the GameBoard and ShipsPanel in RunBattleship.
     *
     * @param panel
     */
    public void setShipsPanel(ShipsPanel panel) {
        this.shipsPanel = panel;
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        game.reset();
        shipsPanel.reset();
        status.setText("Player 1's Turn to Place Ships");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Begins striking part of the game.
     */
    public void confirmShipPlacement() {
        if (game.confirmShipPlacement()) {
            if (game.getCurrentPlayer()) {
                game.rollDie();
                status.setText("Player 1's Turn");
            } else {
                status.setText("Player 2's Turn to Place Ships");
            }
        } else {
            status.setText("Cannot confirm this ship placement.");
        }
        repaint();

        if (shipsPanel != null) {
            shipsPanel.repaint();
        }

        requestFocusInWindow();
    }

    /**
     * Allows player to confirm strike.
     */
    public void confirmStrikePlacement() {
        if (strikeCol != -1 && strikeRow != -1) {
            game.playTurn(strikeCol, strikeRow);
            status.setText("Strike confirmed.");
            strikeCol = -1;
            strikeRow = -1;
            updateStatus();
            repaint();
        } else {
            status.setText("No strike location selected.");
        }

        requestFocusInWindow();
    }

    public void saveGame(Writer writer) throws IOException {
        try {
            // write player one's board
            int[][] playerOneBoard = game.getBoard(true);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    writer.write(playerOneBoard[i][j] + " ");
                }
                writer.write("\n");
            }

            // write player two's board
            int[][] playerTwoBoard = game.getBoard(false);
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    writer.write(playerTwoBoard[i][j] + " ");
                }
                writer.write("\n");
            }

            // write current game state variables
            boolean player1 = game.getCurrentPlayer();
            boolean isPlacingShips = game.getIsPlacingShips();
            boolean gameOver = game.getGameOver();
            int dieRoll = game.getDieRoll();
            writer.write(player1 + " " + isPlacingShips + " " + gameOver + " " + dieRoll + "\n");

            if (isPlacingShips) {
                if (player1) {
                    // write player one's ship lengths
                    LinkedList<Integer> playerOneShipLengths = game.getPlayerShipLengths(true);
                    for (Integer i : playerOneShipLengths) {
                        writer.write(i + " ");
                    }
                    writer.write("\n");
                }

                // write player two's ship lengths
                LinkedList<Integer> playerTwoShipLengths = game.getPlayerShipLengths(false);
                for (Integer i : playerTwoShipLengths) {
                    writer.write(i + " ");
                }
                writer.write("\n");
            }


            writer.flush();
        } catch (IOException e) {
            throw new IOException("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(Reader reader) {
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            game = new Battleship();

            // read player one's board
            int[][] playerOneBoard = new int[10][10];
            for (int i = 0; i < 10; i++) {
                String line = bufferedReader.readLine();

                if (line == null) {
                    throw new IOException("Error reading file.");
                }

                String[] values = line.trim().split("\\s+");
                if (values.length != 10) {
                    throw new IOException("Error reading file.");
                }

                for (int j = 0; j < 10; j++) {
                    playerOneBoard[i][j] = Integer.parseInt(values[j]);
                }
            }

            // read player two's board
            int[][] playerTwoBoard = new int[10][10];
            for (int i = 0; i < 10; i++) {
                String line = bufferedReader.readLine();

                if (line == null) {
                    throw new IOException("Error reading file.");
                }

                String[] values = line.trim().split("\\s+");
                if (values.length != 10) {
                    throw new IOException("Error reading file.");
                }

                for (int j = 0; j < 10; j++) {
                    playerTwoBoard[i][j] = Integer.parseInt(values[j]);
                }
            }

            // read current game state variables
            String line = bufferedReader.readLine();
            if (line == null) {
                throw new IOException("Error reading file.");
            }
            String[] values = line.trim().split("\\s+");
            if (values.length != 4) {
                throw new IOException("Error reading file.");
            }
            boolean player1 = Boolean.parseBoolean(values[0]);
            boolean isPlacingShips = Boolean.parseBoolean(values[1]);
            boolean gameOver = Boolean.parseBoolean(values[2]);
            int dieRoll = Integer.parseInt(values[3]);

            if (isPlacingShips) {
                if (player1) {
                    // read player one's ship lengths
                    LinkedList<Integer> playerOneShipLengths = new LinkedList<Integer>();
                    line = bufferedReader.readLine();
                    if (line == null) {
                        throw new IOException("Error reading file.");
                    }
                    values = line.trim().split("\\s+");
                    if (values.length < 0 || values.length > 5) {
                        throw new IOException("Error reading file.");
                    }
                    for (String s : values) {
                        playerOneShipLengths.add(Integer.parseInt(s));
                    }
                    game.setPlayerOneShipLengths(playerOneShipLengths);
                }

                // read player two's ship lengths
                LinkedList<Integer> playerTwoShipLengths = new LinkedList<Integer>();
                line = bufferedReader.readLine();
                if (line == null) {
                    throw new IOException("Error reading file.");
                }
                values = line.trim().split("\\s+");
                if (values.length < 0 || values.length > 5) {
                    throw new IOException("Error reading file.");
                }
                for (String s : values) {
                    playerTwoShipLengths.add(Integer.parseInt(s));
                }
                game.setPlayerTwoShipLengths(playerTwoShipLengths);
            }

            game.setPlayerOneBoard(playerOneBoard);
            game.setPlayerTwoBoard(playerTwoBoard);
            game.setPlayer1(player1);
            game.setIsPlacingShips(isPlacingShips);
            game.setGameOver(gameOver);
            game.setDieRoll(dieRoll);
            if (shipsPanel != null) {
                shipsPanel.setGame(game);
                shipsPanel.repaint();
            }

            repaint();
            status.setText("Game loaded successfully.");
        } catch (FileNotFoundException e) {
            status.setText("File not found.");
        } catch (IOException e) {
            status.setText("Error loading game: " + e.getMessage());
        }
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (game.getIsPlacingShips()) {
            if (game.getCurrentPlayer()) {
                status.setText("Player 1's Turn to Place Ships");
            } else {
                status.setText("Player 2's Turn to Place Ships");
            }
        } else {
            if (game.getCurrentPlayer()) {
                status.setText("Player 1's Turn");
            } else {
                status.setText("Player 2's Turn");
            }
        }

        int winner = game.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 10;
        int unitHeight = BOARD_HEIGHT / 10;

        for (int i = 1; i <= 10; i++) {
            g.drawLine(unitWidth * i, 0, unitWidth * i, BOARD_HEIGHT);
            g.drawLine(0, unitHeight * i, BOARD_WIDTH, unitHeight * i);
        }

        if (!game.getIsPlacingShips()) {
            // Draw board based on whose turn it is
            if (game.getCurrentPlayer()) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int state = game.getPlayerTwoBoardCell(j, i);
                        drawCellState(g, state, j, i, game.getIsPlacingShips());
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int state = game.getPlayerOneBoardCell(j, i);
                        drawCellState(g, state, j, i, game.getIsPlacingShips());
                    }
                }
            }
        } else {
            // Draw board based on whose turn it is
            if (game.getCurrentPlayer()) {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int state = game.getPlayerOneBoardCell(j, i);
                        drawCellState(g, state, j, i, game.getIsPlacingShips());
                    }
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        int state = game.getPlayerTwoBoardCell(j, i);
                        drawCellState(g, state, j, i, game.getIsPlacingShips());
                    }
                }
            }
        }

        if (!game.getIsPlacingShips() && strikeCol != -1 && strikeRow != -1) {
            g.setColor(Color.RED);
            int x = strikeCol * 50;
            int y = strikeRow * 50;
            g.drawLine(x, y, x + 50, y + 50);
            g.drawLine(x, y + 50, x + 50, y);
            g.setColor(Color.BLACK);
        }
    }

    /**
     * Draw in board cell based on state of the cell.
     *
     * @param g graphics context
     * @param state integer representation of the board cell's state
     * @param j row that drawing should be drawn in
     * @param i column that drawing should be drawn in
     * @param placingShips indicator of if players are placing ships or launching strikes
     * @return a drawing in the board's cell
     */
    public void drawCellState(Graphics g, int state, int j, int i, boolean placingShips) {
        if (placingShips) { // placing ships -- reveal where the ships are
            if (state == 1) { // if it is a ship
                g.drawOval(50 * j, 50 * i, 50, 50);
            } else if (state == 2) { // if it is a strike on sea
                g.drawLine(50 * j, 50 * i, 50 + 50 * j, 50 + 50 * i);
            } else if (state == 3) { // if it is a strike on ship
                g.drawLine(50 * j, 50 * i, 50 + 50 * j, 50 + 50 * i);
                g.drawLine(50 * j, 50 + 50 * i, 50 + 50 * j, 50 * i);
            }
        } else { // not placing ships -- don't reveal where ship are
            if (state == 2) {
                g.drawLine(50 * j, 50 * i, 50 + 50 * j, 50 + 50 * i);
                g.drawLine(50 * j, 50 + 50 * i, 50 + 50 * j, 50 * i);
            } else if (state == 3) {
                g.setColor(Color.GREEN);
                g.drawOval(50 * j + 5, 50 * i + 5, 40, 40);
                g.setColor(Color.BLACK);
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public Battleship getGame() {
        return game;
    }
}
