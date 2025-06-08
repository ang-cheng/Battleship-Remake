package org.cis1200.battleship;
import java.io.*;
import java.util.LinkedList;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

/**
 * This class is a model for TicTacToe.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * This framework is very effective for turn-based games. We
 * STRONGLY recommend you review these lecture slides, starting at
 * slide 8, for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec36.pdf
 * 
 * This model is completely independent of the view and controller.
 * This is in keeping with the concept of modularity! We can play
 * the whole game from start to finish without ever drawing anything
 * on a screen or instantiating a Java Swing object.
 * 
 * Run this file to see the main method play a game of TicTacToe,
 * visualized with Strings printed to the console.
 */
public class Battleship {

    private int[][] playerOneBoard; // 0: empty, 1: ship, 2: strike on sea, 3: strike on ship
    private int[][] playerTwoBoard; // 0: empty, 1: ship, 2: strike on sea, 3: strike on ship

    private boolean player1;
    private boolean gameOver;
    private boolean isPlacingShips;
    private int dieRoll;

    private LinkedList<Ship> playerOneShips;
    private LinkedList<Ship> playerTwoShips;
    private LinkedList<Integer> playerOneShipLengths;
    private LinkedList<Integer> playerTwoShipLengths;

    /**
     * Constructor sets up game state.
     */
    public Battleship() {
        reset();
    }

    /**
     * canPlaceShip checks to see if a player can place a ship in a given location.
     * Returns the length of the ship if the given locations are valid and
     * -1 otherwise.
     *
     * @param c1 column to place ship in for ship's beginning location
     * @param r1 row to place ship in for ship's beginning location
     * @param c2 column to place ship in for ship's end location
     * @param r2 row to place ship in for ship's end location
     * @return length of the ship if locations are valid and -1 otherwise
     */
    public Integer canPlaceShip(int c1, int r1, int c2, int r2) {
        // Invalid start and end locations
        if (c1 != c2 && r1 != r2) {
            return -1;
        }
        if (c1 == c2 && r1 == r2) {
            return -1;
        }

        if (player1) { // if player one is placing ships
            Integer length = canPlaceShipHelper(playerOneShipLengths,
                    playerOneBoard, c1, r1, c2, r2);
            if (length != -1) {
                return length;
            } else {
                return -1;
            }
        } else { // if player two is placing ships
            Integer length = canPlaceShipHelper(playerTwoShipLengths,
                    playerTwoBoard, c1, r1, c2, r2);
            if (length != -1) {
                return length;
            }
            return -1;
        }
    }

    public Integer canPlaceShipHelper(LinkedList<Integer> shipLengths, int[][] playerBoard,
                                      int c1, int r1, int c2, int r2) {
        if (r1 == r2) { // if ship is in the same row
            Integer length = Math.max(c1, c2) - Math.min(c1, c2) + 1;
            if (shipLengths.contains(length)) { // has this ship been placed?
                for (int i = Math.min(c1, c2); i <= Math.max(c1, c2); i++) {
                    if (playerBoard[r1][i] != 0) {
                        return -1;
                    }
                }
                return length;
            } else { // ship has already been placed
                return -1;
            }
        } else if (c1 == c2) { // if ship is in the same column
            Integer length = Math.max(r1, r2) - Math.min(r1, r2) + 1;
            if (shipLengths.contains(length)) { // has this ship been placed?
                for (int i = Math.min(r1, r2); i <= Math.max(r1, r2); i++) {
                    if (playerBoard[i][c1] != 0) {
                        return -1;
                    }
                }
                return length;
            } else { // ship has already been placed
                return -1;
            }
        }
        return -1;
    }

    /**
     * placeShip allows a player to place a ship in a given
     * location. MUST FIRST USE canPlaceShip to verify that the location
     * is available to put a ship in.
     *
     * @param c1 column to place ship in for ship's beginning location
     * @param r1 row to place ship in for ship's beginning location
     * @param c2 column to place ship in for ship's end location
     * @param r2 row to place ship in for ship's end location
     */
    public void placeShip(int c1, int r1, int c2, int r2) {
        if (player1) {
            if (r1 == r2) {
                Integer length = Math.abs(c1 - c2) + 1;
                playerOneShips.add(new Ship(length, c1, r1, c2, r2));
                playerOneShipLengths.remove(length);
            } else if (c1 == c2) {
                Integer length = Math.abs(r1 - r2) + 1;
                playerOneShips.add(new Ship(length, c1, r1, c2, r2));
                playerOneShipLengths.remove(length);
            }
            updateBoardWithShip(player1, c1, r1, c2, r2);
        } else {
            if (r1 == r2) {
                Integer length = Math.abs(c1 - c2) + 1;
                playerTwoShips.add(new Ship(length, c1, r1, c2, r2));
                playerTwoShipLengths.remove(length);
            } else if (c1 == c2) {
                Integer length = Math.abs(r1 - r2) + 1;
                playerTwoShips.add(new Ship(length, c1, r1, c2, r2));
                playerTwoShipLengths.remove(length);
            }
            updateBoardWithShip(player1, c1, r1, c2, r2);
        }
    }

