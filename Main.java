import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.w3c.dom.css.RGBColor;

import java.util.ArrayList;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.net.URL;
import java.util.Random;

public class Main extends JPanel implements KeyListener, Runnable {
    // GS 0 = Main Screen
    // GS 1 = Credits
    // GS 2 = Settings
    // GS 3 = Character Selection
    // GS 4 = Keybind Select
    // GS 5 = Map Select
    // GS 6 = Gameplay
    // GS 7 = Winner and Loser Screen

    static int forward = 0;
    static int back = 1;
    static int idle = 2;
    static int punch = 3;
    static int kick = 4;
    static int jump = 5;
    static int jumpKick = 6;
    static int jumpPunch = 7;
    static int duck = 8;
    static int duckPunch = 9;
    static int duckKick = 10;
    static int block = 11;
    static int duckBlock = 12;
    static int hit = 13;
    static int knocked = 14;
    static int special = 15;
    static int knockedOut = 16;

    public static BufferedImage grabImage(BufferedImage image, int spriteNum) {
        BufferedImage img = image.getSubimage(200 * spriteNum, 0, 200, 140);
        return img;
    }

    // dont have a place for now variables
    public static int windowHeight = 367;
    public static int windowWidth = 800;
    public static int gameState = 0;
    public static boolean blink = true;
    public static int frameController = 0;
    public static boolean hoverBlink = true;

    public static Image bg;
    public static BufferedImage[] ui = new BufferedImage[1];

    public static boolean gameMusic = true;
    public static boolean gameSFX = true;

    public static int logoHeight = 0;

    // char related
    public static BufferedImage p1retsuBig, p2retsuBig;
    public static BufferedImage p1ryuBig, p2ryuBig;
    public static BufferedImage retsuPortrait;
    public static BufferedImage ryuPortrait;
    public static BufferedImage comingSoon, comingSoonBig;

    public static BufferedImage[] p1Retsu = new BufferedImage[17];
    public static BufferedImage[] p2Retsu = new BufferedImage[17];

    public static BufferedImage[] p1Ryu = new BufferedImage[17];
    public static BufferedImage[] p2Ryu = new BufferedImage[17];
    public static int totalMoves = 17;

    public static boolean charactersSelected = false;

    // map select related

    public static Image map0Icon, map1Icon, map2Icon, map3Icon;
    public static Image map0, map1, map2, map3;
    public static Image chosenMap;
    public static int chosenMapNum = -1;

    public static int defaultPosY = 0;

    public static String startTimer = "3";
    // p1 variables
    public static BufferedImage[][] p1 = new BufferedImage[17][10];
    public static String p1Char = "";
    public static BufferedImage p1CurrentMovement;
    public static int p1PosX = 100;
    public static int p1PosY = 250;
    public static int p1Health = 100;
    public static int p1Stamina = 100;
    public static int p1Hover = 0;
    public static int p1HoverCharMovement = -50;
    public static boolean p1CharSwitch = false;
    public static int p1Map = -1;
    public static int p1Combo = 0;
    public static boolean p1Action[] = new boolean[17];
    public static boolean p2Action[] = new boolean[17];

    // p2 variables
    public static BufferedImage[][] p2 = new BufferedImage[17][10];
    public static String p2Char = "";
    public static BufferedImage p2CurrentMovement;
    public static int[] p1Sprite = new int[17];
    public static int[] p2Sprite = new int[17];

    public static int p2PosX = 700;
    public static int p2PosY = 250;
    public static int p2Health = 100;
    public static int p2Stamina = 100;
    public static int p2Hover = 0;
    public static int p2HoverCharMovement = 495;
    public static boolean p2CharSwitch = false;
    public static int p2Map = -1;

    public static int p1Score = 0;
    public static int p2Score = 0;

    public static boolean hasp1Hit = false;
    public static boolean hasp2Hit = false;

    // fonts sizes
    public static Font largeText, text, comboText;

    // staminas

    public static boolean takenP1Stamina = false;
    public static boolean takenP2Stamina = false;
    public static int punchStamina = 15;
    public static int kickStamina = 30;
    public static int jumpKickStamina = 40;
    public static int jumppunchStamina = 30;
    public static int blockStamina = 1;
    public static int specialStamina = 30;

    public static int p1ForwardBind = 68;
    public static int p2ForwardBind = 39;
    public static int p1BackBind = 65;
    public static int p2BackBind = 37;
    public static int p1JumpBind = 87;
    public static int p2JumpBind = 38;
    public static int p1KickBind = 86;
    public static int p2KickBind = 77;
    public static int p1PunchBind = 67;
    public static int p2PunchBind = 44;
    public static int p1DuckBind = 83;
    public static int p2DuckBind = 40;
    public static int p1BlockBind = 88;
    public static int p2BlockBind = 46;
    public static int p2Combo = 0;
    public static int p1SpecialBind = 90;
    public static int p2SpecialBind = 47;

    public static int p1PunchDamage = 5;
    public static int p1KickDamage = 7;
    public static int p1JumpPunchDamage = 7;
    public static int p1JumpKickDamage = 10;
    public static int p1SpecialDamage = 15;

    public static int p2PunchDamage = 5;
    public static int p2KickDamage = 7;
    public static int p2JumpPunchDamage = 7;
    public static int p2JumpKickDamage = 10;
    public static int p2SpecialDamage = 15;

    public static int[] p1MoveLength = new int[totalMoves];
    public static int[] p2MoveLength = new int[totalMoves];
    public static ArrayList<Integer> keysPressed = new ArrayList();

    public Main() {
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        addKeyListener(this);
        Thread t = new Thread(this);
        t.start();
    }

    public static void main(String[] args) throws FontFormatException {
        // Image Importation
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            largeText = Font.createFont(Font.TRUETYPE_FONT, new File("UI/Act_Of_Rejection.ttf")).deriveFont(80f);
            ge.registerFont(largeText);

            comboText = Font.createFont(Font.TRUETYPE_FONT, new File("UI/Act_Of_Rejection.ttf")).deriveFont(30f);
            ge.registerFont(comboText);

            text = Font.createFont(Font.TRUETYPE_FONT, new File("UI/PressStart2P.ttf")).deriveFont(15f);
            ge.registerFont(text);

            bg = new ImageIcon(new File("bg/0.gif").toURL()).getImage();

            // char related
            comingSoon = ImageIO.read(new File("Characters/comingSoon.png"));

            comingSoonBig = ImageIO.read(new File("Characters/comingSoonBig.png"));

            retsuPortrait = ImageIO.read(new File("Characters/Retsu/portrait.png"));

            ryuPortrait = ImageIO.read(new File("Characters/Ryu/portrait.png"));

            p1retsuBig = ImageIO.read(new File("Characters/Retsu/p1/retsuBig.png"));

            p2retsuBig = ImageIO.read(new File("Characters/Retsu/p2/retsuBig.png"));
            
            p1ryuBig = ImageIO.read(new File("Characters/Ryu/p1/ryuBig.png"));

            p2ryuBig = ImageIO.read(new File("Characters/Ryu/p2/ryuBig.png"));

            // map select related
            map0Icon = new ImageIcon(new File("bg/0s.gif").toURL()).getImage();
            map1Icon = new ImageIcon(new File("bg/1s.gif").toURL()).getImage();
            map2Icon = new ImageIcon(new File("bg/2s.gif").toURL()).getImage();
            map3Icon = new ImageIcon(new File("bg/3s.gif").toURL()).getImage();

            map0 = new ImageIcon(new File("bg/0.gif").toURL()).getImage();
            map1 = new ImageIcon(new File("bg/1.gif").toURL()).getImage();
            map2 = new ImageIcon(new File("bg/2.gif").toURL()).getImage();
            map3 = new ImageIcon(new File("bg/3.gif").toURL()).getImage();

            // empty health bar
            ui[0] = ImageIO.read(new File("UI/titleScreen.png"));

            // load all character sprites
            for (int i = 0; i < p1.length; i++) {
                p1Retsu[i] = ImageIO.read(new File("Characters/Retsu/p1/" + i + ".png"));
                p2Retsu[i] = ImageIO.read(new File("Characters/Retsu/p2/" + i + ".png"));

                p1Ryu[i] = ImageIO.read(new File("Characters/Ryu/p1/" + i + ".png"));
                // p2Ryu[i] = ImageIO.read(new File("Characters/Ryu/p2" + i + ".png"));
            }

        } catch (IOException e) {
            System.out.println("Something Wrong");
        }

