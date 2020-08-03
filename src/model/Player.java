/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Image;

/**
 *
 * @author kuble
 */
public class Player extends Sprite {
    private double velX;
    private double velY;
    private final String name;
    
    private final int GAME_PANEL_WIDTH = 640;
    private final int GAME_PANEL_HEIGHT = 640;
    
    public Player(int width, int height, Image image, String name) {
        super();
        this.width = width;
        this.height = height;
        this.image = image;
        this.name = name;
    }
    
    public Player(int x, int y, int width, int height, Image image, String name) {
        super(x, y, width, height, image);
        this.name = name;
    }
    
    public void move() {
        if ((velX < 0 && x > 0) || (velX > 0 && x + width <= GAME_PANEL_WIDTH)) {
            x += velX;
        }
        if ((velY < 0 && y > 0) || (velY > 0 && y + height <= GAME_PANEL_HEIGHT)) {
            y += velY;
        }
    }

    public double getVelX() {
        return velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }
    
    public String getName() {
        return name;
    }
}