    /**
     * updateBoardWithShip updates a player's board after a player successfully
     * places a ship.
     *
     * @param player1 which player placed the boat
     * @param c1 column to place ship in for ship's beginning location
     * @param r1 row to place ship in for ship's beginning location
     * @param c2 column to place ship in for ship's end location
     * @param r2 row to place ship in for ship's end location
     */
    public void updateBoardWithShip(boolean player1, int c1, int r1, int c2, int r2) {
        if (player1) {
            if (r1 == r2) {
                for (int i = Math.min(c1, c2); i <= Math.max(c1, c2); i++) {
                    playerOneBoard[r1][i] = 1;
                }
            } else if (c1 == c2) {
                for (int i = Math.min(r1, r2); i <= Math.max(r1, r2); i++) {
                    playerOneBoard[i][c1] = 1;
                }
            }
        } else {
            if (r1 == r2) {
                for (int i = Math.min(c1, c2); i <= Math.max(c1, c2); i++) {
                    playerTwoBoard[r1][i] = 1;
                }
            } else if (c1 == c2) {
                for (int i = Math.min(r1, r2); i <= Math.max(r1, r2); i++) {
                    playerTwoBoard[i][c1] = 1;
                }
            }
        }
    }

    /**
     * canPlayTurn checks if player can play a turn in a given location. Returns true
     * if the move can be played in that location and false otherwise.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean canPlayTurn(int c, int r) {
        if (c < 0 || c > 9) {
            return false;
        }
        if (r < 0 || r > 9) {
            return false;
        }

        if (player1) {
            if (checkWinner() != 0) {
                return false;
            }
            if (playerTwoBoard[r][c] == 2 || playerTwoBoard[r][c] == 3 || gameOver) {
                return false;
            }
            return true;
        } else {
            if (checkWinner() != 0) {
                player1 = !player1;
            }
            if (playerOneBoard[r][c] == 2 || playerOneBoard[r][c] == 3 || gameOver) {
                return false;
            }
            return true;
        }
    }

    /**
     * playTurn is called in a provided location only AFTER canPlayTurn returns TRUE
     * for that given location.
     */
    public void playTurn(int c, int r) {
        if (player1) {
            playTurnHelper(playerTwoBoard, playerTwoShips, c, r);
        } else {
            playTurnHelper(playerOneBoard, playerOneShips, c, r);
        }

        if (checkWinner() == 0 && dieRoll == 0) {
            player1 = !player1;
            dieRoll = rollDie();
        } else if (checkWinner() == 0) {
            dieRoll--;
        }
    }

