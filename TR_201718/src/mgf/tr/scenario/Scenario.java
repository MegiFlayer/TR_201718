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
    
    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    private Sprite sprite4;
    private final Canvas can;
        
    private Nave ship;
    private Enemy enm1;
    
    private final double width;
    private final double height;
    
    private Scenario(Canvas canvas, SpriteLoader sprites)
    {
        this.can = canvas;
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
        lives.setPosition(10, 20);
        lives.setEnabled(true);
        
        lives.setPosition(display.getX() + Constants.SHIP_WIDTH * 0.5, 100);
        lives.setEnabled(true);
        /* Puedes colocar aqui otras cosas a inicializar */
        

        
        ship = new Nave(sprites, bullets);
        ship.setPosition(Constants.CANVAS_WIDTH / 2, Constants.CANVAS_HEIGHT - Constants.SHIP_HEIGHT + 20);
        ship.setSize(Constants.SHIP_WIDTH, Constants.SHIP_HEIGHT);
        sprite1 = sprites.getSprite("shipLeft");
        sprite2 = sprites.getSprite("shipMid");
        sprite3 = sprites.getSprite("shipRight");
        ship.setSprite(sprite1, sprite2, sprite3);
        
        
        
        enm1 = new Enemy(sprites, bullets);
        enm1.setSize(120, 100);
        enm1.setPosition(100, 200);
        sprite4 = sprites.getSprite("enemy");
        enm1.setSprite(sprite4);
        
    }
    
    public final void update(double delta)
    {
        updateEntities(delta);
        bullets.update(delta);
        score.update(delta);
        lives.update(delta);
        ship.update(delta);
        enm1.update(delta);
        
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
        
        Graphics2D gc = can.getGraphics();
        can.clear(Color.BLACK);
        can.draw(g);
        
        bullets.draw(gc);
        lives.draw(gc);
        ship.draw(gc);
        enm1.draw(gc);
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
