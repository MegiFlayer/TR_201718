/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.Keycode;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;

/**
 *
 * @author ferna
 */
public class Proyectil {
    
    private final Vector2 position;
    private final Vector2 size;
    private final Vector2 speed;
    protected Sprite sprite1;
    private boolean showAble;
    private boolean moveAble;
    
    public Proyectil() {
        
        position = new Vector2();
        size = new Vector2();
        speed = new Vector2();
        showAble = false;
        sprite1 = null;
        
    }
    
    public void setPosition(double x, double y) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        position.set(x, y);
    }
    
    public void setSpeed(double x, double y) {
        speed.x = x;
        speed.y = y;
    }
    
    public Vector2 getSpeed() {
        return speed.copy();
    }
    
    public void setSize(double width, double height) {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        size.set(width, height);
    }
    
    public void setSprite(Sprite s1) {
        
        sprite1 = s1;

    }
    
    public void switchShow(){
    
        if (showAble == false){
            showAble = true;
        }else{
            showAble = false;
        }
        
    }
    
    public void switchMove(){
    
        if (moveAble == false){
            moveAble = true;
        }else{
            moveAble = false;
        }
        
    }
    
    public double getPosX(){
    
        return position.x;
    
    }
    
    public double getPosY(){
    
        return position.y;
    
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
    
    public void chooseShow (boolean show){
    
        if (show == true){
            showAble = true;
        }else{
            showAble = false;
        }
    
    }
    
    public void offShow(){
    
            showAble = false;

    }
    
    public void onShow(){
    
            showAble = true;
        
    }
    
    public void chooseMove (boolean move){
    
        if (move == true){
            moveAble = true;
        }else{
            moveAble = false;
        }
    
    }
    
    public void offMove(){
    
            moveAble = false;

    }
    
    public void onMove(){
    
            moveAble = true;
        
    }
    
    public void draw(Graphics2D g) {
        if (sprite1 != null && showAble == true) {
            sprite1.draw(g, position.x, position.y, size.x, size.y);
        }
        drawSpecs(g);
    }
    
    private void drawSpecs(Graphics2D g) {
        Color old = g.getColor();
        g.setColor(Color.CYAN);
        g.drawString("Position SHOOT = " + position, 12, 36);
        g.drawString("Speed SHOOT = " + speed, 12, 48);
        g.setColor(old);
    }

    public void update(double delta) {
        
        if (moveAble == true){
        
            position.y -= 15 * 60 * delta;
        
        }
        
    }
    
    public void dispatch(InputEvent event) {
        if (event.getIdType() == InputId.KEYBOARD_TYPE) {

            int code = event.getCode();
            
            /*if(code == Keycode.VK_SPACE){
                showAble = true;
            }*/
                
            
        }
    }
}