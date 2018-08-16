/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.BulletManager;

/**
 *
 * @author ferna
 */
public class Wall extends Entity {

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */

    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    

    public Wall(SpriteLoader sprites, BulletManager bullets) {
        
        super(sprites, bullets);

    }
    
    @Override
    public final EntityType getEntityType() { return EntityType.BLOCK; }
    
    public void setSprite(Sprite s1, Sprite s2, Sprite s3) {
        
        sprite1 = s1;
        sprite2 = s2;
        sprite3 = s3;

    }
    
    @Override
    public void init(){}
    
    @Override
    public void draw(Graphics2D g)
    {
        
        if (sprite1 != null) {
            sprite1.draw(g, position.x, position.y, size.x, size.y);
        }
        /*if (sprite2 != null) {
            sprite2.draw(g, position.x, position.y, size.x, size.y);
        }
        if (sprite3 != null) {
            sprite3.draw(g, position.x, position.y, size.x, size.y);
        }*/
        drawSpecs(g);
        
    }

    private void drawSpecs(Graphics2D g) {
        
        Color old = g.getColor();
        g.setColor(Color.magenta);
        g.drawString("Position = " + position, 12, 100);
        g.setColor(old);
        
    }

    @Override
    public void update(double delta)
    {
        super.update(delta);
    }

    @Override
    public void dispatch(InputEvent event) 
    {
        
    }
}
