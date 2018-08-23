/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.visual;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import kp.jngg.font.DefaultFont;
import mgf.tr.utils.Constants;

/**
 *
 * @author Asus
 */
public class FloatingPoints extends Particle
{
    private static final DefaultFont FONT = Constants.DEFAULT_FONT.copy();
    private static final int MIN_SIZE = 12;
    private static final int MAX_SIZE = 24;
    private static final double MIN_TTL = 1;
    private static final double MAX_TTL = 2.5;
    private static final double MAX_SCORE = 1000;
    
    private final DefaultFont font;
    private final String text;
    private final double max;
    
    private FloatingPoints(DefaultFont font, String text, double ttl)
    {
        super();
        this.font = font;
        this.text = text;
        this.ttl = ttl;
        this.max = ttl * 0.25f;
    }
    
    public static final FloatingPoints create(long points, double x, double y, double speed, Color color)
    {
        int size;
        double ttl;
        if(points >= MAX_SCORE)
        {
            size = MAX_SIZE;
            ttl = MAX_TTL;
        }
        else
        {
            size = (int) (MIN_SIZE + (points / MAX_SCORE * (MAX_SIZE - MIN_SIZE)));
            ttl = MIN_TTL + (points / MAX_SCORE * (MAX_TTL - MIN_TTL));
        }
        DefaultFont font = FONT.copy();
        font.setColor(color);
        font.setDimensions(size);
        FloatingPoints fp = new FloatingPoints(font, Long.toString(points), ttl);
        fp.speed.y = -speed;
        fp.position.set(x, y);
        return fp;
    }
    
    @Override
    public void update(double delta)
    {
        if(ttl <= 0)
            return;
        super.update(delta);
        if(ttl < max)
            alpha = (float) (ttl / max);
    }
    
    @Override
    public void draw(Graphics2D g)
    {
        if(ttl <= 0)
            return;
        Composite old = g.getComposite();
        g.setComposite(__ALPHA.derive(alpha));
        font.printCentre(g,text, (int) position.x, (int) position.y);
        g.setComposite(old);
    }
}
