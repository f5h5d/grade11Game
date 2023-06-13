import java.awt.*;
// import java.awt.event.*;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class L7_SpriteAnimation extends JPanel implements Runnable{

    public static BufferedImage grabImage(BufferedImage image) {
        BufferedImage img = image.getSubimage(200 * spriteNo, 0, 200, 104);
        return img;
    }

    // Class variables
    public static BufferedImage retsu;
    public static BufferedImage retsuWalk[] = new BufferedImage[10];
    public static int spriteNo = 0;

    public static int frameController = 0; 
    public static int xPos = 200;
    public static int yPos = 50;

    public L7_SpriteAnimation() {
        setPreferredSize(new Dimension(500,500));
        setFocusable(true);
        Thread t = new Thread(this);
        t.start();
    }

    // paintCOmponent is being called EVERY 0.02 seconds (50 FPS)
    // due to our DYNAMIC approach
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear the screen
        g.drawImage(retsuWalk[spriteNo],xPos,yPos,null);
        frameController++;
        if (frameController %4 == 0) {
            xPos-= 5;
            spriteNo = (spriteNo + 1)%10;
        }

    }

    public static void main(String[] args) {

        // Image Importation
        try {
            retsu = ImageIO.read(new File("Characters/Retsu/BackwardWalking.png"));
            for (int i = 0; i < 10; i++) {
                retsuWalk[i] = grabImage(retsu);
                spriteNo = (spriteNo+1) % 10;
            }
        }
        catch(Exception e) {
            System.out.println("Something wrong with the image!");
        }

        JFrame frame = new JFrame("L7 - Sprite Animation");
        L7_SpriteAnimation panel = new L7_SpriteAnimation();
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }
    
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(20);
            }
            catch (Exception e) {}
        }
    }
    
}