/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;


import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Objects;
import kp.jngg.Display;
import kp.jngg.GameLoop;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputListener;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.entity.EnemyModel;
import mgf.tr.scenario.BulletModel;
import mgf.tr.scenario.ScenarioController;
import mgf.tr.utils.Constants;
import mgf.tr.utils.SpriteConfigLoader;
/**
 *
 * @author ferna
 */
public class GmLoopClass implements GameLoop, InputListener{
    
    private SpriteLoader sprites;
        
    private final Display display;
    private final Canvas canvas;      
    
    //private Scenario stage;
    private ScenarioController stage;
    
    public GmLoopClass(Display display)
    {
        this.display = Objects.requireNonNull(display);
        this.canvas = new Canvas(display, Constants.SCREEN_CANVAS_WIDTH, Constants.SCREEN_CANVAS_HEIGHT, true);
    }
    
    @Override
    public void init() {
             
        try
        {
            sprites = new SpriteLoader(new File("data" + File.separator + "sprites"));
            SpriteConfigLoader.loadSprites(sprites);

            /* Load Bullet models */
            BulletModel.loadBulletModels();
            
            /* Load Enemy models */
            EnemyModel.loadEnemyModels();
        
            //stage = ScenarioController.loadScenario(canvas, sprites, "testLevel");
            stage = ScenarioController.loadScenario(canvas, sprites, "lvl2");
            stage.getScenario().setEnabledDrawBoundingBox(Config.getBoolean("debug.show_bounding_box", false));
            
            stage.start();
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            display.abort();
        }
    }

    @Override
    public void draw(Graphics2D gd){
        
        Graphics2D gc = canvas.getGraphics();
        
        canvas.clear(Color.BLACK);
        stage.draw(gc);
        canvas.draw(gd);
        
    }

    @Override
    public void update(double d) {
        
        stage.update(d);
        
    }

    @Override
    public void dispatchEvent(InputEvent ie) {
        
        stage.dispatchEvent(ie);
        
    }
           
}
    






/*

BACKUP GMLOOP

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 

package mgf.tr;


import java.awt.Color;
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
import mgf.tr.entity.Enemy;
import mgf.tr.entity.Nave;
import mgf.tr.scenario.BulletManager;
import mgf.tr.scenario.label.Lives;
/**
 *
 * @author ferna
 
public class GmLoopClass implements GameLoop, InputListener{
    
    private final Display display;
    private final Canvas canvas;
    private SpriteLoader sprites;
    private Sprite sprite1;
    private AnimatedSprite sprite2;
    private AnimatedSprite sprite3;
    private AnimatedSprite sprite4;
    private AnimatedSprite sprite5;
    private BulletManager bullets;
    private Lives lives;
    
    private Nave ship;
    private Enemy enm1;
    
    public GmLoopClass(Display display)
    {
        this.display = Objects.requireNonNull(display);
        this.canvas = new Canvas(display);
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
        
        lives = new Lives(sprites);
        lives.setPosition(display.getX() + Constants.SHIP_WIDTH * 0.5, 100);
        lives.setEnabled(true);
        
        ship = new Nave(sprites, bullets);
        ship.setPosition(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT - Constants.SHIP_HEIGHT + 20);
        ship.setSize(Constants.SHIP_WIDTH, Constants.SHIP_HEIGHT);
        ship.setSprite(sprite1, sprite2, sprite3);
        
        enm1 = new Enemy(sprites, bullets);
        enm1.setSize(120, 100);
        enm1.setPosition(100, 200);
        enm1.setSprite(sprite5);
        
    }

    @Override
    public void draw(Graphics2D gd){
        
        Graphics2D gc = canvas.getGraphics();
        canvas.clear(Color.BLACK);
        
        bullets.draw(gc);
        ship.draw(gc);
        enm1.draw(gc);
        lives.draw(gc);
        
        //canvas.destroyGraphicsCanvas();
        canvas.draw(gd);
    }

    @Override
    public void update(double d) {
        
        bullets.update(d);
        ship.update(d);
        enm1.update(d);
        lives.update(d);
        
    }

    @Override
    public void dispatchEvent(InputEvent ie) {
        
        ship.dispatch(ie);
        enm1.dispatch(ie);
    }
           
}
    

*/