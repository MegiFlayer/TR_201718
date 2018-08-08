/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;


import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import kp.jngg.Display;
import kp.jngg.GameLoop;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputListener;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
/**
 *
 * @author ferna
 */
public class GmLoopClass implements GameLoop, InputListener{
    
    private final Display display;
    private SpriteLoader sprites;
    private Sprite sprite1;
    private AnimatedSprite sprite2;
    private AnimatedSprite sprite3;
    private AnimatedSprite sprite4;
    private AnimatedSprite sprite5;
    private BulletManager bullets;
    
    private Nave ship;
    private Enemy enm1;
    
    public GmLoopClass(Display display)
    {
        this.display = Objects.requireNonNull(display);
    }
    
    
    @Override
    public void init() {
        sprites = new SpriteLoader(new File("sprites"));
        bullets = new BulletManager(sprites,
                new Vector2(display.getWidth() / 2, display.getHeight() / 2),
                new Vector2(display.getWidth(), display.getHeight()));
        try {
            
            sprite1 = (AnimatedSprite) sprites.loadAnimatedSprite("shipLeft", "Nave25x30_FSanz.png", 0, 0, 25, 30, 1).buildSprite();
            sprite2 = (AnimatedSprite) sprites.loadAnimatedSprite("shipMiddle", "Nave25x30_FSanz.png", 25, 0, 25, 30, 1).buildSprite();
            sprite3 = (AnimatedSprite) sprites.loadAnimatedSprite("shipRight", "Nave25x30_FSanz.png", 50, 0, 25, 30, 1).buildSprite();
            sprite4 = (AnimatedSprite) sprites.loadAnimatedSprite("laser", "ProyectilVertical29x64_FSanz.png", 0, 0, 29, 64, 3).buildSprite();
            sprite5 = (AnimatedSprite) sprites.loadAnimatedSprite("enemy", "Enemigo30x25_FSanz.png", 0, 0, 30, 25, 1).buildSprite();
            sprite4.setLoopMode();
            sprite4.setSpeed(2);
            sprite4.start();
            
        }
        catch(IOException ex) {
            ex.printStackTrace(System.err);
        }
        
        ship = new Nave(bullets);
        ship.setPosition(100, 620);
        ship.setSize(75, 90);
        ship.setSprite(sprite1, sprite2, sprite3);
        
        enm1 = new Enemy();
        enm1.setSize(120, 100);
        enm1.setPosition(100, 200);
        enm1.setSprite(sprite5);
        
    }

    @Override
    public void draw(Graphics2D gd){
        
        bullets.draw(gd);
        ship.draw(gd);
        enm1.draw(gd);
        
    }

    @Override
    public void update(double d) {
        
        bullets.update(d);
        ship.update(d);
        enm1.update(d);
        
    }

    @Override
    public void dispatchEvent(InputEvent ie) {
        
        ship.dispatch(ie);
        enm1.dispatch(ie);
    }
           
}
    
