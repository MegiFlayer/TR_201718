/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import kp.jngg.math.BoundingBox;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Constants;

/**
 *
 * @author Marc
 */
public final class BulletManager
{
    private final SpriteLoader sprites;
    private final LinkedList<Proyectil> aliveBullets;
    private final BoundingBox bounds;
    
    public BulletManager(SpriteLoader sprites, Vector2 boundsPosition, Vector2 boundsSize)
    {
        this.sprites = Objects.requireNonNull(sprites);
        this.aliveBullets = new LinkedList<>();
        this.bounds = BoundingBox.situate(boundsPosition, boundsSize);
    }
    
    private void createBullet(Vector2 pos, Vector2 size, Vector2 speed, Sprite sprite)
    {
        Proyectil bullet = new Proyectil();
        bullet.setSprite(sprite);
        bullet.setPosition(pos.x, pos.y);
        bullet.setSize(size.x, size.y);
        bullet.setSpeed(speed.x, speed.y);
        bullet.updateBoundingBox();
        aliveBullets.add(bullet);
    }
    
    public final void createShipBullet(Vector2 pos)
    {
        AnimatedSprite sprite = sprites.getSprite("laser");
        sprite.setLoopMode();
        sprite.setSpeed(20);
        sprite.start();
        createBullet(pos,
                new Vector2(Constants.BULLET_SHIP_WIDTH, Constants.BULLET_SHIP_HEIGHT),
                new Vector2(Constants.BULLET_SHIP_SPEEDX, Constants.BULLET_SHIP_SPEEDY), sprite);
    }
    
    public final void update(double delta)
    {
        ListIterator<Proyectil> it = aliveBullets.listIterator();
        while(it.hasNext())
        {
            Proyectil bullet = it.next();
            bullet.update(delta);
            if(!bounds.contains(bullet.getPosition()))
            {
                it.remove();
                continue;
            }
            
        }
    }
    
    public final void draw(Graphics2D g)
    {
        for(Proyectil bullet : aliveBullets)
        {
            bullet.draw(g);
        }
    }
}
