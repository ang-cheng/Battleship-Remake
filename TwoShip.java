package org.cis1200.battleship;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TwoShip extends ShipObject {
    public static final String IMG_FILE = "files/two_ship.png";
    public static final int XPOS = 150 - 37;
    public static final int YPOS = 71 + 18;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 30;

    private static BufferedImage img;

    public TwoShip() {
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