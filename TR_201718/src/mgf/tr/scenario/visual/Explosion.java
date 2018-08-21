/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.visual;

import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.SpriteLoader;

/**
 *
 * @author Asus
 */
public class Explosion extends VisualObject<AnimatedSprite>
{
    private Explosion(AnimatedSprite sprite)
    {
        super(sprite);
        sprite.setNormalMode();
        sprite.setCurrentFrame(0);
        sprite.start();
    }
    
    @Override
    public boolean isDead() { return sprite.hasEnded(); }
    
    public static final Explosion createExplosion(SpriteLoader sprites, String spriteId, double x, double y, double width, double height, double speed)
    {
        AnimatedSprite sprite = sprites.getSprite(spriteId);
        sprite.setSpeed((float) speed);
        
        Explosion expl = new Explosion(sprite);
        expl.position.set(x, y);
        expl.size.set(width, height);
        return expl;
    }
}
