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
    public static BufferedImage retsuPortrait;

    public static BufferedImage comingSoon, comingSoonBig;

    // map select related

    public static Image map0Icon, map1Icon, map2Icon, map3Icon;
    public static Image map0, map1, map2, map3;
    public static Image chosenMap;
    public static int chosenMapNum = -1;

    public static int defaultPosY = 0;
    // p1 variables
    public static BufferedImage[] p1 = new BufferedImage[16];
    public static String p1Char = "";
    public static BufferedImage[] p1WalkForward, p1WalkBackward, p1Idle, p1LightPunch, p1Kick, p1Jump, p1JumpKick,
            p1JumpPunch, p1Duck, p1LowPunch, p1LowKick, p1Block, p1DuckBlock, p1Hit, p1Knocked, p1Special;
    public static boolean isp1Forward, isp1Backward, isp1LightPunch, isp1Kick, isp1Jump, isp1Duck, isp1LowPunch,
            isp1LowKick, isp1Block, isp1DuckBlock, isp1Hit, isp1Knocked, isp1Special = false;
    public static int p1SpriteNum, p1IdleSpriteNum, p1LightPunchSpriteNum, p1KickSpriteNum, p1JumpSpriteNum,
            p1DuckSpriteNum, p1LowPunchSpriteNum, p1LowKickSpriteNum, p1BlockSpriteNum, p1DuckBlockSpriteNum, p1HitSpriteNum, p1KnockedSpriteNum, p1SpecialSpriteNum = 0;
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

    public static int p1KickBind = 86;
    public static int p2KickBind = 46;
    public static int p1PunchBind = 67;
    public static int p2PunchBind = 47;
    public static int p1DuckBind = 83;
    public static int p2DuckBind = 40;
    public static int p1BlockBind = 88;
    public static int p2BlockBind = 16;
    public static int p2Combo = 0;
    public static int p1SpecialBind = 90;
    public static int p2SpecialBind = 44;

    public static String startTimer = "3";

    // p2 variables
    public static BufferedImage[] p2 = new BufferedImage[16];
    public static String p2Char = "";
    public static BufferedImage[] p2WalkForward, p2WalkBackward, p2Idle, p2LightPunch, p2Kick, p2Jump, p2JumpKick,
            p2JumpPunch, p2Duck, p2LowPunch, p2LowKick, p2Block, p2DuckBlock, p2Hit, p2Knocked, p2Special;
    public static boolean isp2Forward, isp2Backward, isp2LightPunch, isp2Kick, isp2Jump, isp2Duck, isp2LowPunch,
            isp2LowKick, isp2Block, isp2DuckBlock, isp2Hit, isp2Knocked, isp2Special = false;
    public static int p2SpriteNum, p2IdleSpriteNum, p2LightPunchSpriteNum, p2KickSpriteNum, p2JumpSpriteNum,
            p2DuckSpriteNum, p2LowPunchSpriteNum, p2LowKickSpriteNum, p2BlockSpriteNum, p2HitSpriteNum, p2DuckBlockSpriteNum, p2KnockedSpriteNum, p2SpecialSpriteNum = 0;
    public static BufferedImage p2CurrentMovement;
    // public static BufferedImage[] p2Hit;
    // public static boolean isp2Hit = false;
    // public static int p2HitSpriteNum = 0;
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
    public static int lightPunchStamina = 15;
    public static int kickStamina = 30;
    public static int jumpKickStamina = 40;
    public static int jumpPunchStamina = 30;
    public static int blockStamina = 5;

    public static ArrayList<Integer> keysPressed = new ArrayList();

    public Main() {
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        addKeyListener(this);
        Thread t = new Thread(this);
        t.start();
    }

    public static void main(String[] args) {
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

            p1retsuBig = ImageIO.read(new File("Characters/Retsu/p1/retsuBig.png"));

            p2retsuBig = ImageIO.read(new File("Characters/Retsu/p2/retsuBig.png"));

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

            // p1/p2 related

            // forward movement
            p1[0] = ImageIO.read(new File("Characters/Retsu/p1/ForwardWalking.png"));

            // backward movement
            p1[1] = ImageIO.read(new File("Characters/Retsu/p1/BackwardWalking.png"));

            // idle movement
            p1[2] = ImageIO.read(new File("Characters/Retsu/p1/idle.png"));

            // light punch
            p1[3] = ImageIO.read(new File("Characters/Retsu/p1/lightPunch.png"));

            // kick
            p1[4] = ImageIO.read(new File("Characters/Retsu/p1/kick.png"));

            // jump
            p1[5] = ImageIO.read(new File("Characters/Retsu/p1/jump.png"));

            // jump kick
            p1[6] = ImageIO.read(new File("Characters/Retsu/p1/jumpKick.png"));

            // jump punch
            p1[7] = ImageIO.read(new File("Characters/Retsu/p1/jumpPunch.png"));

            // duck
            p1[8] = ImageIO.read(new File("Characters/Retsu/p1/duck.png"));

            // low punch
            p1[9] = ImageIO.read(new File("Characters/Retsu/p1/lowPunch.png"));

            // low kick
            p1[10] = ImageIO.read(new File("Characters/Retsu/p1/lowKick.png"));

            // low kick
            p1[11] = ImageIO.read(new File("Characters/Retsu/p1/block.png"));

            p1[12] = ImageIO.read(new File("Characters/Retsu/p1/duckBlock.png"));

            // hit
            p1[13] = ImageIO.read(new File("Characters/Retsu/p1/hit.png"));

            // knocked
            p1[14] = ImageIO.read(new File("Characters/Retsu/p1/knocked.png"));

            // special 
            p1[15] = ImageIO.read(new File("Characters/Retsu/p1/special.png"));



            // forward movement
            p2[0] = ImageIO.read(new File("Characters/Retsu/p2/ForwardWalking.png"));

            // backward movement
            p2[1] = ImageIO.read(new File("Characters/Retsu/p2/BackwardWalking.png"));

            // idle movement
            p2[2] = ImageIO.read(new File("Characters/Retsu/p2/idle.png"));

            // light punch
            p2[3] = ImageIO.read(new File("Characters/Retsu/p2/lightPunch.png"));

            // kick
            p2[4] = ImageIO.read(new File("Characters/Retsu/p2/kick.png"));

            // jump
            p2[5] = ImageIO.read(new File("Characters/Retsu/p2/jump.png"));

            // jump kick
            p2[6] = ImageIO.read(new File("Characters/Retsu/p2/jumpKick.png"));

            // jump punch
            p2[7] = ImageIO.read(new File("Characters/Retsu/p2/jumpPunch.png"));

            // duck
            p2[8] = ImageIO.read(new File("Characters/Retsu/p2/duck.png"));

            // low punch
            p2[9] = ImageIO.read(new File("Characters/Retsu/p2/lowPunch.png"));

            // low kick
            p2[10] = ImageIO.read(new File("Characters/Retsu/p2/lowKick.png"));

            // block
            p2[11] = ImageIO.read(new File("Characters/Retsu/p2/block.png"));

            // duck block
            p2[12] = ImageIO.read(new File("Characters/Retsu/p2/duckBlock.png"));

            // hit
            p2[13] = ImageIO.read(new File("Characters/Retsu/p2/hit.png"));

            // knocked
            p2[14] = ImageIO.read(new File("Characters/Retsu/p2/knocked.png"));

            // special
            p2[15] = ImageIO.read(new File("Characters/Retsu/p2/special.png"));

            // finds the amount of sprites and makes that many indexes for each array;
            p1WalkForward = new BufferedImage[p1[0].getWidth() / 200];

            p1WalkBackward = new BufferedImage[p1[1].getWidth() / 200];

            p1Idle = new BufferedImage[p1[2].getWidth() / 200];

            p1LightPunch = new BufferedImage[p1[3].getWidth() / 200];

            p1Kick = new BufferedImage[p1[4].getWidth() / 200];

            p1Jump = new BufferedImage[p1[5].getWidth() / 200];

            p1JumpKick = new BufferedImage[p1[6].getWidth() / 200];

            p1JumpPunch = new BufferedImage[p1[7].getWidth() / 200];

            p1Duck = new BufferedImage[p1[8].getWidth() / 200];

            p1LowPunch = new BufferedImage[p1[9].getWidth() / 200];

            p1LowKick = new BufferedImage[p1[10].getWidth() / 200];

            p1Block = new BufferedImage[p1[11].getWidth() / 200];

            p1DuckBlock = new BufferedImage[p1[12].getWidth() / 200];

            p1Hit = new BufferedImage[p1[13].getWidth() / 200];

            p1Knocked = new BufferedImage[p1[14].getWidth() / 200];

            p1Special = new BufferedImage[p1[15].getWidth() / 200];


            // finds the amount of sprites and makes that many indexes for each array;
            p2WalkForward = new BufferedImage[p2[0].getWidth() / 200];

            p2WalkBackward = new BufferedImage[p2[1].getWidth() / 200];

            p2Idle = new BufferedImage[p2[2].getWidth() / 200];

            p2LightPunch = new BufferedImage[p2[3].getWidth() / 200];

            p2Kick = new BufferedImage[p2[4].getWidth() / 200];

            p2Jump = new BufferedImage[p2[5].getWidth() / 200];

            p2JumpKick = new BufferedImage[p2[6].getWidth() / 200];

            p2JumpPunch = new BufferedImage[p2[7].getWidth() / 200];

            p2Duck = new BufferedImage[p2[8].getWidth() / 200];

            p2LowPunch = new BufferedImage[p2[9].getWidth() / 200];

            p2LowKick = new BufferedImage[p2[10].getWidth() / 200];

            p2Block = new BufferedImage[p2[11].getWidth() / 200];

            p2DuckBlock = new BufferedImage[p2[12].getWidth() / 200];

            p2Hit = new BufferedImage[p2[13].getWidth() / 200];

            p2Knocked = new BufferedImage[p2[14].getWidth() / 200];

            p2Special = new BufferedImage[p2[15].getWidth() / 200];
            

            // loading all the forward walking sprites
            for (int i = 0; i < p1WalkForward.length; i++) {
                p1SpriteNum = i % p1WalkForward.length;
                p1WalkForward[i] = grabImage(p1[0], p1SpriteNum);
            }

            // loading all the backward walking sprites
            for (int i = 0; i < p1WalkBackward.length; i++) {
                p1SpriteNum = i % p1WalkBackward.length;
                p1WalkBackward[i] = grabImage(p1[1], p1SpriteNum);
            }

            // loading all the idle sprites
            for (int i = 0; i < p1Idle.length; i++) {
                p1IdleSpriteNum = i % p1Idle.length;
                p1Idle[i] = grabImage(p1[2], p1IdleSpriteNum);
            }

            // loading all the light punch sprites
            for (int i = 0; i < p1LightPunch.length; i++) {
                p1LightPunchSpriteNum = i % p1LightPunch.length;
                p1LightPunch[i] = grabImage(p1[3], p1LightPunchSpriteNum);
            }

            // loading all the kick sprites
            for (int i = 0; i < p1Kick.length; i++) {
                p1KickSpriteNum = (i) % p1Kick.length;
                p1Kick[i] = grabImage(p1[4], p1KickSpriteNum);
            }

            // loading all the jumping sprites
            for (int i = 0; i < p1Jump.length; i++) {
                p1JumpSpriteNum = (i) % p1Jump.length;
                p1Jump[i] = grabImage(p1[5], p1JumpSpriteNum);
            }

            p1JumpKick[0] = grabImage(p1[6], 0);

            p1JumpPunch[0] = grabImage(p1[7], 0);

            // loading all the ducking sprites
            for (int i = 0; i < p1Duck.length; i++) {
                p1DuckSpriteNum = (i) % p1Duck.length;
                p1Duck[i] = grabImage(p1[8], p1DuckSpriteNum);
            }

            // loading all the low punching sprites
            for (int i = 0; i < p1LowPunch.length; i++) {
                p1LowPunchSpriteNum = (i) % p1LowPunch.length;
                p1LowPunch[i] = grabImage(p1[9], p1LowPunchSpriteNum);
            }

            // loading all the low kick sprites
            for (int i = 0; i < p1LowKick.length; i++) {
                p1LowKickSpriteNum = (i) % p1LowKick.length;
                p1LowKick[i] = grabImage(p1[10], p1LowKickSpriteNum);
            }

            // loading all the block sprites
            for (int i = 0; i < p1Block.length; i++) {
                p1BlockSpriteNum = (i) % p1Block.length;
                p1Block[i] = grabImage(p1[11], p1BlockSpriteNum);
            }

            // loading all the duckblock sprites
            for (int i = 0; i < p1DuckBlock.length; i++) {
                p1DuckBlockSpriteNum = (i) % p1DuckBlock.length;
                p1DuckBlock[i] = grabImage(p1[12], p1DuckBlockSpriteNum);
            }

            // loading all the hit sprites
            for (int i = 0; i < p1Hit.length; i++) {
                p1HitSpriteNum = (i) % p1Hit.length;
                p1Hit[i] = grabImage(p1[13], p1HitSpriteNum);
            }

            // loading all the knocked sprites
            for (int i = 0; i < p1Knocked.length; i++) {
                p1KnockedSpriteNum = (i) % p1Knocked.length;
                p1Knocked[i] = grabImage(p1[14], p1KnockedSpriteNum);
            }

            // loading all the special sprites
            for (int i = 0; i < p1Special.length; i++) {
                p1SpecialSpriteNum = (i) % p1Special.length;
                p1Special[i] = grabImage(p1[15], p1SpecialSpriteNum);
            }

            resetSprites("p1");

            // loading all the forward walking sprites
            for (int i = 0; i < p2WalkForward.length; i++) {
                p2SpriteNum = i % p2WalkForward.length;
                p2WalkForward[i] = grabImage(p2[0], p2SpriteNum);
            }

            // loading all the backward walking sprites
            for (int i = 0; i < p2WalkBackward.length; i++) {
                p2SpriteNum = i % p2WalkBackward.length;
                p2WalkBackward[i] = grabImage(p2[1], p2SpriteNum);
            }

            // loading all the idle sprites
            for (int i = 0; i < p2Idle.length; i++) {
                p2IdleSpriteNum = i % p2Idle.length;
                p2Idle[i] = grabImage(p2[2], p2IdleSpriteNum);
            }

            // loading all the light punch sprites
            for (int i = 0; i < p2LightPunch.length; i++) {
                p2LightPunchSpriteNum = i % p2LightPunch.length;
                p2LightPunch[i] = grabImage(p2[3], p2LightPunchSpriteNum);
            }

            // loading all the kick sprites
            for (int i = 0; i < p2Kick.length; i++) {
                p2KickSpriteNum = i % p2Kick.length;
                p2Kick[i] = grabImage(p2[4], p2KickSpriteNum);
            }

            // loading all the jumping sprites
            for (int i = 0; i < p2Jump.length; i++) {
                p2JumpSpriteNum = i % p2Jump.length;
                p2Jump[i] = grabImage(p2[5], p2JumpSpriteNum);
            }

            p2JumpKick[0] = grabImage(p2[6], 0);

            p2JumpPunch[0] = grabImage(p2[7], 0);

            // loading all the ducking sprites
            for (int i = 0; i < p2Duck.length; i++) {
                p2DuckSpriteNum = (i) % p2Duck.length;
                p2Duck[i] = grabImage(p2[8], p2DuckSpriteNum);
            }

            // loading all the low punching sprites
            for (int i = 0; i < p2LowPunch.length; i++) {
                p2LowPunchSpriteNum = (i) % p2LowPunch.length;
                p2LowPunch[i] = grabImage(p2[9], p2LowPunchSpriteNum);
            }

            // loading all the low kick sprites
            for (int i = 0; i < p2LowKick.length; i++) {
                p2LowKickSpriteNum = (i) % p2LowKick.length;
                p2LowKick[i] = grabImage(p2[10], p2LowKickSpriteNum);
            }

            // loading all the block sprites
            for (int i = 0; i < p2Block.length; i++) {
                p2BlockSpriteNum = (i) % p2Block.length;
                p2Block[i] = grabImage(p2[11], p2BlockSpriteNum);
            }

            // loading all the duckblock sprites
            for (int i = 0; i < p2DuckBlock.length; i++) {
                p2DuckBlockSpriteNum = (i) % p2DuckBlock.length;
                p2DuckBlock[i] = grabImage(p2[12], p2DuckBlockSpriteNum);
            }

            // loading all the hit sprites
            for (int i = 0; i < p2Hit.length; i++) {
                p2HitSpriteNum = (i) % p2Hit.length;
                p2Hit[i] = grabImage(p2[13], p2HitSpriteNum);
            }

            // loading all the knocked sprites
            for (int i = 0; i < p2Knocked.length; i++) {
                p2KnockedSpriteNum = (i) % p2Knocked.length;
                p2Knocked[i] = grabImage(p2[14], p2KnockedSpriteNum);
            }

            // loading all the special sprites
            for (int i = 0; i < p2Special.length; i++) {
                p2SpecialSpriteNum = (i) % p2Special.length;
                p2Special[i] = grabImage(p2[15], p2SpecialSpriteNum);
            }

            resetSprites("p2");

        } catch (Exception e) {
            System.out.println("Something wrong with the image!");
        }

        // getting rid of the extra borders for all movements
        for (int i = 0; i < p1WalkForward.length; i++) {
            p1WalkForward[i] = test(p1WalkForward[i]);
        }
        for (int i = 0; i < p1WalkBackward.length; i++) {
            p1WalkBackward[i] = test(p1WalkBackward[i]);
        }
        for (int i = 0; i < p1Idle.length; i++) {
            p1Idle[i] = test(p1Idle[i]);
        }
        for (int i = 0; i < p1LightPunch.length; i++) {
            p1LightPunch[i] = test(p1LightPunch[i]);
        }
        for (int i = 0; i < p1Kick.length; i++) {
            p1Kick[i] = test(p1Kick[i]);
        }
        for (int i = 0; i < p1Jump.length; i++) {
            p1Jump[i] = test(p1Jump[i]);
        }
        p1JumpKick[0] = test(p1JumpKick[0]);
        p1JumpPunch[0] = test(p1JumpPunch[0]);

        for (int i = 0; i < p1Duck.length; i++) {
            p1Duck[i] = test(p1Duck[i]);
        }
        for (int i = 0; i < p1LowPunch.length; i++) {
            p1LowPunch[i] = test(p1LowPunch[i]);
        }
        for (int i = 0; i < p1LowKick.length; i++) {
            p1LowKick[i] = test(p1LowKick[i]);
        }
        for (int i = 0; i < p1Block.length; i++) {
            p1Block[i] = test(p1Block[i]);
        }
        for (int i = 0; i < p1DuckBlock.length; i++) {
            p1DuckBlock[i] = test(p1DuckBlock[i]);
        }
        for (int i = 0; i < p1Hit.length; i++) {
            p1Hit[i] = test(p1Hit[i]);
        }
        for (int i = 0; i < p1Knocked.length; i++) {
            p1Knocked[i] = test(p1Knocked[i]);
        }
        for (int i = 0; i < p1Special.length; i++) {
            p1Special[i] = test(p1Special[i]);
        }

        for (int i = 0; i < p2WalkForward.length; i++) {
            p2WalkForward[i] = test(p2WalkForward[i]);
        }
        for (int i = 0; i < p2WalkBackward.length; i++) {
            p2WalkBackward[i] = test(p2WalkBackward[i]);
        }
        for (int i = 0; i < p2Idle.length; i++) {
            p2Idle[i] = test(p2Idle[i]);
        }
        for (int i = 0; i < p2LightPunch.length; i++) {
            p2LightPunch[i] = test(p2LightPunch[i]);
        }
        for (int i = 0; i < p2Kick.length; i++) {
            p2Kick[i] = test(p2Kick[i]);
        }
        for (int i = 0; i < p2Jump.length; i++) {
            p2Jump[i] = test(p2Jump[i]);
        }

        p2JumpKick[0] = test(p2JumpKick[0]);
        p2JumpPunch[0] = test(p2JumpPunch[0]);

        for (int i = 0; i < p2Duck.length; i++) {
            p2Duck[i] = test(p2Duck[i]);
        }
        for (int i = 0; i < p2LowPunch.length; i++) {
            p2LowPunch[i] = test(p2LowPunch[i]);
        }
        for (int i = 0; i < p2LowKick.length; i++) {
            p2LowKick[i] = test(p2LowKick[i]);
        }
        for (int i = 0; i < p2Block.length; i++) {
            p2Block[i] = test(p2Block[i]);
        }
        for (int i = 0; i < p2DuckBlock.length; i++) {
            p2DuckBlock[i] = test(p2DuckBlock[i]);
        }
        for (int i = 0; i < p2Hit.length; i++) {
            p2Hit[i] = test(p2Hit[i]);
        }
        for (int i = 0; i < p2Knocked.length; i++) {
            p2Knocked[i] = test(p2Knocked[i]);
        }
        for (int i = 0; i < p2Special.length; i++) {
            p2Special[i] = test(p2Special[i]);
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
            g.drawImage(p1Idle[p1IdleSpriteNum], 100, p1PosY, null);
            p1CurrentMovement = p1Idle[p1IdleSpriteNum];
            if (frameController % 8 == 0) {
                p1IdleSpriteNum = (p1IdleSpriteNum + 1) % p1Idle.length;
            }

            g.drawImage(p2Idle[p2IdleSpriteNum], 700 - p2Idle[p2IdleSpriteNum].getWidth(), p2PosY, null);
            p2CurrentMovement = p2Idle[p2IdleSpriteNum];
            if (frameController % 8 == 0) {
                p2IdleSpriteNum = (p2IdleSpriteNum + 1) % (p2Idle.length);
            }

            logoHeight = logoHeight == 100 ? logoHeight : logoHeight + 2;
        }

        else if (gameState == 1) {
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
            g.drawImage(comingSoon, getWidth() / 2 - 50, 205, null);
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
            } else {
                g.drawImage(comingSoonBig, p2HoverCharMovement, -10, null);
                g2d.drawString("SOON", 540, p2HoverCharMovement - 165);
                if (p2HoverCharMovement != 465) {
                    p2HoverCharMovement -= 1;
                }
            }

            if (frameController % 15 == 0) {
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
                g.drawOval(490, 70, 10, 10);
            }

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

            if (isp1Hit) {

                if (isp1Jump) {
                    if (p1JumpSpriteNum + 1 > p1Jump.length / 2) {
                        p1PosY += 40;
                    } else {
                        p1PosY -= 40;
                    }
                    p1JumpSpriteNum++;
                    if (p1JumpSpriteNum == p1Jump.length) {isp1Jump = false; p1JumpSpriteNum = 0;}
                }
                if (p2Combo % 5 == 0 && p2Combo > 0) {
                    g.drawImage(p1Knocked[p1KnockedSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1Knocked[p1KnockedSpriteNum];

                    if ((frameController % (50 / p1Knocked.length) <= 0 && p1KnockedSpriteNum != 2) || frameController % 20 <= 0 ) {
                        p1KnockedSpriteNum++;
                        if (p1KnockedSpriteNum < p1Knocked.length / 2) {
                            if (!checkForCollisions("p1", "walk").equals("Out of bounds")) {
                                p1PosX -= 40;
                            } else {
                                p1PosX = 0;
                            }

                            p1PosY = defaultPosY + 120 > p1PosY ? p1PosY+30 : p1PosY;
                            if (p1PosY >= 367) {p1PosY = 340;}
                        } 
                        if (p1KnockedSpriteNum == p1Knocked.length - 2) {
                            p1PosY = defaultPosY;
                        }
                    }
                } 
                else {
                    g.drawImage(p1Hit[p1HitSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1Hit[p1HitSpriteNum];

                    if (frameController % (20 / p1Hit.length) <= 0) {
                        p1HitSpriteNum++;
                    }
                }

            }
            else if (isp1Block && p1Stamina >= blockStamina && !isp1Duck) {
                g.drawImage(p1Block[p1BlockSpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1Block[p1BlockSpriteNum];
                p1Stamina -= 1;

                if (p1Stamina - blockStamina <= 0) {
                    p1BlockSpriteNum = 0;
                    isp1Block = false;
                }
            }

            else if (isp1Forward && !isp1Jump && !isp1Duck) {
                g.drawImage(p1WalkForward[p1SpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1WalkForward[p1SpriteNum];
                if (frameController % (40 / p1WalkForward.length) == 0) {
                    if (!checkForCollisions("p1", "walk").equals("Collision")) {
                        p1PosX += 5;
                    } // checks for the possible collisions while moving forward
                    p1SpriteNum++;
                }

            } else if (isp1Backward && !isp1Jump && !isp1Duck) {
                g.drawImage(p1WalkBackward[p1SpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1WalkBackward[p1SpriteNum];
                if (frameController % (40 / p1WalkBackward.length) <= 0) {
                    if (!checkForCollisions("p1", "walk").equals("Out of bounds")) {
                        p1PosX -= 5;
                    } else {
                        p1PosX = 0;
                    }
                    p1SpriteNum++;
                }
            }

            else if (isp1Jump) {
                if (keysPressed.contains(p1PunchBind) && p1JumpSpriteNum > 1
                        && (takenP1Stamina || p1Stamina >= jumpPunchStamina)) {
                    if (!takenP1Stamina) {
                        p1Stamina -= jumpPunchStamina;
                        takenP1Stamina = true;
                    }
                    g.drawImage(p1JumpPunch[0], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1JumpPunch[0];
                    if (checkForCollisions("p1", "high").equals("Collision") && !hasp1Hit) {
                        p2Health -= 7;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;
                    }
                }

                else if (keysPressed.contains(p1KickBind) && p1JumpSpriteNum > 1
                        && (takenP1Stamina || p1Stamina >= jumpKickStamina) && !keysPressed.contains(p1PunchBind)) {
                    if (!takenP1Stamina) {
                        p1Stamina -= jumpKickStamina;
                        takenP1Stamina = true;
                    }
                    g.drawImage(p1JumpKick[0], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1JumpKick[0];
                    if (checkForCollisions("p1", "high").equals("Collision") && !hasp1Hit) {
                        p2Health -= 10;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;
                    }
                }

                else {
                    g.drawImage(p1Jump[p1JumpSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1Jump[p1JumpSpriteNum];
                }

                if (frameController % (40 / p1Jump.length) == 0) {
                    if (keysPressed.contains(68) && !checkForCollisions("p1", "jump").equals("Collision")) {
                        p1PosX += 20;
                    }
                    if (keysPressed.contains(65) && !checkForCollisions("p1", "jump").equals("Out of bounds")) {
                        p1PosX -= 20;
                    }
                    if (p1JumpSpriteNum + 1 > p1Jump.length / 2) {
                        p1PosY += 40;
                    } else {
                        p1PosY -= 40;
                    }
                    p1JumpSpriteNum++;
                }
            }

            else if (isp1Duck) {
                if (keysPressed.contains(p1BlockBind) && p1Stamina >= 1) {
                    g.drawImage(p1DuckBlock[p1DuckBlockSpriteNum], p1PosX, p1PosY - (p1Char.equals("Retsu") ? 15 : 0), null);
                    p1CurrentMovement = p1DuckBlock[p1DuckBlockSpriteNum];
                    isp1Block = true;
                    p1Stamina--;
                    if (frameController % (48 / p1DuckBlock.length) == 0) {
                        p1DuckBlockSpriteNum = (p1DuckBlockSpriteNum + 1) % p1DuckBlock.length;
                    }
                    if (p1Stamina - 1 <= 0) { isp1Block = false; }

                }

                else if (isp1LowPunch && !keysPressed.contains(p1KickBind)) {
                    g.drawImage(p1LowPunch[p1LowPunchSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1LowPunch[p1LowPunchSpriteNum];
                    if (checkForCollisions("p1", "low").equals("Collision") && !hasp1Hit) {
                        p2Health = 0;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;
                    }

                    if (frameController % (24 / p1LowPunch.length) == 0) {
                        p1LowPunchSpriteNum = (p1LowPunchSpriteNum + 1) % p1LowPunch.length;

                        if (p1LowPunchSpriteNum == 0) {
                            takenP1Stamina = false;
                            hasp1Hit = false;
                            isp1LowPunch = false;
                        }
                    }

                }

                else if ((isp1LowKick) && !keysPressed.contains(p1PunchBind)) {
                    g.drawImage(p1LowKick[p1LowKickSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1LowKick[p1LowKickSpriteNum];
                    if (checkForCollisions("p1", "low").equals("Collision") && !hasp1Hit) {
                        p2Health -= 7;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;
                    }
                    if (frameController % (48 / p1LowKick.length) == 0) {
                        p1LowKickSpriteNum = (p1LowKickSpriteNum + 1) % p1LowKick.length;

                        if (p1LowKickSpriteNum == 0) {
                            takenP1Stamina = false;
                            hasp1Hit = false;
                            isp1LowKick = false;
                        }
                    }

                }

                else {
                    g.drawImage(p1Duck[p1DuckSpriteNum], p1PosX, p1PosY, null);
                    p1CurrentMovement = p1Duck[p1DuckSpriteNum];
                    takenP1Stamina = false;
                    p1LowPunchSpriteNum = 0;
                }

                if (frameController % (48 / p1Duck.length) == 0) {
                    p1DuckSpriteNum = (p1DuckSpriteNum + 1) % p1Duck.length;
                }
            }

            else if (isp1LightPunch) {
                g.drawImage(p1LightPunch[p1LightPunchSpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1LightPunch[p1LightPunchSpriteNum];

                if (frameController % (24 / p1LightPunch.length) == 0) {
                    if (checkForCollisions("p1", "attack").equals("Collision") && !hasp1Hit) {
                        p2Health -= 5;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;

                    }
                    p1LightPunchSpriteNum++;
                }
            }

            else if (isp1Kick) {
                g.drawImage(p1Kick[p1KickSpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1Kick[p1KickSpriteNum];

                if (frameController % (48 / p1Kick.length) == 0) {
                    if (checkForCollisions("p1", "attack").equals("Collision") && !hasp1Hit) {
                        p2Health -= 7;
                        isp2Hit = true;
                        hasp1Hit = true;
                        p1Combo++;
                        p2Combo = 0;
                    }
                    p1KickSpriteNum++;
                }
            }

            else {
                g.drawImage(p1Idle[p1IdleSpriteNum], p1PosX, p1PosY, null);
                p1CurrentMovement = p1Idle[p1IdleSpriteNum];
                if (frameController % 8 == 0) {
                    p1IdleSpriteNum = (p1IdleSpriteNum + 1) % p1Idle.length;
                }
            }

            if (isp1Special) {
                g.drawImage(p1Special[p1SpecialSpriteNum], p1PosX+(p1PosX+100+p1Special[p1SpecialSpriteNum].getWidth() < 800 ? 100 : 800-p1Special[p1SpecialSpriteNum].getWidth() ), p1PosY, null);
                p1CurrentMovement = p1Idle[p1IdleSpriteNum];
            }

            if ((p1SpriteNum == p1WalkForward.length) || // checks if walking sprite animation is finished
                    (p1LightPunchSpriteNum == p1LightPunch.length) || // checks if punching sprite animation is finished
                    (p1KickSpriteNum == p1Kick.length) || // checks if kicking animation is done
                    (p1HitSpriteNum == p1Hit.length) || 
                    (p1JumpSpriteNum == p1Jump.length) || 
                    (p1KnockedSpriteNum == p1Knocked.length)

            ) {
                resetSprites("p1");
            }

            if (!isp1LightPunch && !isp1Kick & !keysPressed.contains(p1PunchBind) && !keysPressed.contains(p1KickBind)
                    && frameController % 15 == 0) {
                p1Stamina = p1Stamina + 5 >= 100 ? 100 : p1Stamina + 5;
            }

            if (isp2Hit) {
                if (isp2Jump) {
                    if (p2JumpSpriteNum + 1 > p2Jump.length / 2) {
                        p2PosY += 40;
                    } else {
                        p2PosY -= 40;
                    }
                    p2JumpSpriteNum++;
                    if (p2JumpSpriteNum == p2Jump.length) {isp2Jump = false; p2JumpSpriteNum = 0;}
                }

                if (p1Combo % 5 == 0 && p1Combo > 0) {
                    g.drawImage(p2Knocked[p2KnockedSpriteNum], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2Knocked[p2KnockedSpriteNum];

                    if ((frameController % (50 / p2Knocked.length) <= 0 && p2KnockedSpriteNum != 2) || frameController % 50 == 0 ) {
                        p2KnockedSpriteNum++;
                        if (p2KnockedSpriteNum < p2Knocked.length / 2) {
                            p2PosX +=40;
                            if (checkForCollisions("p2", "walk").equals("Out of bounds")) {
                                p2PosX = 800 - p2CurrentMovement.getWidth();
                            }
                            p2PosY = defaultPosY + 120 > p2PosY ? p2PosY+30 : p2PosY;
                            if (p2PosY >= 300) {p2PosY = 300;}
                        } 
                        if (p2KnockedSpriteNum == p2Knocked.length - 2) {
                            p2PosY = defaultPosY;
                        }
                    }
                }
                else {
                    g.drawImage(p2Hit[p2HitSpriteNum], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2Hit[p2HitSpriteNum];

                    if (frameController % (20 / p2Hit.length) <= 0) {
                        p2HitSpriteNum++;
                    }
                    System.out.println(p2HitSpriteNum);
                }
            }

            else if (isp2Block && p2Stamina >= blockStamina && !isp2Duck) {
                g.drawImage(p2Block[p2BlockSpriteNum], p2PosX - (p2Char.equals("Retsu") ? 14 : 0), p2PosY, null);
                p2Stamina -= 1;

                if (p2Stamina - blockStamina <= 0) {
                    p2BlockSpriteNum = 0;
                    isp2Block = false;
                }
            }

            else if (isp2Forward && !isp2Jump && !isp2Duck) {
                g.drawImage(p2WalkForward[p2SpriteNum], p2PosX, p2PosY, null);
                p2CurrentMovement = p2WalkForward[p2SpriteNum];
                if (frameController % (40 / p2WalkForward.length) == 0) {
                    if (!checkForCollisions("p2", "walk").equals("Out of bounds")) {
                        p2PosX += 5;
                    } else {
                        p2PosX = windowWidth - p2CurrentMovement.getWidth();
                    }
                    p2SpriteNum++;
                }
            }

            else if (isp2Backward && !isp2Jump && !isp2Duck) {
                g.drawImage(p2WalkBackward[p2SpriteNum], p2PosX, p2PosY, null);
                p2CurrentMovement = p2WalkBackward[p2SpriteNum];
                if (frameController % (40 / p2WalkBackward.length) == 0) {
                    if (!checkForCollisions("p2", "walk").equals("Collision")) {
                        p2PosX -= 5;
                    } else {
                        p2PosX +=5;
                    }
                    p2SpriteNum++;
                }
            }

            else if (isp2Jump) {
                if (keysPressed.contains(p2PunchBind) && p2JumpSpriteNum > 1
                        && (takenP2Stamina || p2Stamina >= jumpPunchStamina)) {
                    if (!takenP2Stamina) {
                        p2Stamina -= jumpPunchStamina;
                        takenP2Stamina = true;
                    }
                    g.drawImage(p2JumpPunch[0], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2JumpPunch[0];
                    if (checkForCollisions("p2", "high").equals("Collision") && !hasp2Hit) {
                        p1Health -= 7;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }
                } else if (keysPressed.contains(p2KickBind) && p2JumpSpriteNum > 1
                        && (takenP2Stamina || p2Stamina >= jumpKickStamina)) { 
                    if (!takenP2Stamina) {
                        p2Stamina -= jumpKickStamina;
                        takenP2Stamina = true;
                    }
                    g.drawImage(p2JumpKick[0], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2JumpKick[0];
                    if (checkForCollisions("p2", "high").equals("Collision") && !hasp2Hit) {
                        p1Health -= 10;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }
                } else {
                    g.drawImage(p2Jump[p2JumpSpriteNum], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2Jump[p2JumpSpriteNum];
                }

                if (frameController % (40 / p2Jump.length) == 0) {
                    if (keysPressed.contains(37) && !checkForCollisions("p2", "jump").equals("Collision")) {
                        p2PosX -= 20;
                    }
                    if (keysPressed.contains(39) && !checkForCollisions("p2", "jump").equals("Out of bounds")) {
                        p2PosX += 20;
                    }
                    if (p2JumpSpriteNum + 1 > p2Jump.length / 2) {
                        p2PosY += 40;
                    } else {
                        p2PosY -= 40;
                    }
                    p2JumpSpriteNum++;
                }

            }

            else if (isp2Duck) {
                if (keysPressed.contains(p2BlockBind) && p2Stamina >= 1) {
                    g.drawImage(p2DuckBlock[p2DuckBlockSpriteNum], p2PosX - (p2Char.equals("Retsu") ? 15 : 0), p2PosY - (p2Char.equals("Retsu") ? 15 : 0), null);
                    p2CurrentMovement = p2DuckBlock[p2DuckBlockSpriteNum];
                    p2Stamina--;
                    isp2Block = true;
                    if (frameController % (48 / p2DuckBlock.length) == 0) {
                        p2DuckBlockSpriteNum = (p2DuckBlockSpriteNum + 1) % p2DuckBlock.length;
                    }
                    if (p2Stamina - 1 <= 0) { isp2Block = false;}

                }

                else if (isp2LowPunch && !keysPressed.contains(p2KickBind)) {
                    g.drawImage(p2LowPunch[p2LowPunchSpriteNum], p2PosX - (p2Char.equals("Retsu") ? 20 : 0), p2PosY,
                            null);
                    p2CurrentMovement = p2LowPunch[0];
                    if (checkForCollisions("p2", "low").equals("Collision") && !hasp2Hit) {
                        p1Health = 0;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }

                    if (frameController % (24 / p2LowPunch.length) == 0) {
                        p2LowPunchSpriteNum = (p2LowPunchSpriteNum + 1) % p2LowPunch.length;

                        if (p2LowPunchSpriteNum == 0) {
                            takenP2Stamina = false;
                            hasp2Hit = false;
                            isp2LowPunch = false;
                        }
                    }

                }

                else if (isp2LowKick && !keysPressed.contains(p2PunchBind)) {
                    g.drawImage(p2LowKick[p2LowKickSpriteNum], p2PosX, p2PosY,
                            null);
                    p2CurrentMovement = p2LowKick[p2LowKickSpriteNum];
                    if (checkForCollisions("p2", "low").equals("Collision") && !hasp2Hit) {
                        p1Health -= 7;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }
                    if (frameController % (48 / p2LowKick.length) == 0) {
                        p2LowKickSpriteNum = (p2LowKickSpriteNum + 1) % p2LowKick.length;
                    if (p2Char.equals("Retsu")) { 
                        if (p2LowKickSpriteNum==2) {p2PosX -=30;}
                        if (p2LowKickSpriteNum==3) {p2PosX +=30;}
                    }

                        if (p2LowKickSpriteNum == 0) {
                            takenP2Stamina = false;
                            hasp2Hit = false;
                            isp2LowKick = false;
                        }
                    }

                }

                else {
                    g.drawImage(p2Duck[p2DuckSpriteNum], p2PosX, p2PosY, null);
                    p2CurrentMovement = p2Duck[p2DuckSpriteNum];
                    takenP2Stamina = false;
                    p2LowPunchSpriteNum = 0;
                }

                if (frameController % (48 / p2Duck.length) == 0) {
                    p2DuckSpriteNum = (p2DuckSpriteNum + 1) % p2Duck.length;
                }
            }

            else if (isp2LightPunch) {
                g.drawImage(p2LightPunch[p2LightPunchSpriteNum], p2PosX, p2PosY, null);
                p2CurrentMovement = p2LightPunch[p2LightPunchSpriteNum];

                if (frameController % (24 / p2LightPunch.length) == 0) {
                    if (p2Char == "Retsu") {
                        // this is because character sprite moves when punching so this is to account
                        // for that movement for the p2
                        if (p2LightPunchSpriteNum == 0)
                            p2PosX -= 35;
                        else if (p2LightPunchSpriteNum == 1)
                            p2PosX += 35;
                    }
                    if (checkForCollisions("p2", "attack").equals("Collision") && !hasp2Hit) {
                        p1Health -= 5;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }
                    p2LightPunchSpriteNum++;
                }
            }

            else if (isp2Kick) {
                g.drawImage(p2Kick[p2KickSpriteNum], p2PosX, p2PosY, null);
                p2CurrentMovement = p2Kick[p2KickSpriteNum];
                if (frameController % (48 / p2Kick.length) == 0) {
                    if (p2Char == "Retsu") {
                        // this is because character sprite moves when punching so this is to account
                        // for that movement for the p2
                        if (p2KickSpriteNum == 1)
                            p2PosX -= 35;
                        else if (p2KickSpriteNum == 2)
                            p2PosX += 35;
                    }
                    if (checkForCollisions("p2", "attack").equals("Collision") && !hasp2Hit) {
                        p1Health -= 7;
                        isp1Hit = true;
                        hasp2Hit = true;
                        p2Combo++;
                        p1Combo = 0;
                    }
                    p2KickSpriteNum++;
                }
            }

            else {
                g.drawImage(p2Idle[p2IdleSpriteNum], p2PosX, p2PosY, null);
                p2CurrentMovement = p2Idle[p2IdleSpriteNum];
                if (frameController % 8 == 0) {
                    p2IdleSpriteNum = (p2IdleSpriteNum + 1) % (p2Idle.length);
                }
            }

            if (!isp2LightPunch && !isp2Kick && !keysPressed.contains(p2PunchBind) && !keysPressed.contains(p2KickBind)
                    && frameController % 15 == 0) {
                p2Stamina = p2Stamina + 5 >= 100 ? 100 : p2Stamina + 5;
            }

            if ((p2SpriteNum == p2WalkForward.length) || // checks if walking sprite animation is finished
                    (p2LightPunchSpriteNum == p2LightPunch.length) || // checks if punching sprite animation is finished
                    (p2KickSpriteNum == p2Kick.length) || // checks if kicking animation is done
                    (p2HitSpriteNum == p2Hit.length) || 
                    (p2JumpSpriteNum == p2Jump.length) || 
                    (p2KnockedSpriteNum == p2Knocked.length)

            ) {
                resetSprites("p2");
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
            if (p1Score < 2) {
                System.out.println("p1 good");
                reset("p1");
            }
            else if (p1Score == 2){
                gameState = 7;
            }
        } else if (p1Health <= 0) {
            if (p2Score < 2) {
                reset("p2");
            } 
            else if (p2Score == 2) {
                gameState = 8;
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
                p1Char = p1Hover == 0 ? "Retsu" : "";
                if (!p2Char.equals("")) {
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
                p2Char = p2Hover == 0 ? "Retsu" : "";
                if (!p1Char.equals("")) {
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

        else if (gameState == 6 && startTimer.equals("-1")) {
            // p1 movement
            if (keysPressed.contains(68)) { // 68 = d
                isp1Forward = true;
            }
            if (keysPressed.contains(65)) { // 65 = a
                isp1Backward = true;
            }
            if (keysPressed.contains(87)) { // 87 = w
                if (!isp1Duck) {
                    isp1Jump = true;
                }
            }
            if (keysPressed.contains(83)) { // 83 = s
            }
            if (keysPressed.contains(p1PunchBind) && !isp1Hit) { // 69 = e
                // isp1LightPunch = isp1Hit ? false : true;
                if (p1Stamina >= lightPunchStamina && !isp1Kick && !isp1Jump && !isp1Duck) {
                    p1Stamina -= isp1LightPunch ? 0 : lightPunchStamina;
                    isp1LightPunch = true;
                } 
                else if (p1Stamina >= lightPunchStamina && isp1Duck) {
                    p1Stamina -= isp1LowPunch ? 0 : lightPunchStamina;
                    isp1LowPunch = true;
                }

            }
            if (keysPressed.contains(p1KickBind) && !isp1Hit) { // 81 = q
                // isp1Kick = isp1Hit ? false : true;
                if (p1Stamina >= kickStamina && !isp1LightPunch && !isp1Jump && !isp1Duck) {
                    p1Stamina -= isp1Kick ? 0 : kickStamina;
                    isp1Kick = true;
                }
                else if (p1Stamina >= kickStamina && isp1Duck) {
                    p1Stamina -= isp1LowKick ? 0 : kickStamina;
                    isp1LowKick = true;
                }
            }
            if (keysPressed.contains(p1DuckBind) && !isp1Hit) {
                if (!isp1Jump && !isp1Duck) {
                    isp1Duck = true;
                    p1PosY += 30;
                }
            }

            if (keysPressed.contains(p1BlockBind) && !isp1Hit) {
                if (p1Stamina >= blockStamina && !isp1Duck && !isp1Jump) {
                    isp1Block = true;
                }
            }

            // p2 movement
            if (keysPressed.contains(39) && !isp2Hit) { // 39 = ->
                isp2Forward = true;
            }
            if (keysPressed.contains(37) && !isp2Hit) { // 37 = <-
                isp2Backward = true;
            }
            if (keysPressed.contains(38) && !isp2Hit) { // 38 = ↑
                isp2Jump = true;
            }
            if (keysPressed.contains(40) && !isp2Hit) { // 40 == ↓
                // p2PosY += 10;
            }
            if (keysPressed.contains(p2PunchBind) && !isp2Hit) { // 47 = /
                // isp2LightPunch = isp2Hit ? false : true;
                if (p2Stamina >= lightPunchStamina && !isp2Kick && !isp2Jump && !isp2Duck) {
                    p2Stamina -= isp2LightPunch ? 0 : lightPunchStamina;
                    isp2LightPunch = true;
                }
                else if (p2Stamina >= lightPunchStamina && isp2Duck) {
                    p2Stamina -= isp2LowPunch ? 0 : lightPunchStamina;
                    isp2LowPunch = true;
                }
            }
            if (keysPressed.contains(p2KickBind) && !isp2Hit) { // 46 = .
                // isp2Kick = isp2Hit ? false: true;
                if (p2Stamina >= kickStamina && !isp2LightPunch && !isp2Jump && !isp2Duck) {
                    p2Stamina -= isp2Kick ? 0 : kickStamina;
                    isp2Kick = true;
                }
                else if (p2Stamina >= kickStamina && isp2Duck) {
                    p2Stamina -= isp2LowKick ? 0 : kickStamina;
                    isp2LowKick = true;
                }
            }

            if (keysPressed.contains(p2DuckBind) && !isp2Hit) {
                if (!isp2Jump && !isp2Duck) {
                    isp2Duck = true;
                    p2PosY += 30;
                }
            }

            if (keysPressed.contains(p2BlockBind) && !isp2Hit) {
                if (p2Stamina >= blockStamina && !isp2Duck && !isp2Jump) {
                    isp2Block = true;
                }
            }

        }

        else if (gameState == 7 || gameState == 8) {
            if (keysPressed.contains(8)) { // 8 = BACKSPACE
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
                resetSprites("");
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
                    isp1Forward = false;
                    p1SpriteNum = 0;
                }
                if (e.getKeyCode() == 65) { // 65 = a
                    isp1Backward = false;
                    p1SpriteNum = 0;
                }
                if (e.getKeyCode() == p1DuckBind && isp1Duck && p1PosY != defaultPosY) {
                    isp1Duck = false;
                    p1DuckSpriteNum = 0;
                    p1PosY -= 30;
                    p1DuckSpriteNum = 0;
                }

                if (e.getKeyCode() == p1BlockBind) {
                    isp1Block = false;
                }

                // p2
                if (e.getKeyCode() == 39) { // 39 = ->
                    isp2Forward = false;
                    p2SpriteNum = 0;
                }

                if (e.getKeyCode() == 37) { // 37 = <-
                    isp2Backward = false;
                    p2SpriteNum = 0;
                }

                if (e.getKeyCode() == p2DuckBind && isp2Duck && p2PosY != defaultPosY) { 
                    isp2Duck = false;
                    p2DuckSpriteNum = 0;
                    p2PosY -= 30;
                    p2DuckSpriteNum = 0;
                }
                if (e.getKeyCode() == p2BlockBind) {
                    isp2Block = false;
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

        int p1Left = p1PosX;
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
        int[] p2TopRight = { p2PosX + p2CurrentMovement.getWidth(), p2PosY };
        int[] p2BottomLeft = { p2PosX, p2PosY + p2CurrentMovement.getHeight() };
        int[] p2BottomRight = { p2PosX + p2CurrentMovement.getWidth(), p2PosY + p2CurrentMovement.getHeight() };
        // checks for the collisions
        if (playerToCheck.equals("p1")) {
            if (isp2Block)
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
            if (isp1Block)
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
            
            else {
                if (p2Right > 800) {
                    System.out.println('f');
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
        isp1Duck = false;
        isp2Duck = false;
        p1DuckSpriteNum = 0;
        p2DuckSpriteNum = 0;
        resetSprites("");
        gameState = 6;
        p1Combo = 0;
        p2Combo = 0;
        startTimer = "3";

        p1CurrentMovement = p1Idle[0];
        p2CurrentMovement = p2Idle[0];
        if (winner.equals("p1")) {p1Score++;}
        if (winner.equals("p2")) {p2Score++;}
        if (p1Score == 2) {gameState = 7;}
        if (p2Score == 2) {gameState = 8;}
    }

    public static void resetSprites(String whos) {
        if (!whos.equals("p2")) {
            isp1Forward = false;
            isp1Backward = false;
            isp1LightPunch = false;
            isp1Kick = false;
            isp1Jump = false;
            isp1Hit = false;
            frameController = 0;
            p1SpriteNum = 0;
            p1LightPunchSpriteNum = 0;
            p1KickSpriteNum = 0;
            p1JumpSpriteNum = 0;
            p1HitSpriteNum = 0;
            p1KnockedSpriteNum = 0;
            hasp1Hit = false;
            takenP1Stamina = false;
            p1HitSpriteNum = 0;
            isp1Hit = false;
            isp1Special = false;
            p1SpecialSpriteNum = 0;
        }
        else if (!whos.equals("p1")) {
            isp2Forward = false;
            isp2Backward = false;
            isp2LightPunch = false;
            isp2Kick = false;
            isp2Jump = false;
            isp2Hit = false;
            frameController = 0;
            p2SpriteNum = 0;
            p2LightPunchSpriteNum = 0;
            p2KickSpriteNum = 0;
            p2JumpSpriteNum = 0;
            p2DuckBlockSpriteNum = 0;
            p2HitSpriteNum = 0;
            p2KnockedSpriteNum = 0;
            hasp2Hit = false;
            takenP2Stamina = false;
            isp2Hit = false;
            p2HitSpriteNum = 0;
            isp2Special = false;
            p2SpecialSpriteNum = 0;
        }
    }

}
