/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Constants;
import mgf.tr.scenario.BulletManager;

/**
 *
 * @author ferna
 */
public class Enemy extends Entity{

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */
    
    private Sprite enmSprite;
    private int moveX = 1;

    public Enemy(SpriteLoader sprites, BulletManager bullets) {
    
        super(sprites, bullets);
        enmSprite = null;
        
    }
    
    @Override
    public final EntityType getEntityType() { return EntityType.ENEMY; }
    
    public void setSprite(Sprite s1){
    
    enmSprite = s1;
    
    }
    
    public void setAnimatedSprite(AnimatedSprite sprite){
    
        enmSprite = sprite;
        sprite.setLoopMode();
        sprite.setSpeed(2);
        sprite.start();
    
    }
    
    @Override
    public void init () {
    
    }
    
    @Override
    public void draw(Graphics2D g) {
        
        Vector2 pos = position.difference(size.quotient(2));
        
        if (enmSprite != null) {
            enmSprite.draw(g, pos.x, pos.y, size.x, size.y);
        }
        
    }

    @Override
    public void update(double delta) {
        
        enmSprite.update(delta);
        
        speed.x = 400 * moveX;
        
        if(position.x <= size.x / 2){
            position.x = size.x / 2;
            moveX = 1;
        }else if(position.x >= Constants.CANVAS_WIDTH - size.x / 2){
            position.x = Constants.CANVAS_WIDTH - size.x / 2;
            moveX = -1;
        }
        
        super.update(delta);
        
    }

    @Override
    public void dispatch(InputEvent event) {

    }
    
}

