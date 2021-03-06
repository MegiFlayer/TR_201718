/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.label;

import java.awt.Graphics2D;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;

/**
 *
 * @author Asus
 */
public final class Lives extends Label
{
    private final Sprite shipSprite;
    private int lives;
    
    public Lives(SpriteLoader sprites)
    {
        shipSprite = sprites.getSprite("shipMid");
        lives = 0;
    }
    
    public final void setLives(int lives)
    {
        this.lives = lives < 0 ? 0 : lives;
    }
    public final int getLives() { return lives; }
    
    public final void addLives(int lives)
    {
        this.lives += lives < 0 ? -lives : lives;
    }
    public final void removeLives(int lives)
    {
        this.lives -= lives < 0 ? -lives : lives;
        if(this.lives < 0)
            this.lives = 0;
    }
    
    public final boolean hasLives() { return lives > 0; }
    
    @Override
    public final boolean update(double delta)
    {
        if(!super.update(delta))
            return false;
        shipSprite.update(delta);
        return true;
    }
    
    @Override
    public final void draw(Graphics2D g)
    {
        if(!enabled)
            return;
        Vector2 spriteDim = new Vector2(font.getDimensions() * 0.8, font.getDimensions());
        Vector2 oldPosition = position.copy();
        
        shipSprite.draw(g, position.x, position.y - spriteDim.y * 0.2, spriteDim.x, spriteDim.y);
        
        position.add(spriteDim.x, spriteDim.y / 2 + 6);
        setText(" X " + lives);
        super.draw(g);
        position.set(oldPosition);
    }
}
