package org.cis1200.battleship;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class ShipsPanel extends JPanel {
    private Battleship game;

    private TwoShip twoShip;
    private ThreeShipOne threeShipOne;
    private ThreeShipTwo threeShipTwo;
    private FourShip fourShip;
    private FiveShip fiveShip;

    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 500;

    public ShipsPanel(Battleship game) {
        this.game = game;
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        reset();

        repaint();
    }

    public void setGame(Battleship newGame) {
        this.game = newGame;
        repaint();
    }

    public Battleship getGame() {
        return game;
    }

    /**
     //     * Returns the size of the game board.
     //     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public void reset() {
        twoShip = new TwoShip();
        threeShipOne = new ThreeShipOne();
        threeShipTwo = new ThreeShipTwo();
        fourShip = new FourShip();
        fiveShip = new FiveShip();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        boolean player1 = game.getCurrentPlayer();

        if (game.getIsPlacingShips()) {
            LinkedList<Integer> shipLengths = game.getPlayerShipLengths(player1);

            if (shipLengths.contains(2)) {
                twoShip.draw(g);
            }
            if (shipLengths.indexOf(3) != -1 &&
                    shipLengths.indexOf(3) == shipLengths.lastIndexOf(3)) {
                threeShipTwo.draw(g);
            } else if (shipLengths.indexOf(3) != shipLengths.lastIndexOf(3)) { // both three ships
                threeShipOne.draw(g);
                threeShipTwo.draw(g);
            }
            if (shipLengths.contains(4)) {
                fourShip.draw(g);
            }
            if (shipLengths.contains(5)) {
                fiveShip.draw(g);
            }
        }
    }
}