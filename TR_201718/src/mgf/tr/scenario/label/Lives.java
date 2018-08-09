/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.label;

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
        shipSprite = sprites.getSprite("shipMiddle");
        lives = 0;
    }
}
