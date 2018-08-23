/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.label;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import kp.jngg.math.Vector2;

/**
 *
 * @author Asus
 */
public final class Score extends Label
{
    private long score;
    private long remainder;
    private double delay;
    
    public Score() {}
    
    public final long addScore(long points)
    {
        if(enabled && points > 0)
        {
            remainder += points;
            delay = 1.25;
            return points;
        }
        return 0;
    }
    public final long getScore() { return score; }
    
    @Override
    public final boolean update(double delta)
    {
        if(!super.update(delta))
            return false;
        if(remainder > 0)
        {
            if(delay > 0)
                delay -= delta;
            else
            {
                double speed = remainder + 250d;
                long part = (long) (speed * delta);
                if(part > remainder)
                    part = remainder;
                remainder -= part;
                score += part;
            }
        }
        return true;
    }
    
    @Override
    public final void draw(Graphics2D g)
    {
        String old = text;
        text += " " + score;
        super.draw(g);
        
        if(remainder != 0)
        {
            Color oldColor = font.getColor();
            Vector2 oldPos = position.copy();
            Dimension dim = font.getTextSize(text);
            position.x += dim.width;
            font.setColor(Color.GREEN);
            text = " " + remainder;
            super.draw(g);
            font.setColor(Color.GREEN);
            font.setColor(oldColor);
            position.set(oldPos);
        }
        
        text = old;
    }
}
