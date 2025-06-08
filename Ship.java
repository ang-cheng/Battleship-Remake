package org.cis1200.battleship;

public class Ship implements Comparable<Ship> {
    private int length;

    // consists of arrays of length 3 with column and row coords and sunk status
    // location[x][2] = 1 if that part of the ship has been struck, 0 if not
    private int[][] location;

    // x1, y1 is ship's beginning location. x2, y2 is ship's end location.
    public Ship(int length, int c1, int r1, int c2, int r2) {
        this.length = length;
        this.location = new int[length][3];

        // Record ship's location
        if (r1 == r2) {
            for (int i = 0; i < length; i++) {
                for (int j = Math.min(c1, c2); j <= Math.max(c1, c2); j++) {
                    location[i][1] = r1;
                    location[i][0] = j;
                    location[i][2] = 0;
                }
            }
        } else if (c1 == c2) {
            for (int i = 0; i < length; i++) {
                for (int j = Math.min(r1, r2); j <= Math.max(r1, r2); j++) {
                    location[i][0] = c1;
                    location[i][1] = j;
                    location[i][2] = 0;
                }
            }
        }
    }

    public int[][] getLocation() {
        return location;
    }

    public boolean getSunk(int[][] board) { // returns true if ship sunk, false otherwise
        boolean sunk = false;
        for (int i = 0; i < length; i++) {
            int col = location[i][0];
            int row = location[i][1];

            if (board[row][col] != 1) {
                sunk = true;
            }
        }

        return sunk;
    }

    public int getLength() {
        return length;
    }

    @Override
    public int compareTo(Ship s) {
        if (this.length == s.getLength()) {
            return 0;
        } else if (this.length > s.getLength()) {
            return 1;
        } else {
            return -1;
        }
    }
}
