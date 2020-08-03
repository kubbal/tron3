/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import db.Database;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import model.Player;
import model.Sprite;
import model.Wall;
import res.ResourceLoader;

/**
 *
 * @author kuble
 */
public final class GamePanel extends JPanel {
    private boolean paused;
    private boolean isp1MovingStarted;
    private boolean isp2MovingStarted;
    private boolean player1Wins;
    private boolean player2Wins;
    private boolean init;
    
    private  Player player1;
    private  Player player2;
    private final Image player1image;
    private final Image player2image;
    private  ArrayList<Sprite> player1coordi;
    private  ArrayList<Sprite> player2coordi;
    private final Image background;
    private final Image wallIMG;
    private  ArrayList<Sprite> wallContainer;
    private ArrayList<Sprite> bgContainer;
    
    private final int PLAYER_WIDTH = 32;
    private final int PLAYER_HEIGHT = 32;
    private final double PLAYER_MOVE_X = 3;
    private final double PLAYER_MOVE_Y = 3;
    private final Timer newFrameTimer;
    private final int FPS = 60;
    
    private final int GAME_PANEL_WIDTH = 640;
    private final int GAME_PANEL_HEIGHT = 640;
    private final int BG_SIZE = 32;
    private int level;
    
    private final int ROWS = 20;
    private ArrayList<String> gameLevelRows;
    
    private Database db;
    
    public GamePanel(String pName1, String color1, String pName2, String color2, int level) throws IOException {
        super();
        this.level = level;
        db = new Database();
        
        //Load images
        player1image = ResourceLoader.loadImage("res/characters/" + color1 + ".png");
        player2image = ResourceLoader.loadImage("res/characters/" + color2 + ".png");
        background = ResourceLoader.loadImage("res/background.png");
        wallIMG = ResourceLoader.loadImage("res/wall.jpg");
        
        player1Wins = false;
        player2Wins = false;
        isp1MovingStarted = false;
        isp2MovingStarted = false;
        paused = false;
        init = false;
        player1 = new Player(PLAYER_WIDTH, PLAYER_HEIGHT, player1image, pName1);
        player2 = new Player(PLAYER_WIDTH, PLAYER_HEIGHT, player2image, pName2);
        player1coordi = new ArrayList<>();
        player2coordi = new ArrayList<>();
        wallContainer = new ArrayList<>();
        bgContainer = new ArrayList<>();
        
        registerControlls();

        newFrameTimer = new Timer(1000 / FPS, new NewFrameListener());
        readLevel(level);
        newFrameTimer.start();
    }
    
    public void restart(String pName1, String color1, String pName2, String color2, int level) {
        this.level = level;
        db = new Database();
        player1Wins = false;
        player2Wins = false;
        isp1MovingStarted = false;
        isp2MovingStarted = false;
        paused = false;
        init = false;
        player1 = new Player(PLAYER_WIDTH, PLAYER_HEIGHT, player1image, pName1);
        player2 = new Player(PLAYER_WIDTH, PLAYER_HEIGHT, player2image, pName2);
        player1coordi.clear();
        player2coordi.clear();
        wallContainer.clear();
        bgContainer.clear();
        readLevel(level);
        newFrameTimer.start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gr = (Graphics2D)g;
        this.setBackground(Color.BLACK);
        
        if(!player1Wins && !player2Wins) {
            if(!init) {
                for(int i=0; i<ROWS; i++) {
                String s = gameLevelRows.get(i);
                    for(int j=0; j<ROWS; j++) {
                        switch(s.charAt(j)) {
                            case '#':
                                wallContainer.add(new Wall(j*BG_SIZE, i*BG_SIZE, BG_SIZE, BG_SIZE, wallIMG));
                                break;
                            case 'P':
                                player1.setX(j*PLAYER_WIDTH);
                                player1.setY(i*PLAYER_HEIGHT);
                                break;
                            case 'G':
                                player2.setX(j*PLAYER_WIDTH);
                                player2.setY(i*PLAYER_HEIGHT);
                                break;
                            case ' ':
                                bgContainer.add(new Sprite(j*BG_SIZE, i*BG_SIZE, BG_SIZE, BG_SIZE, background));
                                break;
                        }
                    }
                }
                init = true;
            }
            for(Sprite s : bgContainer) {
                s.draw(gr);
            }
            for(Sprite w : wallContainer) {
                w.draw(gr);
            }
            for(Sprite p1c : player1coordi) {
                p1c.draw(gr);
            }
            for(Sprite p2c : player2coordi) {
                p2c.draw(gr);
            }
            player1.draw(gr);
            player2.draw(gr);
            
        }
        else {
            gr.setFont(new Font("Arial", Font.BOLD, 14));
            gr.setColor(Color.WHITE);
            gr.drawString("GAME OVER", GAME_PANEL_WIDTH/2-50, GAME_PANEL_HEIGHT/2);
            if(player1Wins && player2Wins) {
                gr.drawString("Egymásnak ütköztetek. Döntetlen", GAME_PANEL_WIDTH/2-100, GAME_PANEL_HEIGHT-200);
            } else if(player2Wins) {
                gr.drawString(player2.getName() + " nyert.", GAME_PANEL_WIDTH/2-50, GAME_PANEL_HEIGHT-200);
            } else if(player1Wins) {
                gr.drawString(player1.getName() + " nyert.", GAME_PANEL_WIDTH/2-50, GAME_PANEL_HEIGHT-200);
            }
        }
        
    }
    
