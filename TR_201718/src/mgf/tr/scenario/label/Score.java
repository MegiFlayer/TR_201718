/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.label;

import java.awt.Graphics2D;

/**
 *
 * @author Asus
 */
public final class Score extends Label
{
    private long score;
    private long remainder;
    
    public Score() {}
    
    public final long addScore(long points)
    {
        if(enabled && points > 0)
        {
            remainder += points;
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
            double speed = remainder + 250d;
            long part = (long) (speed * delta);
            if(part > remainder)
                part = remainder;
            remainder -= part;
            score += part;
        }
        return true;
    }
    
    @Override
    public final void draw(Graphics2D g)
    {
        String old = text;
        text += " " + score;
        super.draw(g);
        text = old;
    }
}