        JFrame frame = new JFrame("Game");
        Main panel = new Main();
        frame.add(panel);
        frame.setVisible(true);
        frame.pack();
    }

    public void paintComponent(Graphics g) {
        frameController++;
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientBackground = new GradientPaint(0, 0, Color.ORANGE, 0, 700, Color.RED);
        Color textColor = new Color(255, 165, 0);
        if (gameState == 0) {
            windowWidth = 800;

            g2d.setColor(Color.BLACK);
            // g2d.fillRect(0, 0, getWidth(), getHeight());
            g.drawImage(map0, 0, 0, this);
            g2d.setPaint(new GradientPaint(0, 0, Color.ORANGE, 0, 200, Color.RED));
            g2d.setFont(largeText);
            centerString(g2d, "Fighting Fury", "H" + logoHeight);
            g2d.setFont(text);
            g2d.setPaint(Color.WHITE);
            if (logoHeight == 100) {
                centerString(g2d, "PRESS ENTER TO CONTINUE", "H250");
                centerString(g2d, "PRESS ESC FOR SETTINGS", "H300");
                g.drawString("MADE BY: FAHAD", 550, 365);
            }

            // g.drawImage()

            // These are the characters that are shown in the main page
            // g.drawImage(p1[idle][p1Sprite], 100, p1PosY, null);
            // p1CurrentMovement = p1[idle][p1Sprite];
            // if (frameController % 8 == 0) {
            // p1Sprite = (p1Sprite + 1) % p1[idle].length;
            // }

            // g.drawImage(p1[idle][p2Sprite], 700 - p2[idle][p2Sprite].getWidth(), p2PosY,
            // null);
            // p2CurrentMovement = p2Idle[p2IdleSpriteNum];
            // if (frameController % 8 == 0) {
            // p2Sprite = (p2Sprite + 1) % p2[idle].length;
            // }

            logoHeight = logoHeight == 100 ? logoHeight : logoHeight + 2;
        } else if (gameState == 1) {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setFont(largeText);
            centerString(g2d, "CREDITS", "H85");

            g2d.setFont(text);
            g2d.setPaint(textColor);
            centerString(g2d, "Fahad Is The Creator Of The Game", "H150");
            centerString(g2d, "Mr. Chow Is His Teacher", "H200");
            centerString(g2d, "Street Fighter Is His Inspiration", "H250");
            centerString(g2d, "Press BACKSPACE to go back to main screen", "H340");

        }

        else if (gameState == 2) {
            g2d.setPaint(gradientBackground);
            g2d.setFont(largeText);
            g2d.fillRect(0, 0, 800, 367);
            g2d.setPaint(Color.WHITE);
            centerString(g2d, "SETTINGS", "H100");

            g2d.setFont(text);
            String gameSoundText = gameMusic ? "ON" : "OFF";
            String gameSFXText = gameSFX ? "ON" : "OFF";
            centerString(g2d, "GAME MUSIC: " + gameSoundText, "H250");
            centerString(g2d, "GAME SFX: " + gameSFXText, "H300");
        }

        else if (gameState == 3) {
            g2d.setPaint(gradientBackground);
            g2d.fillRect(0, 0, 800, 367);
            g2d.setFont(largeText);
            g2d.setPaint(Color.WHITE);
            centerString(g2d, "CHARACTER SELECT", "H100");
            g2d.setStroke(new BasicStroke(3));
            g2d.fillRect(getWidth() / 2 - 115, 200, 50, 50);
            g2d.fillRect(getWidth() / 2 - 55, 200, 50, 50);
            g2d.fillRect(getWidth() / 2 + 5, 200, 50, 50);
            g2d.fillRect(getWidth() / 2 + 65, 200, 50, 50);

            g2d.setPaint(Color.BLACK);

            g2d.drawRect(getWidth() / 2 - 115, 200, 50, 50);
            g2d.drawRect(getWidth() / 2 - 55, 200, 50, 50);
            g2d.drawRect(getWidth() / 2 + 5, 200, 50, 50);
            g2d.drawRect(getWidth() / 2 + 65, 200, 50, 50);

            g.drawImage(retsuPortrait, getWidth() / 2 - 110, 205, null);
            g.drawImage(ryuPortrait, getWidth() / 2 - 50, 205, null);
            g.drawImage(comingSoon, getWidth() / 2 + 10, 205, null);
            g.drawImage(comingSoon, getWidth() / 2 + 70, 205, null);

            // hovers

            if (!p1Char.equals("")) {
                g2d.setStroke(new BasicStroke(5));
                int moveAmount = 0;

                g2d.setPaint(new Color(171, 26, 219));
                moveAmount = p1Hover == 0 ? -115 : p1Hover == 1 ? -55 : p1Hover == 2 ? 5 : 65;
                g2d.drawRect(getWidth() / 2 + moveAmount, 200, 50, 50);
            }

            if (!p2Char.equals("")) {
                g2d.setStroke(new BasicStroke(5));
                int moveAmount = 0;

                g2d.setPaint(new Color(219, 26, 113));
                moveAmount = p2Hover == 0 ? -115 : p2Hover == 1 ? -55 : p2Hover == 2 ? 5 : 65;
                g2d.drawRect(getWidth() / 2 + moveAmount, 200, 50, 50);
            }

            if (hoverBlink) {
                g2d.setStroke(new BasicStroke(5));
                int moveAmount = 0;

                if (p1Char.equals("")) {
                    // p1 hover
                    g2d.setPaint(new Color(171, 26, 219));
                    moveAmount = p1Hover == 0 ? -115 : p1Hover == 1 ? -55 : p1Hover == 2 ? 5 : 65;
                    g2d.drawRect(getWidth() / 2 + moveAmount, 200, 50, 50);
                }

                if (p2Char.equals("")) {
                    // p2 hover
                    g2d.setPaint(new Color(219, 26, 113));
                    moveAmount = p2Hover == 0 ? -115 : p2Hover == 1 ? -55 : p2Hover == 2 ? 5 : 65;
                    g2d.drawRect(getWidth() / 2 + moveAmount, 200, 50, 50);
                }
                if (p1Hover == p2Hover) {
                    g2d.setPaint(new Color(81, 26, 219));
                    moveAmount = p1Hover == 0 ? -115 : p1Hover == 1 ? -55 : p1Hover == 2 ? 5 : 65;
                    g2d.drawRect(getWidth() / 2 + moveAmount, 200, 50, 50);
                }
            }

            // checks if players just switched their characters for animation reset
            if (p1CharSwitch) {
                p1CharSwitch = false;
                p1HoverCharMovement = -50;
            }
            if (p2CharSwitch) {
                p2CharSwitch = false;
                p2HoverCharMovement = 495;
            }

            g2d.setPaint(new Color(171, 26, 219));
            if (p1Hover == 0) {
                g.drawImage(p1retsuBig, p1HoverCharMovement, -10, null);
                g2d.drawString("RETSU", 50, -p1HoverCharMovement + 280);
                if (p1HoverCharMovement != -20) {
                    p1HoverCharMovement += 1;
                }
            } else if (p1Hover == 1) {
                g.drawImage(p1ryuBig, p1HoverCharMovement, 30, null);
                g2d.drawString("RYU", 50, -p1HoverCharMovement + 280);
                if (p1HoverCharMovement != -20) {
                    p1HoverCharMovement += 1;
                }
            } else {
                g.drawImage(comingSoonBig, p1HoverCharMovement, -10, null);
                g2d.drawString("SOON", 60, -p1HoverCharMovement + 280);
                if (p1HoverCharMovement != -20) {
                    p1HoverCharMovement += 1;
                }
            }

            g2d.setPaint(new Color(219, 26, 113));
            if (p2Hover == 0) {
                g.drawImage(p2retsuBig, p2HoverCharMovement, -10, null);
                g2d.drawString("RETSU", 550, p2HoverCharMovement - 165);
                if (p2HoverCharMovement != 465) {
                    p2HoverCharMovement -= 1;
                }
            } else if (p2Hover == 1) {
                g.drawImage(p2ryuBig, p2HoverCharMovement, 30, null);
                g2d.drawString("RYU", 630, p2HoverCharMovement - 165);
                if (p2HoverCharMovement != 465) {
                    p2HoverCharMovement -= 1;
                }
            } else {
                g.drawImage(comingSoonBig, p2HoverCharMovement, -10, null);
                g2d.drawString("SOON", 540, p2HoverCharMovement - 165);
                if (p2HoverCharMovement != 465) {
                    p2HoverCharMovement -= 1;
                }
            }

            if (frameController % 5 == 0) {
                hoverBlink = hoverBlink ? false : true;
            }

        }

        else if (gameState == 5) {
            g2d.setPaint(gradientBackground);
            g2d.fillRect(0, 0, 800, 367);
            g2d.setFont(largeText);
            g2d.setPaint(Color.WHITE);
            centerString(g2d, "MAP SELECT", "H100");

            g.drawImage(map0Icon, getWidth() / 2 - 222, 130, null);
            g.drawImage(map1Icon, getWidth() / 2 + 15, 130, null);
            g.drawImage(map2Icon, getWidth() / 2 - 222, 250, null);
            g.drawImage(map3Icon, getWidth() / 2 + 15, 250, null);

            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(5));
            g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
            g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
            g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
            g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);

            if (p1Map != -1 && p2Map != -1) {
                g2d.setPaint(new Color(81, 26, 219));
                if (chosenMapNum == 0) {
                    g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                    p1PosY = 240;
                    p2PosY = 240;
                    defaultPosY = 240;
                } else if (chosenMapNum == 1) {
                    g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                    p1PosY = 265;
                    p2PosY = 265;
                    defaultPosY = 265;
                } else if (chosenMapNum == 2) {
                    g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                    p1PosY = 250;
                    p2PosY = 250;
                    defaultPosY = 250;
                } else {
                    g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                    p1PosY = 250;
                    p2PosY = 250;
                    defaultPosY = 250;
                }
                gameState = 6;
            } else {
                if (p1Map != -1) {
                    g2d.setPaint(new Color(171, 26, 219));
                    if (p1Hover == 0) {
                        g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                    } else if (p1Hover == 1) {
                        g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                    } else if (p1Hover == 2) {
                        g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                    } else {
                        g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                    }
                }

                if (p2Map != -1) {
                    g2d.setPaint(new Color(219, 26, 113));
                    if (p2Hover == 0) {
                        g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                    } else if (p2Hover == 1) {
                        g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                    } else if (p2Hover == 2) {
                        g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                    } else {
                        g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                    }
                }

                if (hoverBlink) {

                    if (p1Map == -1) {
                        // p1 hover
                        g2d.setPaint(new Color(171, 26, 219));
                        if (p1Hover == 0) {
                            g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                        } else if (p1Hover == 1) {
                            g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                        } else if (p1Hover == 2) {
                            g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                        } else {
                            g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                        }
                    }

                    if (p2Map == -1) {
                        // p2 hover
                        g2d.setPaint(new Color(219, 26, 113));
                        if (p2Hover == 0) {
                            g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                        } else if (p2Hover == 1) {
                            g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                        } else if (p2Hover == 2) {
                            g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                        } else {
                            g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                        }
                    }

                    if (p1Hover == p2Hover) {
                        g2d.setPaint(new Color(81, 26, 219));
                        if (p1Hover == 0) {
                            g2d.drawRect(getWidth() / 2 - 222, 130, 207, 95);
                        } else if (p1Hover == 1) {
                            g2d.drawRect(getWidth() / 2 + 15, 130, 207, 95);
                        } else if (p1Hover == 2) {
                            g2d.drawRect(getWidth() / 2 - 222, 250, 207, 95);
                        } else {
                            g2d.drawRect(getWidth() / 2 + 15, 250, 207, 95);
                        }
                    }
                }

                if (frameController % 15 == 0) {
                    hoverBlink = hoverBlink ? false : true;
                }
            }

        } else if (gameState == 6) {

            g.drawImage(chosenMap, 0, 0, this);

            g2d.setColor(Color.WHITE);
            g2d.fillRect(19, 16, 304, 29);
            g2d.fillRect(getWidth() - 22 - 300, 16, 304, 29);

            g2d.setColor(Color.RED);
            g2d.fillRect(21, 18, 300, 25);
            g2d.fillRect(getWidth() - 21 - 300, 18, 300, 25);

            g2d.setColor(Color.YELLOW);
            g2d.fillRect(21, 18, p1Health * 3, 25);
            g2d.fillRect(getWidth() - 21 - p2Health * 3, 18, p2Health * 3, 25);

            g2d.setPaint(gradientBackground);
            g2d.fillRect(21, 52, p1Stamina * 3, 5);
            g2d.fillRect(getWidth() - 21 - p2Stamina * 3, 52, p2Stamina * 3, 5);

            g2d.setPaint(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));

            g2d.setPaint(gradientBackground);
            if (p1Score == 1) {
                g.fillOval(300, 70, 10, 10);
            }
            if (p2Score == 1) {
                g.fillOval(490, 70, 10, 10);
            }
            System.out.println(p1Score);

            g2d.setPaint(Color.WHITE);
            g.drawOval(300, 70, 10, 10);
            g.drawOval(490, 70, 10, 10);

            g.setFont(comboText);
            if (p1Combo > 0) {
                g.drawString("x" + p1Combo, 20, 250);
            }
            if (p2Combo > 0) {
                g.drawString("x" + p2Combo, 760, 250);
            }

            if (Integer.parseInt(startTimer) > -1) {
                g2d.setFont(largeText);
                if (!startTimer.equals("0")) {
                    centerString(g2d, startTimer, "B");
                }

                if (startTimer.equals("0")) {
                    centerString(g2d, "FIGHT!", "B");
                }

                if (frameController % 50 == 0) {
                    startTimer = "" + (Integer.parseInt(startTimer) - 1);
                }
                return;
            }

            if (p1Action[knockedOut])
                knockedOut(g, "p1");
            if (p1Action[hit] && !p1Action[knockedOut])
                hit(g, "p1");
            else if (p1Action[duck])
                duck(g, "p1");
            else if (p1Action[block])
                block(g, "p1");
            else if (p1Action[jump])
                jump(g, "p1");
            else if (p1Action[forward])
                moveForward(g, "p1");
            else if (p1Action[back])
                moveBack(g, "p1");
            else if (p1Action[punch])
                punch(g, "p1");
            else if (p1Action[kick])
                kick(g, "p1");
            else if (!p1Action[knockedOut])
                idle(g, "p1");
            if (p1Action[special])
                special(g, "p1");

            if (!p1Action[punch] && !p1Action[kick] && !p1Action[block] && frameController % 15 == 0) {
                p1Stamina = p1Stamina + 5 >= 100 ? 100 : p1Stamina + 5;
            }

            if (p2Action[knockedOut])
                knockedOut(g, "p2");
            else if (p2Action[hit])
                hit(g, "p2");
            else if (p2Action[duck])
                duck(g, "p2");
            else if (p2Action[block])
                block(g, "p2");
            else if (p2Action[jump])
                jump(g, "p2");
            else if (p2Action[forward])
                moveForward(g, "p2");
            else if (p2Action[back])
                moveBack(g, "p2");
            else if (p2Action[punch])
                punch(g, "p2");
            else if (p2Action[kick])
                kick(g, "p2");
            else if (!p2Action[knockedOut])
                idle(g, "p2");
            if (p2Action[special])
                special(g, "p2");

            if (!p2Action[punch] && !p2Action[kick] && !p2Action[block] && frameController % 15 == 0) {
                p2Stamina = p2Stamina + 5 >= 100 ? 100 : p2Stamina + 5;
            }

            // g2d.setPaint(Color.BLACK);
            // g2d.drawRect(p1PosX, p1PosY, p1CurrentMovement.getWidth(),
            // p1CurrentMovement.getHeight());
        }
        if (gameState == 7) {
            g2d.setPaint(gradientBackground);
            g.fillRect(0, 0, 800, 367);
            if (p1Char.equals("Retsu")) {
                g.drawImage(p1retsuBig, 200, 0, null);
            }
            g2d.setFont(largeText);
            g2d.setPaint(new Color(171, 26, 219));
            centerString(g2d, "PLAYER 1 WINS", "B");
            g2d.setFont(text);
            g2d.setPaint(Color.WHITE);
            centerString(g2d, "Press BACKSPACE For Main Menu", "H300");
            centerString(g2d, "Press ENTER To Restart With Same Settings", "H350");
        }

        if (gameState == 8) {
            g2d.setPaint(gradientBackground);
            g.fillRect(0, 0, 800, 367);
            if (p2Char.equals("Retsu")) {
                g.drawImage(p2retsuBig, 250, 0, null);
            }
            g2d.setFont(largeText);
            g2d.setPaint(new Color(219, 26, 113));
            centerString(g2d, "PLAYER 2 WINS", "B");
            g2d.setFont(text);
            g2d.setPaint(Color.WHITE);
            centerString(g2d, "Press BACKSPACE For Main Menu", "H300");
            centerString(g2d, "Press ENTER To Restart With Same Settings", "H350");

        }

        if (p2Health <= 0) {
            p2Action[knockedOut] = true;
        } else if (p1Health <= 0) {
            p1Action[knockedOut] = true;

        }
    }

    public void moveForward(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[forward][p1Sprite[forward]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[forward][p1Sprite[forward]];
            if (frameController % p1MoveLength[forward] == 0) {
                if (!checkForCollisions("p1", "walk").equals("Collision")) {
                    p1PosX += 5;
                } // checks for the possible collisions while moving forward
                p1Sprite[forward] = (p1Sprite[forward] + 1) % p1[forward].length;
            }
        } else {
            g.drawImage(p2[forward][p2Sprite[forward]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[forward][p2Sprite[forward]];
            if (frameController % p2MoveLength[forward] == 0) {
                if (!checkForCollisions("p2", "walk").equals("Out of bounds")) {
                    p2PosX += 5;
                } else {
                    p2PosX = windowWidth - p2CurrentMovement.getWidth();
                }
                p2Sprite[forward] = (p2Sprite[forward] + 1) % p2[forward].length;
            }
        }

    }

    public void moveBack(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[back][p1Sprite[back]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[back][p1Sprite[back]];
            if (frameController % p1MoveLength[back] == 0) {
                if (!checkForCollisions("p1", "walk").equals("Out of bounds")) {
                    p1PosX -= 5;
                } else {
                    p1PosX = 0;
                }
                p1Sprite[back] = (p1Sprite[back] + 1) % p1[back].length;
            }
        } else {
            g.drawImage(p2[back][p2Sprite[back]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[back][p2Sprite[back]];
            if (frameController % p2MoveLength[back] == 0) {
                if (!checkForCollisions("p2", "walk").equals("Collision")) {
                    p2PosX -= 5;
                } else {
                    p2PosX += 5;
                }
                p2Sprite[back] = (p2Sprite[back] + 1) % p2[back].length;
            }
        }
    }

    public void punch(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[punch][p1Sprite[punch]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[punch][p1Sprite[punch]];

            if (frameController % p1MoveLength[punch] == 0) {
                if (checkForCollisions("p1", "attack").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1PunchDamage < 0 ? 0 : p2Health - p1PunchDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;

                }
                p1Sprite[punch] = (p1Sprite[punch] + 1) % p1[punch].length;
                if (p1Sprite[punch] == 0) {
                    p1Action[punch] = false;
                    hasp1Hit = false;
                }
            }
        } else {
            g.drawImage(p2[punch][p2Sprite[punch]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[punch][p2Sprite[punch]];

            if (frameController % p2MoveLength[punch] == 0) {
                if (p2Char == "Retsu") {
                    // this is because character sprite moves when punching so this is to account
                    // for that movement for the p2
                    if (p2Sprite[punch] == 0)
                        p2PosX -= 35;
                    else if (p2Sprite[punch] == 1)
                        p2PosX += 35;
                }
                if (checkForCollisions("p2", "attack").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p2PunchDamage < 0 ? 0 : p1Health - p2PunchDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }
                p2Sprite[punch] = (p2Sprite[punch] + 1) % p2[punch].length;
                if (p2Sprite[punch] == 0) {
                    p2Action[punch] = false;
                    hasp2Hit = false;
                }
            }
        }

    }

    public void kick(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[kick][p1Sprite[kick]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[kick][p1Sprite[kick]];

            if (frameController % p1MoveLength[kick] == 0) {
                if (checkForCollisions("p1", "attack").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1KickDamage < 0 ? 0 : p2Health - p1KickDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;
                }
                p1Sprite[kick] = (p1Sprite[kick] + 1) % p1[kick].length;
                if (p1Sprite[kick] == 0) {
                    p1Action[kick] = false;
                    hasp1Hit = false;
                }
            }

        } else {
            g.drawImage(p2[kick][p2Sprite[kick]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[kick][p1Sprite[kick]];
            if (frameController % p2MoveLength[kick] == 0) {
                if (p2Char == "Retsu") {
                    // this is because character sprite moves when punching so this is to account
                    // for that movement for the p2
                    if (p2Sprite[kick] == 1)
                        p2PosX -= 35;
                    else if (p2Sprite[kick] == 2)
                        p2PosX += 35;
                }
                if (checkForCollisions("p2", "attack").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p2KickDamage < 0 ? 0 : p1Health - p2KickDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }
                p2Sprite[kick] = (p2Sprite[kick] + 1) % p2[kick].length;
                if (p2Sprite[kick] == 0) {
                    p2Action[kick] = false;
                    hasp2Hit = false;
                }
            }
        }
    }

    public void jump(Graphics g, String player) {
        if (player.equals("p1")) {
            if (p1Action[punch] && p1Sprite[jump] > 1
                    && (takenP1Stamina || p1Stamina >= jumppunchStamina)) {
                if (!takenP1Stamina) {
                    p1Stamina -= jumppunchStamina;
                    takenP1Stamina = true;
                }
                g.drawImage(p1[jumpPunch][0], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[jumpPunch][0];
                if (checkForCollisions("p1", "high").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1JumpPunchDamage < 0 ? 0 : p2Health - p1JumpPunchDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;
                }
            }

            else if (p1Action[kick] && p1Sprite[jump] > 1
                    && (takenP1Stamina || p1Stamina >= jumpKickStamina) && !keysPressed.contains(p1PunchBind)) {
                if (!takenP1Stamina) {
                    p1Stamina -= jumpKickStamina;
                    takenP1Stamina = true;
                }
                g.drawImage(p1[jumpKick][0], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[jumpKick][0];
                if (checkForCollisions("p1", "high").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1JumpKickDamage < 0 ? 0 : p2Health - p1JumpKickDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;
                }
            }

            else {
                g.drawImage(p1[jump][p1Sprite[jump]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[jump][p1Sprite[jump]];
            }

            if (frameController % p1MoveLength[jump] == 0) {
                if (keysPressed.contains(68) && !checkForCollisions("p1", "jump").equals("Collision")) {
                    p1PosX += 20;
                }
                if (keysPressed.contains(65) && !checkForCollisions("p1", "jump").equals("Out of bounds")) {
                    p1PosX -= 20;
                }
                if (p1Sprite[jump] + 1 > p1[jump].length / 2) {
                    p1PosY += 40;
                } else {
                    p1PosY -= 40;
                }
                p1Sprite[jump] = (p1Sprite[jump] + 1) % p1[jump].length;
                if (p1Sprite[jump] == 0) {
                    p1Action[jump] = false;
                    p1Action[kick] = false;
                    p1Action[punch] = false;
                    hasp1Hit = false;
                }
            }
        } else {
            if (p2Action[punch] && p2Sprite[jump] > 1
                    && (takenP2Stamina || p2Stamina >= jumppunchStamina)) {
                if (!takenP2Stamina) {
                    p2Stamina -= jumppunchStamina;
                    takenP2Stamina = true;
                }
                g.drawImage(p2[jumpPunch][0], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[jumpPunch][0];
                if (checkForCollisions("p2", "high").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p2JumpPunchDamage < 0 ? 0 : p1Health - p2JumpPunchDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }
            } else if (p2Action[kick] && p2Sprite[jump] > 1
                    && (takenP2Stamina || p2Stamina >= jumpKickStamina)) {
                if (!takenP2Stamina) {
                    p2Stamina -= jumpKickStamina;
                    takenP2Stamina = true;
                }
                g.drawImage(p2[jumpKick][0], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[jumpKick][0];
                if (checkForCollisions("p2", "high").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p1JumpKickDamage < 0 ? 0 : p1Health - p1JumpKickDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }
            } else {
                g.drawImage(p2[jump][p2Sprite[jump]], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[jump][p2Sprite[jump]];
            }

            if (frameController % p2MoveLength[jump] == 0) {
                if (keysPressed.contains(37) && !checkForCollisions("p2", "jump").equals("Collision")) {
                    p2PosX -= 20;
                }
                if (keysPressed.contains(39) && !checkForCollisions("p2", "jump").equals("Out of bounds")) {
                    p2PosX += 20;
                }
                if (p2Sprite[jump] + 1 > p2[jump].length / 2) {
                    p2PosY += 40;
                } else {
                    p2PosY -= 40;
                }
                p2Sprite[jump] = (p2Sprite[jump] + 1) % p2[jump].length;
                if (p2Sprite[jump] == 0) {
                    p2Action[jump] = false;
                    p2Action[punch] = false;
                    p2Action[kick] = false;
                    hasp2Hit = false;
                }
            }
        }
    }

    public void duck(Graphics g, String player) {
        if (player.equals("p1")) {
            if (p1Action[block]) {
                g.drawImage(p1[duckBlock][p1Sprite[duckBlock]], p1PosX, p1PosY - (p1Char.equals("Retsu") ? 15 : 0),
                        null);
                p1CurrentMovement = p1[duckBlock][p1Sprite[duckBlock]];
                p1Stamina--;
                if (frameController % p1MoveLength[duckBlock] == 0) {
                    p1Sprite[duckBlock] = (p1Sprite[duckBlock] + 1) % p1[duckBlock].length;
                }
                if (p1Stamina - 1 <= 0) {
                    p1Action[block] = false;
                }

            }

            else if (p1Action[punch] && !keysPressed.contains(p1KickBind)) {
                g.drawImage(p1[duckPunch][p1Sprite[duckPunch]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[duckPunch][p1Sprite[duckPunch]];
                if (checkForCollisions("p1", "low").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1PunchDamage < 0 ? 0 : p2Health - p1PunchDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;
                }

                if (frameController % p1MoveLength[duckPunch] == 0) {
                    p1Sprite[duckPunch] = (p1Sprite[duckPunch] + 1) % p1[duckPunch].length;

                    if (p1Sprite[duckPunch] == 0) {
                        takenP1Stamina = false;
                        hasp1Hit = false;
                        p1Action[punch] = false;
                        hasp1Hit = false;
                    }
                }

            }

            else if ((p1Action[kick])) {
                g.drawImage(p1[duckKick][p1Sprite[duckKick]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[duckKick][p1Sprite[duckKick]];
                if (checkForCollisions("p1", "low").equals("Collision") && !hasp1Hit) {
                    p2Health = p2Health - p1KickDamage < 0 ? 0 : p2Health - p1KickDamage;
                    p2Action[hit] = true;
                    hasp1Hit = true;
                    p1Combo++;
                    p2Combo = 0;
                }
                if (frameController % p1MoveLength[duckKick] == 0) {
                    p1Sprite[duckKick] = (p1Sprite[duckKick] + 1) % p1[duckKick].length;

                    if (p1Sprite[duckKick] == 0) {
                        takenP1Stamina = false;
                        hasp1Hit = false;
                        p1Action[kick] = false;
                        hasp1Hit = false;
                    }
                }

            }

            else {
                g.drawImage(p1[duck][p1Sprite[duck]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[duck][p1Sprite[duck]];
                takenP1Stamina = false;
                p1Sprite[duckPunch] = 0;
            }

            if (frameController % p1MoveLength[duck] == 0) {
                p1Sprite[duck] = (p1Sprite[duck] + 1) % p1[duck].length;
            }
        } else {
            if (p2Action[block]) {
                g.drawImage(p2[duckBlock][p2Sprite[duckBlock]], p2PosX - (p2Char.equals("Retsu") ? 15 : 0),
                        p2PosY - (p2Char.equals("Retsu") ? 15 : 0), null);
                p2CurrentMovement = p2[duckBlock][p2Sprite[duckBlock]];
                p2Stamina--;
                // isp2Block = true;
                if (frameController % p2MoveLength[duckBlock] == 0) {
                    p2Sprite[duckBlock] = (p2Sprite[duckBlock] + 1) % p2[duckBlock].length;
                }
                if (p2Stamina - 1 <= 0) {
                    p2Action[duckBlock] = false;
                }
            }

            else if (p2Action[punch] && !keysPressed.contains(p2KickBind)) {
                g.drawImage(p2[duckPunch][p2Sprite[duckPunch]], p2PosX, p2PosY,
                        null);
                p2CurrentMovement = p2[duckPunch][p2Sprite[duckPunch]];
                if (checkForCollisions("p2", "low").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p1PunchDamage < 0 ? 0 : p1Health - p1PunchDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }

                if (frameController % p2MoveLength[duckPunch] == 0) {
                    p2Sprite[duckPunch] = (p2Sprite[duckPunch] + 1) % p2[duckPunch].length;
                    if (p2Sprite[duckPunch] == 1 && p2Char.equals("Retsu")) {
                        p2PosX -= 20;
                    }
                    if (p2Sprite[duckPunch] == p2[duckPunch].length - 1 && p2Char.equals("Retsu")) {
                        p2PosX += 20;
                    }

                    if (p2Sprite[duckPunch] == 0) {
                        takenP2Stamina = false;
                        hasp2Hit = false;
                        p2Action[punch] = false;
                        hasp2Hit = false;
                    }
                }

            }

            else if (p2Action[kick]) {
                g.drawImage(p2[duckKick][p2Sprite[duckKick]], p2PosX, p2PosY,
                        null);
                p2CurrentMovement = p2[duckKick][p2Sprite[duckKick]];
                if (checkForCollisions("p2", "low").equals("Collision") && !hasp2Hit) {
                    p1Health = p1Health - p2KickDamage < 0 ? 0 : p1Health - p2KickDamage;
                    p1Action[hit] = true;
                    hasp2Hit = true;
                    p2Combo++;
                    p1Combo = 0;
                }
                if (frameController % p2MoveLength[duckKick] == 0) {
                    p2Sprite[duckKick] = (p2Sprite[duckKick] + 1) % p2[duckKick].length;
                    if (p2Char.equals("Retsu")) {
                        if (p2Sprite[duckKick] == 2) {
                            p2PosX -= 30;
                        }
                        if (p2Sprite[duckKick] == 3) {
                            p2PosX += 30;
                        }
                    }

                    if (p2Sprite[duckKick] == 0) {
                        takenP2Stamina = false;
                        hasp2Hit = false;
                        p2Action[kick] = false;
                        hasp2Hit = false;
                    }
                }
            }

            else {
                g.drawImage(p2[duck][p2Sprite[duck]], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[duck][p2Sprite[duck]];
                takenP2Stamina = false;
                p2Sprite[duckPunch] = 0;
            }

            if (frameController % p2MoveLength[duck] == 0) {
                p2Sprite[duck] = (p2Sprite[duck] + 1) % p2[duck].length;
            }
        }
    }

    public void hit(Graphics g, String player) {
        if (player.equals("p1")) {

            if (p1Action[jump]) {
                if (p1Sprite[jump] + 1 > p1[jump].length / 2) {
                    p1PosY += 40;
                } else {
                    p1PosY -= 40;
                }
                p1Sprite[jump] = (p1Sprite[jump] + 1) % p1[jump].length;
                if (p1Sprite[jump] == 0) {
                    p1Action[jump] = false;
                }
            }
            if (p2Combo % 5 == 0 && p2Combo > 0) {
                g.drawImage(p1[knocked][p1Sprite[knocked]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[knocked][p1Sprite[knocked]];

                if ((frameController % p1MoveLength[knocked] <= 0 && p1Sprite[knocked] != 2)
                        || frameController % 50 <= 0) {
                    p1Sprite[knocked] = (p1Sprite[knocked] + 1) % p1[knocked].length;
                    if (p1Sprite[knocked] == 0)
                        p1Action[hit] = false;
                    if (p1Sprite[knocked] < p1[knocked].length / 2) {
                        if (!checkForCollisions("p1", "walk").equals("Out of bounds")) {
                            if (p1Sprite[knocked] != 0)
                                p1PosX -= 40;
                        } else {
                            p1PosX = 0;
                        }

                        p1PosY = defaultPosY + 120 > p1PosY && p1Sprite[knocked] != 0 ? p1PosY + 30 : p1PosY;

                        if (p1PosY >= 367) {
                            p1PosY = 340;
                        }
                    }
                    if (p1Sprite[knocked] == p1[knocked].length - 2) {
                        p1PosY = defaultPosY;
                    }
                }
            } else {
                g.drawImage(p1[hit][p1Sprite[hit]], p1PosX, p1PosY, null);
                p1CurrentMovement = p1[hit][p1Sprite[hit]];

                if (frameController % p1MoveLength[hit] <= 0) {
                    p1Sprite[hit] = (p1Sprite[hit] + 1) % p1[hit].length;
                    if (p1Sprite[hit] == 0)
                        p1Action[hit] = false;
                }
            }
        } else {
            if (p2Action[jump]) {
                if (p2Sprite[jump] + 1 > p2[jump].length / 2) {
                    p2PosY += 40;
                } else {
                    p2PosY -= 40;
                }
                p2Sprite[jump] = (p2Sprite[jump] + 1) % p2[jump].length;

                if (p2Sprite[jump] == 0) {
                    p2Action[jump] = false;
                }
            }

            if (p1Combo % 5 == 0 && p1Combo > 0) {
                g.drawImage(p2[knocked][p2Sprite[knocked]], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[knocked][p2Sprite[knocked]];

                if ((frameController % p2MoveLength[knocked] == 0 && p2Sprite[knocked] != 2)
                        || frameController % 50 == 0) {
                    p2Sprite[knocked] = (p2Sprite[knocked] + 1) % p2[knocked].length;
                    if (p2Sprite[knocked] == 0)
                        p2Action[hit] = false;
                    if (p2Sprite[knocked] < p2[knocked].length / 2) {
                        if (p2Sprite[knocked] != 0)
                            p2PosX += 40;
                        if (checkForCollisions("p2", "walk").equals("Out of bounds") && p2Sprite[knocked] != 0) {
                            p2PosX = 800 - p2CurrentMovement.getWidth();
                        }
                        p2PosY = defaultPosY + 120 > p2PosY && p2Sprite[knocked] != 0 ? p2PosY + 30 : p2PosY;
                        if (p2PosY >= 300) {
                            p2PosY = 300;
                        }
                    }
                    if (p2Sprite[knocked] == p2[knocked].length - 2) {
                        p2PosY = defaultPosY;
                    }
                }
            } else {
                g.drawImage(p2[hit][p2Sprite[hit]], p2PosX, p2PosY, null);
                p2CurrentMovement = p2[hit][p2Sprite[hit]];

                if (frameController % p2MoveLength[hit] == 0) {
                    p2Sprite[hit] = (p2Sprite[hit] + 1) % p2[hit].length;
                    if (p2Sprite[hit] == 0)
                        p2Action[hit] = false;
                }
            }
        }
    }

    public void block(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[block][p1Sprite[block]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[block][p1Sprite[block]];
            p1Stamina -= 1;
            if (p1Stamina - blockStamina <= 0) {
                p1Sprite[block] = 0;
                p1Action[block] = false;
            }
        } else {
            g.drawImage(p2[block][p2Sprite[block]], p2PosX - (p2Char.equals("Retsu") ? 14 : 0), p2PosY, null);
            p2CurrentMovement = p2[block][p2Sprite[block]];
            p2Stamina -= 1;
            if (p2Stamina - blockStamina <= 0) {
                p2Sprite[block] = 0;
                p2Action[block] = false;
            }
        }
    }

    public void special(Graphics g, String player) {
        if (player.equals("p1")) {
            if (p1Char.equals("Retsu")) {
                g.drawImage(p1[special][p1Sprite[special]],
                        p1PosX + (p1PosX + 100 + p1[special][p1Sprite[special]].getWidth() < 800 ? 100
                                : p1[special][p1Sprite[special]].getWidth()),
                        defaultPosY - (p1Sprite[special] == 2 ? 20 : 0), null);
                p1CurrentMovement = p1[special][p1Sprite[special]];
            }

            if (checkForCollisions("p1", p1Char + "Special").equals("Collision") && !hasp1Hit) {
                p2Action[hit] = true;
                hasp1Hit = true;
                p2Health = p2Health - p1SpecialDamage < 0 ? 0 : p2Health - p1SpecialDamage;
                p1Combo++;
                p2Combo = 0;
            }
            if (frameController % p1MoveLength[special] == 0) {
                p1Sprite[special] = (p1Sprite[special] + 1) % p1[special].length;
                if (p1Sprite[special] == 1)
                    p1Stamina -= specialStamina;
                if (p1Sprite[special] == 0) {
                    p1Action[special] = false;
                    hasp1Hit = false;
                }

            }
        } else {
            if (p2Char.equals("Retsu")) {
                g.drawImage(p2[special][p2Sprite[special]],
                        p2PosX - (p2PosX - 100 - p2[special][p2Sprite[special]].getWidth() > 0 ? 100
                                : p2[special][p2Sprite[special]].getWidth()),
                        defaultPosY - (p2Sprite[special] == 2 ? 20 : 0), null);
                p2CurrentMovement = p2[special][p2Sprite[special]];
            }

            if (checkForCollisions("p2", p2Char + "Special").equals("Collision") && !hasp2Hit) {
                p1Action[hit] = true;
                hasp2Hit = true;
                p1Health = p1Health - p1SpecialDamage < 0 ? 0 : p1Health - p1SpecialDamage;
                p2Combo++;
                p1Combo = 0;
            }
            if (frameController % p2MoveLength[special] == 0) {
                p2Sprite[special] = (p2Sprite[special] + 1) % p2[special].length;
                if (p2Sprite[special] == 1)
                    p2Stamina -= specialStamina;
                if (p2Sprite[special] == 0) {
                    p2Action[special] = false;
                    hasp2Hit = false;
                }
            }
        }
    }

    public void knockedOut(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[knockedOut][p1Sprite[knockedOut]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[knockedOut][p1Sprite[knockedOut]];

            if (frameController % p1MoveLength[knockedOut] == 0) {
                p1Sprite[knockedOut] = (p1Sprite[knockedOut] + 1 == p1[knockedOut].length ? p1Sprite[knockedOut]
                        : p1Sprite[knockedOut] + 1);

                if (p1Char.equals("Retsu") && p1Sprite[knockedOut] <= 2) {
                    if (!checkForCollisions("p1", "walk").equals("Out of bounds")) {
                        p1PosX -= 40;
                    } else {
                        p1PosX = 0;
                    }
                    p1PosY = defaultPosY + 120 > p1PosY ? p1PosY + (300 - p1PosY) / 3 : p1PosY;
                    if (p1PosY >= 367) {
                        p1PosY = 340;
                    }
                    if (p1Sprite[knockedOut] == 2) {
                        p1PosY = 300;
                    }
                }
            }
            if (p1Sprite[knockedOut] == p1[knockedOut].length - 1) {
                g.setFont(largeText);
                centerString((Graphics2D) g, "P2 WIN!", "B");
                if (frameController % 300 == 0) {
                    if (p2Score == 1) {
                        gameState = 8;
                    } else {
                        reset("p2");
                    }
                }
            }
        } else {
            g.drawImage(p2[knockedOut][p2Sprite[knockedOut]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[knockedOut][p2Sprite[knockedOut]];

            if (frameController % p2MoveLength[knockedOut] == 0) {
                p2Sprite[knockedOut] = (p2Sprite[knockedOut] + 1 == p2[knockedOut].length ? p2Sprite[knockedOut]
                        : p2Sprite[knockedOut] + 1);
                if (p2Char.equals("Retsu") && p2Sprite[knockedOut] <= 2) {
                    if (!checkForCollisions("p2", "walk").equals("Out of bounds")) {
                        p2PosX += 40;
                    } else {
                        p2PosX = 800 - p2CurrentMovement.getWidth();
                    }
                    p2PosY = defaultPosY + 120 > p2PosY ? p2PosY + (340 - p2PosY) / 3 : p2PosY;
                    if (p2PosY >= 367) {
                        p2PosY = 340;
                    }
                    if (p2Sprite[knockedOut] == 2) {
                        p2PosY = 300;
                    }
                }
            }
            if (p2Sprite[knockedOut] == p2[knockedOut].length - 1) {
                g.setFont(largeText);
                centerString((Graphics2D) g, "P1 WIN!", "B");
                if (frameController % 300 == 0) {
                    if (p1Score == 1) {
                        gameState = 7;
                    } else {
                        reset("p1");
                    }
                }

            }
        }
    }

    public void idle(Graphics g, String player) {
        if (player.equals("p1")) {
            g.drawImage(p1[idle][p1Sprite[idle]], p1PosX, p1PosY, null);
            p1CurrentMovement = p1[idle][p1Sprite[idle]];
            if (frameController % p1MoveLength[idle] == 0) {
                p1Sprite[idle] = (p1Sprite[idle] + 1) % p1[idle].length;
            }
        } else {
            g.drawImage(p2[idle][p2Sprite[idle]], p2PosX, p2PosY, null);
            p2CurrentMovement = p2[idle][p2Sprite[idle]];
            if (frameController % p2MoveLength[idle] == 0) {
                p2Sprite[idle] = (p2Sprite[idle] + 1) % p2[idle].length;
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (!keysPressed.contains(e.getKeyCode())) {
            keysPressed.add(e.getKeyCode());
        }

        // System.out.println(e.getKeyCode());

        if (gameState == 0) {
            if (keysPressed.contains(27)) {
                gameState = 2;
            }
            if (keysPressed.contains(10)) { // 13 = ENTER
                gameState = 3;
            }
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                gameState = 1;
            }
        }

        else if (gameState == 1) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                gameState = 0;
            }
        }

        else if (gameState == 2) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                gameState = 0;
            }
        }

        else if (gameState == 3) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                gameState = 0;
            }
            // p1
            if (keysPressed.contains(68)) { // 68 = d
                p1Hover = (p1Hover + 1) % 4;
                p1CharSwitch = true;
            }
            if (keysPressed.contains(65)) { // 65 = a
                p1Hover = (p1Hover - 1) % 4;
                p1CharSwitch = true;
            }
            if (keysPressed.contains(69)) { // 69 = e
                p1Char = p1Hover == 0 ? "Retsu" : p1Hover == 1 ? "Ryu" : "";
                if (!p2Char.equals("")) {
                    loadSprites();
                    gameState = 4;
                    p1Hover = 0;
                    p2Hover = 0;
                }
            }

            // p2
            if (keysPressed.contains(39)) { // 39 = ->
                p2Hover = (p2Hover + 1) % 4;
                p2CharSwitch = true;
            }
            if (keysPressed.contains(37)) { // 37 = <-
                p2Hover = (p2Hover - 1) % 4;
                p2CharSwitch = true;
            }
            if (keysPressed.contains(47)) { // 47 = /
                p2Char = p2Hover == 0 ? "Retsu" : p2Hover == 1 ? "Ryu" : "";
                if (!p1Char.equals("")) {
                    loadSprites();
                    gameState = 4;
                    p1Hover = 0;
                    p2Hover = 0;
                }
            }
        }

        else if (gameState == 4) {
            if (keysPressed.contains(10)) { // 13 = ENTER
                gameState = 5;
            }

        }

        else if (gameState == 5) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                gameState = 0;
            }
            // p1
            if (keysPressed.contains(68)) { // 68 = d
                if (p1Hover == 0) {
                    p1Hover = 1;
                }
                if (p1Hover == 2) {
                    p1Hover = 3;
                }

                p1Map = -1;
            }
            if (keysPressed.contains(65)) { // 65 = a
                if (p1Hover == 1) {
                    p1Hover = 0;
                }
                if (p1Hover == 3) {
                    p1Hover = 2;
                }
                p1Map = -1;
            }
            if (keysPressed.contains(87)) { // 87 = w
                if (p1Hover == 2) {
                    p1Hover = 0;
                } else if (p1Hover == 3) {
                    p1Hover = 1;
                }
                p1Map = -1;
            }
            if (keysPressed.contains(83)) { // 83 = s
                if (p1Hover == 0) {
                    p1Hover = 2;
                } else if (p1Hover == 1) {
                    p1Hover = 3;
                }
                p1Map = -1;
            }
            if (keysPressed.contains(69)) { // 69 = e
                p1Map = p1Hover;
                if (p2Map != -1) {
                    Random rand = new Random();
                    int n = rand.nextInt(2);
                    if (n == 0) {
                        chosenMapNum = p1Map;
                    } else {
                        chosenMapNum = p2Map;
                    }
                    if (chosenMapNum == 0) {
                        chosenMap = map0;
                    } else if (chosenMapNum == 1) {
                        chosenMap = map1;
                    } else if (chosenMapNum == 2) {
                        chosenMap = map2;
                    } else {
                        chosenMap = map3;
                    }
                }
            }

            // p2
            if (keysPressed.contains(39)) { // 39 = ->
                if (p2Hover == 0) {
                    p2Hover = 1;
                } else if (p2Hover == 2) {
                    p2Hover = 3;
                }
                p2Map = -1;
            }
            if (keysPressed.contains(37)) { // 37 = <-
                if (p2Hover == 1) {
                    p2Hover = 0;
                } else if (p2Hover == 3) {
                    p2Hover = 2;
                }
                p2Map = -1;
            }
            if (keysPressed.contains(38)) { // 38 = ↑
                if (p2Hover == 2) {
                    p2Hover = 0;
                } else if (p2Hover == 3) {
                    p2Hover = 1;
                }
                p2Map = -1;
            }
            if (keysPressed.contains(40)) { // 40 == ↓
                if (p2Hover == 0) {
                    p2Hover = 2;
                } else if (p2Hover == 1) {
                    p2Hover = 3;
                }
                p2Map = -1;
            }
            if (keysPressed.contains(47)) { // 47 = /
                p2Map = p2Hover;
                if (p1Map != -1) {
                    Random rand = new Random();
                    int n = rand.nextInt(2);
                    if (n == 0) {
                        chosenMapNum = p1Map;
                    } else {
                        chosenMapNum = p2Map;
                    }
                    if (chosenMapNum == 0) {
                        chosenMap = map0;
                    } else if (chosenMapNum == 1) {
                        chosenMap = map1;
                    } else if (chosenMapNum == 2) {
                        chosenMap = map2;
                    } else {
                        chosenMap = map3;
                    }
                }
            }
        }

        else if (gameState == 6 && startTimer.equals("-1") && !p1Action[knockedOut] && !p2Action[knockedOut]) {

            // p1 movement
            if (keysPressed.contains(p1ForwardBind) && !p1Action[duck] && !p1Action[hit]) { // 68 = d
                p1Action[forward] = true;
            }
            if (keysPressed.contains(p1BackBind) && !p1Action[duck] && !p1Action[hit]) { // 65 = a
                p1Action[back] = true;
            }
            if (keysPressed.contains(p1JumpBind) && !p1Action[jump] && !p1Action[hit] && !p1Action[duck]
                    && !p1Action[knockedOut]) {
                p1Action[jump] = true;
            }
            if (keysPressed.contains(p1PunchBind) && !p1Action[hit]) { // 69 = e
                if (p1Stamina >= punchStamina && !p1Action[punch] && !p1Action[kick]) {
                    p1Stamina -= punchStamina;
                    p1Action[punch] = true;
                }

            }
            if (keysPressed.contains(p1KickBind) && !p1Action[hit]) { // 81 = q
                if (p1Stamina >= kickStamina && !p1Action[punch] && !p1Action[kick]) {
                    p1Stamina -= kickStamina;
                    p1Action[kick] = true;
                }
            }
            if (keysPressed.contains(p1DuckBind) && !p1Action[duck] && !p1Action[jump] && !p1Action[hit]) {
                p1Action[duck] = true;
                p1PosY += 30;
            }

            if (keysPressed.contains(p1BlockBind) && !p1Action[hit] && p1Stamina >= 30 && !p1Action[knockedOut]
                    && !p1Action[jump]) {
                p1Action[block] = true;
            }

            if (keysPressed.contains(p1SpecialBind) && !p1Action[hit] && p1Stamina >= specialStamina
                    && !p1Action[special]) {
                p1Action[special] = true;
            }

            // p2 movement
            if (keysPressed.contains(p2ForwardBind) && !p2Action[duck] && !p2Action[hit]) { // 39 = ->
                p2Action[forward] = true;
            }
            if (keysPressed.contains(p2BackBind) && !p2Action[duck] && !p2Action[hit]) { // 37 = <-
                p2Action[back] = true;
            }
            if (keysPressed.contains(p2JumpBind) && !p2Action[hit] && !p2Action[jump] && !p2Action[duck]) { // 38 = ↑
                p2Action[jump] = true;
            }
            if (keysPressed.contains(p2PunchBind) && !p2Action[hit]) { // 47 = /
                if (p2Stamina >= punchStamina && !p2Action[punch] && !p2Action[kick]) {
                    p2Stamina -= p2Action[punch] ? 0 : punchStamina;
                    p2Action[punch] = true;
                }
            }
            if (keysPressed.contains(p2KickBind) && !p2Action[hit]) { // 46 = .
                if (p2Stamina >= kickStamina && !p2Action[punch] && !p2Action[kick]) {
                    p2Stamina -= kickStamina;
                    p2Action[kick] = true;
                }
            }

            if (keysPressed.contains(p2DuckBind) && !p2Action[duck] && !p2Action[jump] && !p2Action[hit]) {
                p2Action[duck] = true;
                p2PosY += 30;
            }
            System.out.println(e.getKeyCode());
            if (keysPressed.contains(p2BlockBind) && !p2Action[hit] && p2Stamina >= 30 && !p2Action[knockedOut]
                    && !p2Action[jump]) {
                p2Action[block] = true;
            }
            if (keysPressed.contains(p2SpecialBind) && !p2Action[hit] && p2Stamina >= specialStamina
                    && !p2Action[special]) {
                p2Action[special] = true;
            }

        }

        else if (gameState == 7 || gameState == 8) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
                resetSprites("");
                gameState = 0;
                p1Health = 100;
                p2Health = 100;
                p1PosX = 100;
                p2PosX = 700;
                p1PosY = defaultPosY;
                p2PosY = defaultPosY;
                p1Char = "";
                p2Char = "";
                p1Hover = 0;
                p2Hover = 0;
                p1Map = -1;
                p2Map = -1;
                chosenMapNum = -1;
                defaultPosY = -1;
                p1HoverCharMovement = -50;
                p2HoverCharMovement = 495;
                p1CharSwitch = false;
                p2CharSwitch = false;
                p1Score = 0;
                p2Score = 0;
                p1Combo = 0;
                p2Combo = 0;
                startTimer = "3";
            }
            if (keysPressed.contains(10)) { // 13 = ENTER
                gameState = 6;
                p1Health = 100;
                p2Health = 100;
                p1PosX = 100;
                p2PosX = 700;
                p1Score = 0;
                p2Score = 0;
                startTimer = "3";
                p1Combo = 0;
                p2Combo = 0;
                resetSprites("");
            }
        }
        repaint();

    }

    public void keyReleased(KeyEvent e) {
        if (keysPressed.contains(e.getKeyCode())) {
            if (gameState == 6) {
                // p1
                if (e.getKeyCode() == 68) { // 68 = d
                    p1Action[forward] = false;
                }
                if (e.getKeyCode() == 65) { // 65 = a
                    p1Action[back] = false;
                }
                if (e.getKeyCode() == p1DuckBind && p1Action[duck] && p1PosY != defaultPosY && !p1Action[jump]) {
                    p1Action[duck] = false;
                    p1PosY -= 30;
                    p1Sprite[duck] = 0;
                }

                if (e.getKeyCode() == p1BlockBind) {
                    p1Action[block] = false;
                }

                // p2
                if (e.getKeyCode() == 39) { // 39 = ->
                    p2Action[forward] = false;
                }

                if (e.getKeyCode() == 37) { // 37 = <-
                    p2Action[back] = false;
                }

                if (e.getKeyCode() == p2DuckBind && p2Action[duck] && p2PosY != defaultPosY && !p2Action[jump]) {
                    p2Action[duck] = false;
                    p2PosY -= 30;
                }
                if (e.getKeyCode() == p2BlockBind) {
                    p2Action[block] = false;
                }

            }
            keysPressed.remove(keysPressed.indexOf(e.getKeyCode()));
        }
        repaint();
    }

    public static BufferedImage test(BufferedImage img) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int color = img.getRGB(x, y);
                int alpha = (color >> 24) & 0xFF;
                if (alpha != 0) {
                    // This sets the edges to the current coordinate
                    // if this pixel lies outside the current boundaries
                    minX = Math.min(x, minX);
                    maxX = Math.max(x, maxX);
                    minY = Math.min(y, minY);
                    maxY = Math.max(y, maxY);
                }
            }
        }
        BufferedImage result = new BufferedImage(maxX - minX, maxY - minY, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < maxX - minX; x++) {
            for (int y = 0; y < maxY - minY; y++) {
                result.setRGB(x, y, img.getRGB(x + minX, y + minY));
            }
        }
        return result;
    }

    public static String checkForCollisions(String playerToCheck, String move) {
        // getting points for both characters
        int p1Right = p1PosX + p1CurrentMovement.getWidth();
        int p1Top = p1PosY;
        int p1Bottom = p1PosY + p1CurrentMovement.getHeight();
        int[] p1TopLeft = { p1PosX, p1PosY };
        int[] p1TopRight = { p1PosX + p1CurrentMovement.getWidth(), p1PosY };
        int[] p1BottomLeft = { p1PosX, p1PosY + p1CurrentMovement.getHeight() };
        int[] p1BottomRight = { p1PosX + p1CurrentMovement.getWidth(), p1PosY + p1CurrentMovement.getHeight() };

        int p2Left = p2PosX;
        int p2Right = p2PosX + p2CurrentMovement.getWidth();
        int p2Top = p2PosY;
        int p2Bottom = p2PosY + p2CurrentMovement.getHeight();
        int[] p2TopLeft = { p2PosX, p2PosY };
        int[] p2BottomLeft = { p2PosX, p2PosY + p2CurrentMovement.getHeight() };
        // checks for the collisions
        if (playerToCheck.equals("p1")) {
            if (p2Action[block])
                return "";
            if (move.equals("high")) {
                if (p1Right >= p2Left && p1Top + (p1CurrentMovement.getHeight() / 2) >= p2Top) {
                    return "Collision";
                }
            }

            else if (move.equals("low")) {
                if (p1Right >= p2Left && p1Top + (p1CurrentMovement.getHeight() / 2) <= p2Bottom) {
                    return "Collision";
                }
            }

            else if (move.equals("attack")) {
                if (p1TopRight[0] >= p2TopLeft[0] || p1BottomRight[0] >= p2BottomLeft[0]) {
                    return "Collision";
                }
            }

            else if (move.equals("RetsuSpecial")) {
                if (p1Right + 100 > p2Left && p1Right + 100 <= p2Left + p2CurrentMovement.getWidth()) {
                    return "Collision";
                }
            }

            else {
                // all p1 false cases
                if (p1TopLeft[0] < 0 || p1BottomLeft[0] < 0) {
                    return "Out of bounds";
                }
                if (p1TopRight[0] >= p2TopLeft[0] || p1BottomRight[0] >= p2BottomLeft[0]) {
                    return "Collision";
                }
                return "";
            }

        } else {
            if (p1Action[block])
                return "";
            if (move.equals("high")) {
                if (p2Left <= p1Right && p2Top + (p2CurrentMovement.getHeight() / 2) >= p1Top) {
                    return "Collision";
                }
            } else if (move.equals("low")) {
                if (p2Left <= p1Right && p2Top + (p2CurrentMovement.getHeight() / 2) <= p1Bottom) {
                    return "Collision";
                }
            }

            else if (move.equals("attack")) {
                if (p2TopLeft[0] <= p1TopRight[0] || p2BottomLeft[0] <= p1BottomRight[0]) {
                    return "Collision";
                }
            }

            else if (move.equals("RetsuSpecial")) {
                int clonePositionX = p2Left - 100 - p2CurrentMovement.getWidth() > 0 ? 100
                        : p2CurrentMovement.getWidth();
                if (p2Left - clonePositionX < p1Right
                        && p2Left - clonePositionX >= p1Right - p1CurrentMovement.getWidth()) {
                    return "Collision";
                }
            }

            else {
                if (p2Right > 800) {
                    return "Out of bounds";
                }

                if (p2TopLeft[0] <= p1TopRight[0] || p2BottomLeft[0] <= p1BottomRight[0]) {
                    return "Collision";
                }
                return "";
            }

        }
        return "";
    }

    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }
    }

    public void centerString(Graphics2D g2d, String str, String type) {
        FontMetrics fm = g2d.getFontMetrics();
        int x = ((getWidth() - fm.stringWidth(str)) / 2);
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

        if (type.charAt(0) == 'B') { // if it is both, then it fully centers it
            g2d.drawString(str, x, y);
        } else if (type.charAt(0) == 'H') { // if it is horizontal only, it centers horizontally
            g2d.drawString(str, x, Integer.parseInt(type.substring(1)));
        } else { // last case scenario is only vertical center
            g2d.drawString(str, Integer.parseInt(type.substring(1)), y);
        }

    }

    public void reset(String winner) {
        p1Health = 100;
        p2Health = 100;
        p1PosX = 100;
        p1PosY = defaultPosY;
        p2PosX = 700;
        p2PosY = defaultPosY;
        resetSprites("");
        gameState = 6;
        p1Combo = 0;
        p2Combo = 0;
        startTimer = "3";
        if (winner.equals("p1")) {
            p1Score++;
        }
        if (winner.equals("p2")) {
            p2Score++;
        }
    }

    public static void resetSprites(String whos) {
        for (int i = 0; i < p1Sprite.length; i++) {
            p1Sprite[i] = 0;
            p2Sprite[i] = 0;
            p1Action[i] = false;
            p2Action[i] = false;
        }
    }

    public void loadSprites() {
        try {
             BufferedImage[] p1Character = new BufferedImage[totalMoves]; 
             BufferedImage[] p2Character = new BufferedImage[totalMoves]; 

            if (p1Char.equals("Retsu")) p1Character = p1Retsu;
            if (p2Char.equals("Retsu")) p2Character = p2Retsu;

            if (p1Char.equals("Ryu")) p1Character = p1Ryu;
            // if (p2Char.equals("Ryu")) p2Character = p2Ryu;

                for (int i = 0; i < totalMoves; i++) {
                    p1[i] = new BufferedImage[p1Character[i].getWidth() / 200];
                    for (int x = 0; x < p1[i].length; x++) {
                        p1Sprite[i] = x % p1[i].length;
                        p1[i][x] = grabImage(p1Retsu[i], p1Sprite[i]);
                        p1Sprite[i] = 0;
                        p1[i][x] = test(p1[i][x]);
                    }
                }
                for (int i = 0; i < totalMoves; i++) {
                    p2[i] = new BufferedImage[p2Character[i].getWidth() / 200];
                    for (int x = 0; x < p2[i].length; x++) {
                        p2Sprite[i] = x % p2[i].length;
                        p2[i][x] = grabImage(p2Retsu[i], p2Sprite[i]);
                        p2Sprite[i] = 0;
                        p2[i][x] = test(p2[i][x]);
                    }
                }
        } catch (Exception e) {
            System.out.println("something wrong");
        }

        if (p1Char.equals("Retsu")) {
            p1MoveLength[forward] = 3;
            p1MoveLength[back] = 3;
            p1MoveLength[punch] = 10;
            p1MoveLength[kick] = 10;
            p1MoveLength[jump] = 7;
            p1MoveLength[duckBlock] = 48;
            p1MoveLength[duckPunch] = 10;
            p1MoveLength[duckKick] = 10;
            p1MoveLength[duck] = 48;
            p1MoveLength[knocked] = 15;
            p1MoveLength[hit] = 5;
            p1MoveLength[special] = 10;
            p1MoveLength[knockedOut] = 10;
            p1MoveLength[idle] = 8;
        }
        if (p2Char.equals("Retsu")) {
            p2MoveLength[forward] = 3;
            p2MoveLength[back] = 3;
            p2MoveLength[punch] = 10;
            p2MoveLength[kick] = 10;
            p2MoveLength[jump] = 7;
            p2MoveLength[duckBlock] = 48;
            p2MoveLength[duckPunch] = 10;
            p2MoveLength[duckKick] = 10;
            p2MoveLength[duck] = 48;
            p2MoveLength[knocked] = 15;
            p2MoveLength[hit] = 5;
            p2MoveLength[special] = 10;
            p2MoveLength[knockedOut] = 10;
            p2MoveLength[idle] = 8;
        }
    }
}
