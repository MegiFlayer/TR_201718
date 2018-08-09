/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.util.Objects;
import kp.jngg.Display;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.entity.EntityManager;

/**
 *
 * @author Asus
 */
public final class Scenario
{
    private final Display display;
    private final SpriteLoader sprites;
    private final EntityManager entities;
    private final BulletManager bullets;
    private Sprite background;
    
    private Scenario(Display display, SpriteLoader sprites)
    {
        this.display = Objects.requireNonNull(display);
        this.sprites = Objects.requireNonNull(sprites);
        this.entities = new EntityManager();
        this.bullets = new BulletManager(sprites,
                new Vector2(display.getWidth() / 2, display.getHeight() / 2),
                new Vector2(display.getWidth(), display.getHeight()));
    }
}
