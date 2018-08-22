/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.UUID;
import kp.jngg.input.InputEvent;
import kp.jngg.math.BoundingBox;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.Bullet;
import mgf.tr.scenario.BulletManager;
import mgf.tr.scenario.Scenario;

/**
 *
 * @author Marc
 */
public abstract class Entity
{
    private final UUID id = UUID.randomUUID();
    protected final Vector2 position;
    protected final Vector2 speed;
    protected final Vector2 acceleration;
    protected final Vector2 size;
    protected final SpriteLoader sprites;
    protected final BulletManager bullets;
    
    private final BoundingBox bbox;
    private int hp;
    
    private boolean destroyed;
    
    public Entity(SpriteLoader sprites, BulletManager bullets)
    {
        this.sprites = Objects.requireNonNull(sprites);
        this.bullets = Objects.requireNonNull(bullets);
        
        this.position = new Vector2();
        this.speed = new Vector2();
        this.acceleration = new Vector2();
        this.size = new Vector2(1, 1);
        
        this.bbox = new BoundingBox();
        this.hp = 1;
    }
    
    public final UUID getId() { return id; }
    
    public abstract EntityType getEntityType();
    
    public final void setPosition(double x, double y) { position.set(x, y); }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    
    public final void setSpeed(double x, double y) { speed.set(x, y); }
    public final void setSpeed(Vector2 position) { this.speed.set(position); }
    
    public final void setSize(double x, double y) { size.set(x, y); }
    public final void setSize(Vector2 position) { this.size.set(position); }
    
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    public final Vector2 getPosition() { return position.copy(); }
    
    public final double getSpeedX() { return speed.x; }
    public final double getSpeedY() { return speed.y; }
    public final Vector2 getSpeed() { return speed.copy(); }
    
    public final double getAccelerationX() { return acceleration.x; }
    public final double getAccelerationY() { return acceleration.y; }
    public final Vector2 getAcceleration() { return acceleration.copy(); }
    
    public final double getWidth() { return size.x; }
    public final double getHeight() { return size.y; }
    public final Vector2 getSize() { return size.copy(); }
    
    public final void setHealthPoints(int hp)
    {
        this.hp = hp < 1 ? 1 : hp;
    }
    public final void damage(int points)
    {
        hp -= points < 0 ? -points : points;
        if(hp < 0)
            hp = 0;
    }
    public final void kill() { hp = 0; }
    public final void heal(int points)
    {
        if(hp > 0)
            hp += points < 0 ? -points : points;
    }
    public final void resurrect(int points)
    {
        if(hp <= 0)
        {
            if(points == 0)
                hp = 1;
            else hp = points < 0 ? -points : points;
        }
    }
    public final int getHealthPoints() { return hp; }
    public final boolean isAlive() { return hp > 0; }
    
    public final void updateBoundingBox()
    {
        bbox.resituate(position, size);
    }
    
    public final boolean hasCollision(Bullet bullet)
    {
        return bullet.hasCollision(bbox);
    }
    
    public final void collide(Scenario scenario, Bullet bullet)
    {
        if(bullet.getOwner().getEntityType() != getEntityType())
        {
            damage(bullet.getPower());
            onCollide(scenario, bullet);
        }
    }
    protected abstract void onCollide(Scenario scenario, Bullet bullet);
    
    public void init() {}
    
    public void update(double delta)
    {
        speed.add(acceleration.product(delta));
        position.add(speed.product(delta));
        updateBoundingBox();
    }
    
    public abstract void draw(Graphics2D g);
    
    public final void drawBoundingBox(Graphics2D g)
    {
        if(bbox != null)
            bbox.draw(g, Color.GREEN);
    }
    
    public abstract void dispatch(InputEvent event);
    
    public final void destroy()
    {
        if(destroyed)
            return;
        destroyed = true;
        onDestroying();
    }
    protected void onDestroying() {}
    public final boolean hasDestroyed() { return destroyed; }
}
