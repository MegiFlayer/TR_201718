/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.utils.Constants;
import mgf.tr.scenario.BulletManager;
import mgf.tr.scenario.Bullet;
import mgf.tr.scenario.Scenario;
import mgf.tr.scenario.visual.Explosion;

/**
 *
 * @author ferna
 */
public class Wall extends Entity {

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */
    
    private static final int MAX_HEALTH_POINTS = 5;

    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    

    public Wall(SpriteLoader sprites, BulletManager bullets) {
        
        super(sprites, bullets);

    }
    
    @Override
    public final EntityType getEntityType() { return EntityType.BLOCK; }
    
    public void setSprite(Sprite s1, Sprite s2, Sprite s3) {
        
        sprite1 = s1;
        sprite2 = s2;
        sprite3 = s3;

    }
    
    @Override
    protected final void onCollide(Scenario scenario, Bullet bullet)
    {
        if(!isAlive())
        {
            Explosion expl = Explosion.createExplosion(sprites, Constants.SPRITE_EXPL_NORMAL,
                    position.x, position.y, size.x * 1.2, size.x * 1.2, 15);
            scenario.addVisualObject(expl);
        }
    }
    
    @Override
    public void init()
    {
        setSize(Constants.WALL_WIDTH, Constants.WALL_HEIGHT);
        //setPosition(Constants.CANVAS_WIDTH / 5 - Constants.WALL_WIDTH / 2, Constants.CANVAS_HEIGHT - Constants.SHIP_HEIGHT - 115);
        setSprite(sprites.getSprite("shieldFull"), sprites.getSprite("shieldTouched"), sprites.getSprite("shieldBroken"));
        setHealthPoints(MAX_HEALTH_POINTS);
    }
    
    @Override
    public void draw(Graphics2D g)
    {
        Vector2 pos = position.difference(size.quotient(2));
        int hp = getHealthPoints();
        if(hp < 3)
        {
            if(sprite3 != null)
                sprite3.draw(g, pos.x, pos.y, size.x, size.y);
        }
        else if(hp < MAX_HEALTH_POINTS)
        {
            if(sprite2 != null)
                sprite2.draw(g, pos.x, pos.y, size.x, size.y);
        }
        else if(sprite1 != null)
            sprite1.draw(g, pos.x, pos.y, size.x, size.y);
        
    }

    private void drawSpecs(Graphics2D g) {
        
        Color old = g.getColor();
        g.setColor(Color.magenta);
        g.drawString("Position = " + position, 12, 100);
        g.setColor(old);
        
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
    }

    @Override
    public void dispatch(InputEvent event) 
    {
        
    }
}
