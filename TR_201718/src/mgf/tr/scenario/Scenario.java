/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.Display;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Canvas;
import mgf.tr.Constants;
import mgf.tr.entity.Enemy;
import mgf.tr.entity.Entity;
import mgf.tr.entity.EntityManager;
import mgf.tr.entity.Nave;
import mgf.tr.entity.Wall;
import mgf.tr.scenario.label.Lives;
import mgf.tr.scenario.label.Score;

/**
 *
 * @author Asus
 */
public final class Scenario
{
    
    private final SpriteLoader sprites;
    private final EntityManager entities;
    private final BulletManager bullets;
    private final Score score;
    private final Lives lives;
    private Sprite background;
    private Display display;
    
    private Sprite sprtNaveL;
    private Sprite sprtNave;
    private Sprite sprtNaveR;
    private AnimatedSprite sprtEnm;
    private Sprite sprtShield1;
    private Sprite sprtShield2;
    private Sprite sprtShield3;
        
    private Nave ship;
    private Enemy enm1;
    private Wall shield1;
    private Wall shield2;
    private Wall shield3;
    private Wall shield4;
    
    private final double width;
    private final double height;
    
    private Scenario(Canvas canvas, SpriteLoader sprites)
    {
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.sprites = Objects.requireNonNull(sprites);
        this.entities = new EntityManager();
        this.bullets = new BulletManager(sprites, new Vector2(width / 2, height / 2), new Vector2(width, height));
        
        score = new Score();
        lives = new Lives(sprites);
        
        init();
    }
    
    /* Usa este metodo para crear una instancia de Scenario */
    public static final Scenario createDebugScenario(Canvas canvas, SpriteLoader sprites)
    {
        return new Scenario(canvas, sprites);
    }
    
    private void init()
    {
        /* Situar puntuación arriba a la izquierda */
        score.setPosition(10, 5);
        score.setEnabled(true);
        
        /* Situar vidas justo debajo de la puntuación */
        lives.setPosition(30, 64);
        lives.setEnabled(true);
        
        /* Puedes colocar aqui otras cosas a inicializar */
        

        
        ship = new Nave(sprites, bullets);
        ship.setPosition(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT - Constants.SHIP_HEIGHT + 20);
        ship.setSize(Constants.SHIP_WIDTH, Constants.SHIP_HEIGHT);
        sprtNaveL = sprites.getSprite("shipLeft");
        sprtNave = sprites.getSprite("shipMid");
        sprtNaveR = sprites.getSprite("shipRight");
        ship.setSprite(sprtNaveL, sprtNave, sprtNaveR);
        
        
        
        enm1 = new Enemy(sprites, bullets);
        enm1.setSize(120, 100);
        enm1.setPosition(100, 200);
        sprtEnm = sprites.getSprite("enemy");
        enm1.setAnimatedSprite(sprtEnm);
        
        sprtShield1 = sprites.getSprite("shieldFull");
        sprtShield2 = sprites.getSprite("shieldTouched");
        sprtShield3 = sprites.getSprite("shieldBroken");
        
        shield1 = new Wall(sprites, bullets);
        shield1.setSize(Constants.WALL_WIDTH, Constants.WALL_HEIGHT);
        shield1.setPosition(Constants.CANVAS_WIDTH / 5 - Constants.WALL_WIDTH / 2, ship.getPositionY() - 135);
        shield1.setSprite(sprtShield1, sprtShield2, sprtShield3);
        
        shield2 = new Wall(sprites, bullets);
        shield2.setSize(Constants.WALL_WIDTH, Constants.WALL_HEIGHT);
        shield2.setPosition(Constants.CANVAS_WIDTH / 5 * 2 - Constants.WALL_WIDTH / 2, ship.getPositionY() - 135);
        shield2.setSprite(sprtShield1, sprtShield2, sprtShield3);
        
        shield3 = new Wall(sprites, bullets);
        shield3.setSize(Constants.WALL_WIDTH, Constants.WALL_HEIGHT);
        shield3.setPosition(Constants.CANVAS_WIDTH / 5 * 3 - Constants.WALL_WIDTH / 2, ship.getPositionY() - 135);
        shield3.setSprite(sprtShield1, sprtShield2, sprtShield3);
        
        shield4 = new Wall(sprites, bullets);
        shield4.setSize(Constants.WALL_WIDTH, Constants.WALL_HEIGHT);
        shield4.setPosition(Constants.CANVAS_WIDTH / 5 * 4  - Constants.WALL_WIDTH / 2, ship.getPositionY() - 135);
        shield4.setSprite(sprtShield1, sprtShield2, sprtShield3);
        
    }
    
    public final void update(double delta)
    {
        updateEntities(delta);
        bullets.update(delta);
        score.update(delta);
        lives.update(delta);
        ship.update(delta);
        enm1.update(delta);
        shield1.update(delta);
        shield2.update(delta);
        shield3.update(delta);
        shield4.update(delta);
        
        if(background != null)
            background.update(delta);
        entities.update();
    }
    
    private void updateEntities(double delta)
    {
        entities.forEachEntity((e) -> {
            if(!e.hasDestroyed())
            {
                e.update(delta);
                if(!e.isAlive())
                    e.destroy();
            }
        });
    }
    
    public final void draw(Graphics2D g)
    {
        
        if(background != null)
            background.draw(g, 0, 0, width, height);

        drawEntities(g);
        bullets.draw(g);
        score.draw(g);
        
        bullets.draw(g);
        lives.draw(g);
        ship.draw(g);
        enm1.draw(g);
        shield1.draw(g);
        shield2.draw(g);
        shield3.draw(g);
        shield4.draw(g);
    }
    
    private void drawEntities(Graphics2D g)
    {
        entities.forEachEntity((e) -> {
            if(!e.hasDestroyed())
            {
                e.draw(g);
            }
        });
    }
    
    public final void dispatch(InputEvent event)
    {
        entities.forEachEntity((Entity e) -> {
            if(!e.hasDestroyed())
            {
                e.dispatch(event);
            }
        });
        
        ship.dispatch(event);
        enm1.dispatch(event);
        
    }
}
