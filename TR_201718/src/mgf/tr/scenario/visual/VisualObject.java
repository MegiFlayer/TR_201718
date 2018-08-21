/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.visual;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import mgf.tr.Canvas;

/**
 *
 * @author Asus
 */
public abstract class VisualObject<S extends Sprite>
{
    protected static final AlphaComposite __ALPHA = AlphaComposite.SrcOver;
    
    public final Vector2 position, speed, acceleration;
    public final Vector2 size;
    public S sprite;
    public float angle, angularSpeed, alpha, alphaSpeed;
    
    public VisualObject(S sprite)
    {
        position = new Vector2();
        speed = new Vector2();
        acceleration = new Vector2();
        size = new Vector2();
        this.sprite = sprite;
        angle = 0f;
        angularSpeed = 0f;
        alpha = 1f;
        alphaSpeed = 0f;
    }
    public VisualObject()
    {
        this(null);
    }
    
    public void update(double delta)
    {
        position.add(speed.product(delta));
        speed.add(acceleration.product(delta));
        angle += angularSpeed * delta;
        alpha += alphaSpeed * delta;
        alpha = alpha > 1f ? 1f : alpha < 0f ? 0f : alpha;
        if(sprite != null)
            sprite.update(delta);
    }
    
    public void draw(Graphics2D g)
    {
        if(sprite == null)
            return;
        Vector2 pos = position.difference(size.quotient(2));
        if(alpha != 1f)
        {
            Composite old = g.getComposite();
            g.setComposite(__ALPHA.derive(alpha));
            sprite.draw(g, pos.x, pos.y, size.x, size.y, angle);
            g.setComposite(old);
            return;
        }
        sprite.draw(g, pos.x, pos.y, size.x, size.y, angle);
    }
    
    public final boolean isOutOfCanvas(Canvas canvas)
    {
        return position.x + size.x < 0 ||
                position.x > (canvas.getWidth() + size.x) ||
                position.y + size.y < 0 ||
                position.y > (canvas.getHeight() + size.y);
    }
    
    public abstract boolean isDead();
}
