/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;


import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import kp.jngg.GameLoop;
import kp.jngg.math.Vector2;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.InputListener;
import kp.jngg.input.Keycode;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
/**
 *
 * @author ferna
 */
public class GmLoopClass implements GameLoop, InputListener{
    
    private SpriteLoader sprites;
    private Sprite sprite1;
    private AnimatedSprite sprite2;
    private AnimatedSprite sprite3;
    private AnimatedSprite sprite4;
    
    private Nave ship;
    private Proyectil laser;
    
    
    @Override
    public void init() {
        sprites = new SpriteLoader(new File("sprites"));
        try {
            
            sprite1 = (AnimatedSprite) sprites.loadAnimatedSprite("shipLeft", "Nave25x30_FSanz.png", 0, 0, 25, 30, 1).buildSprite();
            sprite2 = (AnimatedSprite) sprites.loadAnimatedSprite("shipMiddle", "Nave25x30_FSanz.png", 25, 0, 25, 30, 1).buildSprite();
            sprite3 = (AnimatedSprite) sprites.loadAnimatedSprite("shipRight", "Nave25x30_FSanz.png", 50, 0, 25, 30, 1).buildSprite();
            sprite4 = (AnimatedSprite) sprites.loadAnimatedSprite("laser", "Proyectil64x29_FSanz.png", 64, 0, 64, 29, 1).buildSprite();
            
        }
        catch(IOException ex) {
            ex.printStackTrace(System.err);
        }
        
        ship = new Nave();
        ship.setPosition(100, 620);
        ship.setSize(75, 90);
        ship.setSprite(sprite1, sprite2, sprite3);
        
        laser = new Proyectil();
        laser.setPosition(100, 100);
        laser.setSize(100, 200);
        laser.setSprite(sprite4);
        
        
    }

    @Override
    public void draw(Graphics2D gd){
        
        ship.draw(gd);
        laser.draw(gd);
        
    }

    @Override
    public void update(double d) {
        
        ship.update(d);
        laser.update(d);
        
    }

    @Override
    public void dispatchEvent(InputEvent ie) {
        
        ship.dispatch(ie);
        laser.dispatch(ie);
        
    }
           
}
    
