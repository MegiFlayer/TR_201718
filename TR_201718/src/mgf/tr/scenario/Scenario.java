/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Canvas;
import mgf.tr.entity.EntityManager;
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
        lives.setPosition(10, 20);
        lives.setEnabled(true);
        
        /* Puedes colocar aqui otras cosas a inicializar */
    }
    
    public final void update(double delta)
    {
        updateEntities(delta);
        bullets.update(delta);
        score.update(delta);
        lives.update(delta);
        
        if(background != null)
            background.update(delta);
        entities.update();
    }
    
    private void updateEntities(double delta)
    {
        entities.forEachEntity((e) -> {
            e.update(delta);
            if(!e.isAlive())
                e.destroy();
        });
    }
    
    public final void draw(Graphics2D g)
    {
        if(background != null)
            background.draw(g, 0, 0, width, height);
        
        drawEntities(g);
        bullets.draw(g);
        score.draw(g);
        lives.draw(g);
    }
    
    private void drawEntities(Graphics2D g)
    {
        entities.forEachEntity((e) -> {
            if(e.isAlive())
            {
                e.draw(g);
            }
        });
    }
}
