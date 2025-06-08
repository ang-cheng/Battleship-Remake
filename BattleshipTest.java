package org.cis1200.battleship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BattleshipTest {
    Battleship b;

    @BeforeEach
    public void setUp() {
        b = new Battleship();
    }

    @Test
    public void testStrikeRegisteredShip() {
        for (int i = 0; i < 2; i++) {
            b.placeShip(0, 0, 1, 0);
            b.placeShip(0, 1, 2, 1);
            b.placeShip(0, 2, 2, 2);
            b.placeShip(0, 3, 3, 3);
            b.placeShip(0, 4, 4, 4);
            b.confirmShipPlacement();
        }
        assertFalse(b.getIsPlacingShips());

        // check that player one's strike hits player two's ship
        assertTrue(b.canPlayTurn(0, 0));
        b.playTurn(0, 0);
        assertEquals(3, b.getPlayerTwoBoardCell(0, 0));
    }

    @Test
    public void testStrikeRegisteredSea() {
        for (int i = 0; i < 2; i++) {
            b.placeShip(0, 0, 1, 0);
            b.placeShip(0, 1, 2, 1);
            b.placeShip(0, 2, 2, 2);
            b.placeShip(0, 3, 3, 3);
            b.placeShip(0, 4, 4, 4);
            b.confirmShipPlacement();
        }
        assertFalse(b.getIsPlacingShips());

        // check that player one's strike hits player two's sea
        assertTrue(b.canPlayTurn(9, 9));
        b.playTurn(9, 9);
        assertEquals(2, b.getPlayerTwoBoardCell(9, 9));
    }

    @Test
    public void testInvalidStrikeLocation() {
        for (int i = 0; i < 2; i++) {
            b.placeShip(0, 0, 1, 0);
            b.placeShip(0, 1, 2, 1);
            b.placeShip(0, 2, 2, 2);
            b.placeShip(0, 3, 3, 3);
            b.placeShip(0, 4, 4, 4);
            b.confirmShipPlacement();
        }
        assertFalse(b.getIsPlacingShips());

        assertFalse(b.canPlayTurn(99, 99));
    }

    @Test
    public void testBoardUpdatesAfterShipsPlaced() {
        assertEquals(2, b.canPlaceShip(0, 0, 1, 0));
        b.placeShip(0, 0, 1, 0);
        int[][] board = b.getBoard(true); // true represents player one's board
        assertEquals(1, board[0][0]);
        assertEquals(1, board[0][1]);

        assertEquals(3, b.canPlaceShip(0, 1, 2, 1));
        b.placeShip(0, 1, 2, 1);
        assertEquals(1, board[1][1]);
        assertEquals(1, board[1][1]);
        assertEquals(1, board[1][2]);
    }

    @Test
    public void testGoodShipPlacement() {
        assertEquals(2, b.canPlaceShip(0, 0, 1, 0));
        assertEquals(4, b.canPlaceShip(0, 1, 3, 1));
    }

    @Test
    public void testBadPlaceShipTwice() {
        assertEquals(2, b.canPlaceShip(0, 0, 1, 0));
        b.placeShip(0, 0, 1, 0);
        assertEquals(4, b.canPlaceShip(0, 1, 3, 1));
        b.placeShip(0, 1, 3, 1);
        assertEquals(-1, b.canPlaceShip(0, 5, 1, 5));
    }

    @Test
    public void testBadShipPlacementSameLocation() {
        assertEquals(-1, b.canPlaceShip(0, 0, 0, 0));
    }

    @Test
    public void testBadShipPlacementDiagonal() {
        assertEquals(-1, b.canPlaceShip(0, 0, 1, 1));
    }

    @Test
    public void testBadShipPlacementOverlap() {
        assertEquals(2, b.canPlaceShip(0, 0, 1, 0));
        b.placeShip(0, 0, 1, 0);
        assertEquals(4, b.canPlaceShip(0, 1, 3, 1));
        b.placeShip(0, 1, 3, 1);
        assertEquals(-1, b.canPlaceShip(0, 0, 4, 0));
    }

    @Test
    public void testOutOfBoundsShipPlacement() {
        assertEquals(-1, b.canPlaceShip(99, 99, 99, 38));
    }

    @Test
    public void testChangedPlayerAfterShipsPlaced() {
        b.placeShip(0, 0, 1, 0);
        b.placeShip(0, 1, 2, 1);
        b.placeShip(0, 2, 2, 2);
        b.placeShip(0, 3, 3, 3);
        b.placeShip(0, 4, 4, 4);

        assertTrue(b.confirmShipPlacement());
        assertFalse(b.getCurrentPlayer()); // false for player two's turn
    }
}
