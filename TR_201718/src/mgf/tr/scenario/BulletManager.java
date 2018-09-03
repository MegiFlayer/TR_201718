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
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.entity.Entity;
import mgf.tr.entity.EntityManager;

/**
 *
 * @author Marc
 */
public final class BulletManager
{
    private final Scenario scenario;
    private final SpriteLoader sprites;
    private final EntityManager entities;
    private final LinkedList<Bullet> aliveBullets;
    private final BoundingBox bounds;
    private boolean drawBbox;
    
    public BulletManager(Scenario scenario, Vector2 boundsPosition, Vector2 boundsSize)
    {
        this.scenario = Objects.requireNonNull(scenario);
        this.sprites = Objects.requireNonNull(scenario.getSpriteLoader());
        this.entities = Objects.requireNonNull(scenario.getEntityManager());
        this.aliveBullets = new LinkedList<>();
        this.bounds = BoundingBox.situate(boundsPosition, boundsSize);
    }
    
    public final void setEnabledDrawBoundingBox(boolean flag) { drawBbox = flag; }
    
    public final void createBullet(String bulletId, Entity owner, Vector2 pos, double dirRadians)
    {
        BulletModel model = BulletModel.getModel(bulletId);
        if(model == null)
            return;
        Bullet bullet = model.build(owner, sprites);
        bullet.setPosition(pos.x, pos.y);
        bullet.setSpeed(bullet.getSpeed().rotate(dirRadians));
        bullet.rotateBoundingBoxFromCenter(dirRadians);
        bullet.updateBoundingBox();
        aliveBullets.add(bullet);
    }
    public final void createBullet(String bulletId, Entity owner, double x, double y, double dirRadians)
    {
        createBullet(bulletId, owner, new Vector2(x, y), dirRadians);
    }
    
    public final void update(double delta)
    {
        ListIterator<Bullet> it = aliveBullets.listIterator();
        while(it.hasNext())
        {
            Bullet bullet = it.next();
            bullet.update(delta);
            
            if(computeCollisions(bullet) || !bounds.contains(bullet.getPosition()))
            {
                bullet.explode(scenario);
                it.remove();
                continue;
            }
            
        }
    }
    
    public final void draw(Graphics2D g)
    {
        for(Bullet bullet : aliveBullets)
        {
            bullet.draw(g);
            if(drawBbox)
                bullet.drawBoundingBox(g);
        }
    }
    
    private boolean computeCollisions(Bullet bullet)
    {
        int collisions = 0;
        for(Entity entity : entities)
        {
            if(!entity.getId().equals(bullet.getOwner().getId()))
            {
                if(bullet.ignoreAllies())
                {
                    if(bullet.getOwner().getEntityType() != entity.getEntityType() && entity.hasCollision(bullet))
                    {
                        collisions++;
                        entity.collide(scenario, bullet);
                    }
                }
                else
                {
                    if(entity.hasCollision(bullet))
                    {
                        collisions++;
                        if(bullet.getOwner().getEntityType() != entity.getEntityType())
                            entity.collide(scenario, bullet);
                    }
                }
            }
        }
        return collisions > 0;
    }
}
