package org.cis1200.battleship;

import java.awt.*;

public abstract class ShipObject {
    private int px;
    private int py;

    private final int width;
    private final int height;

    public ShipObject(int px, int py, int width, int height) {
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;
    }

    // GETTERS
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    // SETTERS
    public void setPx(int px) {
        this.px = px;
    }

    public void setPy(int py) {
        this.py = py;
    }
}