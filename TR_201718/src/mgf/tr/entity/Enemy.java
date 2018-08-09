/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.Keycode;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;

/**
 *
 * @author ferna
 */
public class Enemy {

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */
    
    private Vector2 size;
    private Vector2 position;
    private Sprite enmSprite;
    private int moveX = 1;

    public Enemy() {
        
        size = new Vector2();
        position = new Vector2();
        enmSprite = null;
        
    }
    
    public void setSize(int width, int height){
    
        if(width < 1){
            width = 1;
        }
        
        if(height < 1){
            height = 1;
        }
        
        size.set(width, height);
    
    }
    
    public void setPosition(double x, double y){
    
        if(x < 0){
            x = 0;
        }
        
        if(y < 0){
            y = 0;
        }
        
        position.set(x, y);
    
    }
    
    public void setSprite(Sprite s1){
    
    enmSprite = s1;
    
    }

    public double getPosX(){
    
        return position.x;
    
    }
    
    public double getPosY(){
    
        return position.y;
    
    }
    
    public Vector2 getPos(){
    
        return position;
    
    }
    
    public void setPosX(double pX){
    
        position.x = pX;
    
    }
    
    public void setPosY(double pY){
    
        position.y = pY;
    
    }
    
    public double getSizeX(){
    
        return size.x;
    
    }
    
    public double getSizeY(){
    
        return size.y;
    
    }
    
    public Vector2 getSize(){
    
        return size;
    
    }
    
    public void init () {
    
    }
    
    public void draw(Graphics2D g) {
        
        if (enmSprite != null) {
            enmSprite.draw(g, position.x, position.y, size.x, size.y);
        }
        
    }
    
    

    public void update(double delta) {
        
        if(moveX == 1){
            position.add(6.5, 0);
        }else if(moveX == -1){
            position.add(-6.5, 0);
        }
        
        if(position.x <= 300){
            position.x = 300;
            moveX = 1;
        }else if(position.x + size.x >= 980){
            position.x = 980 - size.x;
            moveX = -1;
        }        
        
    }

    public void dispatch(InputEvent event) {

    }
    
}

