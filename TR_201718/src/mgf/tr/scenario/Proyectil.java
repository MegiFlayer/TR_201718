/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.math.BoundingBox;
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
    private final BoundingBox bbox;
    protected Sprite sprite1;

    public Proyectil() {

        position = new Vector2();
        size = new Vector2();
        speed = new Vector2();
        bbox = new BoundingBox();
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
    
    public final boolean hasCollision(BoundingBox other) { return bbox.hasCollision(other); }

    @Deprecated
    public void switchShow() {

    }

    @Deprecated
    public void switchMove() {

    }

    public double getPosX() {

        return position.x;

    }

    public double getPosY() {

        return position.y;

    }
    public final Vector2 getPosition() { return position.copy(); }

    public void setPosX(double pX) {

        position.x = pX;

    }

    public void setPosY(double pY) {

        position.y = pY;

    }

    public double getSizeX() {

        return size.x;

    }

    public double getSizeY() {

        return size.y;

    }

    @Deprecated
    public void chooseShow(boolean show) {

        /*if (show == true) {
            showAble = true;
        } else {
            showAble = false;
        }*/

    }
    
    @Deprecated
    public void offShow() {

        //showAble = false;

    }
    
    @Deprecated
    public void onShow() {

        //showAble = true;

    }
    
    @Deprecated
    public void chooseMove(boolean move) {

        /*if (move == true) {
            moveAble = true;
        } else {
            moveAble = false;
        }*/

    }

    @Deprecated
    public void offMove() {

        //moveAble = false;

    }

    @Deprecated
    public void onMove() {

        //moveAble = true;

    }

    public void draw(Graphics2D g) {
        if (sprite1 != null) {
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
    
    public final void updateBoundingBox()
    {
        bbox.resituate(position, size);
    }

    public void update(double delta)
    {
        sprite1.update(delta);
        position.add(speed.product(delta));
        updateBoundingBox();
    }

    public void dispatch(InputEvent event) {
        if (event.getIdType() == InputId.KEYBOARD_TYPE) {

            int code = event.getCode();
            
        }
    }
}
