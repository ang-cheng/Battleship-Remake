package org.cis1200.battleship;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThreeShipTwo extends ShipObject {
    public static final String IMG_FILE = "files/three_ship_two.png";
    public static final int XPOS = 150 - 56;
    public static final int YPOS = 213 + 19;
    public static final int WIDTH = 112;
    public static final int HEIGHT = 38;

    private static BufferedImage img;

    public ThreeShipTwo() {
        super(XPOS, YPOS, WIDTH, HEIGHT);

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error: " + e.getMessage());
        }
    }

    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}