    public void playTurnHelper(int[][] board, LinkedList<Ship> ships, int c, int r) {
        if (board[r][c] == 0) {
            board[r][c] = 2;
        } else if (board[r][c] == 1) {
            board[r][c] = 3;

            for (Ship s : ships) {
                for (int[] location : s.getLocation()) {
                    if (location[0] == c && location[1] == r) {
                        location[2] = 1;
                    }
                }
            }
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won
     */
    public int checkWinner() {
        boolean playerTwoWin = true, playerOneWin = true;
        for (int i = 0; i < playerOneBoard.length; i++) {
            for (int j = 0; j < playerOneBoard[i].length; j++) {
                if (playerOneBoard[i][j] == 1) {
                    playerTwoWin = false;
                }
            }
        }
        if (playerTwoWin && !isPlacingShips) {
            gameOver = true;
            return 2;
        }

        for (int i = 0; i < playerTwoBoard.length; i++) {
            for (int j = 0; j < playerTwoBoard[i].length; j++) {
                if (playerTwoBoard[i][j] == 1) {
                    playerOneWin = false;
                }
            }
        }
        if (playerOneWin && !isPlacingShips) {
            gameOver = true;
            return 1;
        }

        return 0;
    }

    public int rollDie() {
        return (int) (Math.random() * 6);
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("----------------------------------------");
        System.out.println("Player One's Board: \n");
        for (int i = 0; i < playerOneBoard.length; i++) {
            for (int j = 0; j < playerOneBoard[i].length; j++) {
                System.out.print(playerOneBoard[i][j]);
                if (j < 9) {
                    System.out.print(" | ");
                }
            }
            if (i < 9) {
                System.out.println("\n-------------------------------------");
            }
        }
        System.out.println("\n----------------------------------------");
        System.out.println("Player Two's Board: \n");
        for (int i = 0; i < playerTwoBoard.length; i++) {
            for (int j = 0; j < playerTwoBoard[i].length; j++) {
                System.out.print(playerTwoBoard[i][j]);
                if (j < 9) {
                    System.out.print(" | ");
                }
            }
            if (i < 9) {
                System.out.println("\n-------------------------------------");
            }
        }
        System.out.println("\n----------------------------------------");
        System.out.println("Player Turn: " + player1);
        System.out.println("Game Over State: " + gameOver);
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        playerOneBoard = new int[10][10];
        playerTwoBoard = new int[10][10];
        player1 = true;
        gameOver = false;
        isPlacingShips = true;
        dieRoll = rollDie();
        playerOneShips = new LinkedList<>();
        playerTwoShips = new LinkedList<>();
        playerOneShipLengths = new LinkedList<>();
        playerTwoShipLengths = new LinkedList<>();
        playerOneShipLengths.add(3);
        playerOneShipLengths.add(3);
        playerOneShipLengths.add(2);
        playerOneShipLengths.add(4);
        playerOneShipLengths.add(5);
        playerTwoShipLengths.add(3);
        playerTwoShipLengths.add(3);
        playerTwoShipLengths.add(2);
        playerTwoShipLengths.add(4);
        playerTwoShipLengths.add(5);
    }

    /**
     * confirmShipPlacement returns true if all ships have been
     * successfully placed by the player, and false otherwise.
     */
    public boolean confirmShipPlacement() {
        if (!isPlacingShips) {
            return false;
        }
        if (player1 && playerOneShipLengths.isEmpty()) {
            player1 = !player1;
            return true;
        } else if (!player1 && playerTwoShipLengths.isEmpty()) {
            player1 = !player1;
            isPlacingShips = !isPlacingShips;
            return true;
        }
        return false;
    }

    /**
     * confirmStrikePlacement returns true if player can strike
     * a missile in the given location and successfully strikes,
     * and false otherwise.
     */
    public boolean confirmStrikePlacement() {
        return !(dieRoll == 0 || isPlacingShips);
    }


    // GETTER METHODS!!!!!!!!!
    /**
     * getCurrentPlayer is a getter for the player
     * whose turn it is in the game.
     * 
     * @return true if it's Player 1's turn,
     *         false if it's Player 2's turn.
     */
    public boolean getCurrentPlayer() {
        return player1;
    }

    public int getDieRoll() {
        return dieRoll + 1;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    /**
     * getIsPlacingShips is a getter for the state of the game,
     * indicating if the players are currently placing ships or not.
     *
     * @return true if players are placing ships,
     *         false if players are nat placing ships.
     */
    public boolean getIsPlacingShips() {
        return isPlacingShips;
    }

    public LinkedList<Integer> getPlayerShipLengths(boolean player1) {
        if (player1) {
            return playerOneShipLengths;
        } else {
            return playerTwoShipLengths;
        }
    }

    public LinkedList<Ship> getPlayerShips(boolean player1) {
        if (player1) {
            return playerOneShips;
        } else {
            return playerTwoShips;
        }
    }

    public int[][] getBoard(boolean player1) {
        if (player1) {
            return playerOneBoard;
        } else {
            return playerTwoBoard;
        }
    }

    /**
     * getPlayerOneBoardCell is a getter for the contents of the cell specified by the method
     * arguments in player one's board.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = sea, 1 = ship, 2 = strike on sea, 3 = strike on ship
     */
    public int getPlayerOneBoardCell(int c, int r) {
        return playerOneBoard[r][c];
    }

    /**
     * getPlayerTwoBoardCell is a getter for the contents of the cell specified by the method
     * arguments in player two's board.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the contents of the corresponding cell on the
     *         game board. 0 = sea, 1 = ship, 2 = strike on sea, 3 = strike on ship
     */
    public int getPlayerTwoBoardCell(int c, int r) {
        return playerTwoBoard[r][c];
    }


    // SETTER METHODS!!!

    public void setPlayerOneBoard(int[][] board) {
        playerOneBoard = board;
    }

    public void setPlayerTwoBoard(int[][] board) {
        playerTwoBoard = board;
    }

    public void setPlayerOneShipLengths(LinkedList<Integer> lengths) {
        playerOneShipLengths = lengths;
    }

    public void setPlayerTwoShipLengths(LinkedList<Integer> lengths) {
        playerTwoShipLengths = lengths;
    }

    public void setPlayer1(boolean player1) {
        this.player1 = player1;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setIsPlacingShips(boolean placingShips) {
        this.isPlacingShips = placingShips;
    }

    public void setDieRoll(int dieRoll) {
        this.dieRoll = dieRoll;
    }


    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {
        Battleship b = new Battleship();

        // both players place their ships, and confirm placement
        for (int i = 0; i < 2; i++) {
            b.placeShip(0, 0, 1, 0);
//            b.printGameState();

            b.placeShip(0, 1, 2, 1);
//            b.printGameState();

            b.placeShip(0, 2, 2, 2);
//            b.printGameState();

            b.placeShip(0, 3, 3, 3);
//            b.printGameState();

            b.placeShip(0, 4, 4, 4);
//            b.printGameState();

            b.confirmShipPlacement();
//            b.printGameState();
        }
    }
}