    public int getGamePanelWidth() { return GAME_PANEL_WIDTH; }
    public int getGamePanelHeight() { return GAME_PANEL_HEIGHT; }
    
    public void registerControlls() {
        //Control with WASD
        this.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressed W");
        this.getActionMap().put("pressed W", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp1MovingStarted();
                player1.setVelY(-PLAYER_MOVE_Y);
                player1.setVelX(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressed A");
        this.getActionMap().put("pressed A", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp1MovingStarted();
                player1.setVelX(-PLAYER_MOVE_X);
                player1.setVelY(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressed S");
        this.getActionMap().put("pressed S", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp1MovingStarted();
                player1.setVelY(PLAYER_MOVE_Y);
                player1.setVelX(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressed D");
        this.getActionMap().put("pressed D", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp1MovingStarted();
                player1.setVelX(PLAYER_MOVE_X);
                player1.setVelY(0);
            }
        });
        
        //Control with arrows
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "pressed up");
        this.getActionMap().put("pressed up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp2MovingStarted();
                player2.setVelY(-PLAYER_MOVE_Y);
                player2.setVelX(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "pressed left");
        this.getActionMap().put("pressed left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp2MovingStarted();
                player2.setVelX(-PLAYER_MOVE_X);
                player2.setVelY(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "pressed down");
        this.getActionMap().put("pressed down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp2MovingStarted();
                player2.setVelY(PLAYER_MOVE_Y);
                player2.setVelX(0);
            }
        });
        this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "pressed right");
        this.getActionMap().put("pressed right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                checkp2MovingStarted();
                player2.setVelX(PLAYER_MOVE_X);
                player2.setVelY(0);
            }
        });
        
        //Press P to pause Game
        this.getInputMap().put(KeyStroke.getKeyStroke("P"), "pause");
        this.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                paused = !paused;
            }
        });
    }
    
    public void checkp1MovingStarted() {
        if(!isp1MovingStarted) {
            isp1MovingStarted = true;
        }
    }
    
    public void checkp2MovingStarted() {
        if(!isp2MovingStarted) {
            isp2MovingStarted = true;
        }
    }
    
    public void readLevel(int lvl) {
        InputStream is;
        is = ResourceLoader.loadResource("res/levels/level" + lvl + ".txt");
        
        try (Scanner sc = new Scanner(is)){
            String line = readNextLine(sc);
            gameLevelRows = new ArrayList<>();
            while (!line.isEmpty()){
                gameLevelRows.add(line);
                line = readNextLine(sc);
            }
        } catch (Exception e){
            System.out.println("Hiba a pálya betöltésekor.");
        }
    }
    
    private String readNextLine(Scanner sc){
        String line = "";
        while (sc.hasNextLine() && line.trim().isEmpty()){
            line = sc.nextLine();
        }
        return line;
    }
    
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (!paused) {
                if(isp1MovingStarted) {
                    player1coordi.add(new Sprite(player1.getX(), player1.getY(), player1.getWidth(), player1.getHeight(), player1image));
                }
                if(isp2MovingStarted) {
                    player2coordi.add(new Sprite(player2.getX(), player2.getY(), player2.getWidth(), player2.getHeight(), player2image));
                }
                player1.move();
                player2.move();
                
                //Vége a játéknak
                if (player1.collides(player2)) {
                    player1Wins = true;
                    player2Wins = true;
                    repaint();
                    newFrameTimer.stop();
                }
                for(Sprite c : player1coordi) {
                    if(player2.collides(c)) {
                        player1Wins = true;
                        repaint();
                        String gyoztes = player1.getName();
                        db.storeToDatabase(gyoztes, db.getHisScore(gyoztes)+1);
                        newFrameTimer.stop();
                    }
                }
                for(Sprite c : player2coordi) {
                    if(player1.collides(c)) {
                        player2Wins = true;
                        repaint();
                        String gyoztes = player2.getName();
                        db.storeToDatabase(gyoztes, db.getHisScore(gyoztes)+1);
                        newFrameTimer.stop();
                    }
                }
                for(Sprite w : wallContainer) {
                    if(player1.collides(w)) {
                        player2Wins = true;
                        repaint();
                        String gyoztes = player2.getName();
                        db.storeToDatabase(gyoztes, db.getHisScore(gyoztes)+1);
                        newFrameTimer.stop();
                    }
                    if(player2.collides(w)) {
                        player1Wins = true;
                        repaint();
                        String gyoztes = player1.getName();
                        db.storeToDatabase(gyoztes, db.getHisScore(gyoztes)+1);
                        newFrameTimer.stop();
                    }
                }
                //
            }
            repaint();
        }
    }
}
