/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.visual;

import java.awt.Graphics2D;
import kp.jngg.sprite.Sprite;

/**
 *
 * @author mpasc
 */
public class Particle extends VisualObject<Sprite>
{
    public double ttl;
    
    public Particle(Sprite sprite)
    {
        super(sprite);
        ttl = 0f;
    }
    public Particle()
    {
        super();
        ttl = 0f;
    }
    
    public void update(double delta)
    {
        if(ttl <= 0f)
            return;
        ttl -= delta;
        super.update(delta);
    }
    
    public void draw(Graphics2D g)
    {
        if(ttl <= 0f)
            return;
        super.draw(g);
    }

    @Override
    public boolean isDead() { return ttl <= 0; }
}
