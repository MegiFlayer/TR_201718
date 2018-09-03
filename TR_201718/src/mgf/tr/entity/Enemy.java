/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.math.RNG;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.Bullet;
import mgf.tr.scenario.BulletManager;
import mgf.tr.scenario.Scenario;
import mgf.tr.scenario.visual.Explosion;
import mgf.tr.scenario.visual.FloatingPoints;
import mgf.tr.utils.Constants;

/**
 *
 * @author ferna
 */
public class Enemy extends Entity
{

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */
    
    private static final RNG RAND = new RNG();
    
    private AnimatedSprite sprite;
    
    private final String explosionSpriteId;
    private String bulletModelId = "";
    private int fireRatio;
    private double fireDelay;
    private int score;
    private Color scoreColor;
    private double pixelsToDown;
    private boolean forceShoot;

    public Enemy(SpriteLoader sprites, BulletManager bullets, String explosionSpriteId) {
    
        super(sprites, bullets);
        sprite = null;
        this.explosionSpriteId = Objects.requireNonNull(explosionSpriteId);
    }
    
    @Override
    public final EntityType getEntityType() { return EntityType.ENEMY; }
    
    public void setSprite(AnimatedSprite sprite)
    {
        this.sprite = sprite;
    }
    
    public final void setBulletModelId(String bulletModelId)
    {
        this.bulletModelId = Objects.requireNonNull(bulletModelId);
    }
    
    public final void setFireRatio(int fireRatio)
    {
        this.fireRatio = fireRatio;
    }
    
    public final void setForceShoot(boolean flag) { this.forceShoot = flag; }
    
    public final void setScore(int score) { this.score = score; }
    public final int getScopre() { return score; }
    
    public final void setScoreColor(Color scoreColor) { this.scoreColor = scoreColor; }
    public final Color getScopreColor() { return scoreColor; }
    
    public final void setPixelsToDown(double pixels)
    {
        this.pixelsToDown = pixels < 0 ? 0 : pixels;
    }
    public final boolean isInToDown() { return pixelsToDown != 0; }
    
    @Override
    protected final void onCollide(Scenario scenario, Bullet bullet)
    {
        if(!isAlive())
        {
            Explosion expl = Explosion.createExplosion(sprites, explosionSpriteId, position.x, position.y, size.x * 1.25, size.y * 1.25, 16);
            scenario.addVisualObject(expl);
            scenario.getScore().addScore(score);
            
            FloatingPoints fp = FloatingPoints.create(score, position.x, position.y, 15, scoreColor);
            scenario.addVisualObject(fp);
        }
    }
    
    @Override
    public void init()
    {
        setSize(Constants.CELL_WIDTH * 0.9, Constants.CELL_HEIGHT * 0.9);
    }
    
    @Override
    public void innerDraw(Graphics2D g)
    {
        Vector2 pos = position.difference(size.quotient(2));
        if(sprite != null)
            sprite.draw(g, pos.x, pos.y, size.x, size.y);
    }

    @Override
    public void update(double delta)
    {
        if(sprite != null)
            sprite.update(delta);
        if(pixelsToDown != 0)
        {
            double last = speed.x;
            speed.set(0, Math.abs(last));
            super.update(delta);
            speed.set(last, 0);
            pixelsToDown -= Math.abs(last) * delta;
            if(pixelsToDown < 0)
            {
                position.y += pixelsToDown;
                pixelsToDown = 0;
            }
        }
        else super.update(delta);
        if(fireRatio > 0)
        {
            if(fireDelay <= 0)
            {
                if(stage != null && (forceShoot || stage.canShoot(this)))
                    bullets.createBullet(bulletModelId, this, getPosition(), 0);
                updateFireDelay();
            }
            else fireDelay -= delta;
        }
    }

    @Override
    public void dispatch(InputEvent event) {

    }
    
    public final void updateFireDelay()
    {
        if(fireRatio <= 0)
            return;
        fireDelay = 0.25;
        if(fireRatio >= 255)
            return;
        fireDelay += (1d - fireRatio / 255d) * 10d;
        fireDelay += RAND.getDouble(fireDelay);
    }
    
}

