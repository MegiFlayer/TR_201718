/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
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
    
    @Override
    public void init(){
    
    }
    
    @Override
    public void draw(Graphics2D g)
    {
        
    }

    private void drawSpecs(Graphics2D g) {
        
    }

    @Override
    public void update(double delta)
    {
        
    }

    @Override
    public void dispatch(InputEvent event) 
    {
        
    }
}
