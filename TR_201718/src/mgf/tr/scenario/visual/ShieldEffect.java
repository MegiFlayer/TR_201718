/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.visual;

import java.awt.Graphics2D;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import kp.jngg.sprite.SpriteModel;

/**
 *
 * @author Asus
 */
public final class ShieldEffect extends VisualObject<AnimatedSprite>
{
    private static final double STATE_ENABLING_SPEED = 2;
    private static final double STATE_DISABLING_SPEED = 1.5;
    
    private final SpriteLoader sprites;
    private State state = State.DISABLED;
    
    public ShieldEffect(SpriteLoader sprites)
    {
        super();
        this.sprites = sprites;
        this.alpha = 0;
    }
    
    public final void setColor(ShieldColor color)
    {
        sprite = color.createSpriteInstance(sprites);
        sprite.setLoopMode();
        sprite.setSpeed(15);
        sprite.start();
    }
    
    public final void setEnabled(boolean flag)
    {
        if(flag)
        {
            if(state == State.DISABLED || state == State.DISABLING)
                state = State.ENABLING;
        }
        else
        {
            if(state == State.ENABLED || state == State.ENABLING)
                state = State.DISABLING;
        }
    }
    public final boolean isEnabled() { return state == State.ENABLED || state == State.ENABLING; }
    
    public final boolean isInFront() { return state != State.DISABLED && sprite != null && sprite.getCurrentFrame() < sprite.getFrameCount() / 2; }
    
    @Override
    public boolean isDead() { return false; }
    
    @Override
    public final void draw(Graphics2D g)
    {
        if(state != State.DISABLED)
            super.draw(g);
    }
    
    @Override
    public final void update(double delta)
    {
        if(state != State.DISABLED)
        {
            super.update(delta);
            switch(state)
            {
                case ENABLING:
                    alpha += STATE_ENABLING_SPEED * delta;
                    if(alpha >= 1)
                    {
                        alpha = 1;
                        state = State.ENABLED;
                    }
                    break;
                case DISABLING:
                    alpha -= STATE_DISABLING_SPEED * delta;
                    if(alpha <= 0)
                    {
                        alpha = 0;
                        state = State.DISABLED;
                    }
                    break;
            }
        }
    }
    
    private enum State
    {
        DISABLED,
        ENABLING,
        ENABLED,
        DISABLING;
    }
    
    public enum ShieldColor
    {
        RED("entity.energy_shield.red"),
        ORANGE("entity.energy_shield.orange"),
        YELLOW("entity.energy_shield.yellow"),
        GREEN("entity.energy_shield.green"),
        CYAN("entity.energy_shield.cyan"),
        BLUE("entity.energy_shield.blue"),
        PURPLE("entity.energy_shield.purple");
        
        private final String spriteId;
        
        private ShieldColor(String spriteId) { this.spriteId = spriteId; }
        
        private AnimatedSprite createSpriteInstance(SpriteLoader sprites)
        {
            Sprite sprite = sprites.getSprite(spriteId);
            if(sprite == null || sprite.getModel().getModelType() != SpriteModel.TYPE_ANIMATED)
                return null;
            return (AnimatedSprite) sprite;
        }
    }
}
