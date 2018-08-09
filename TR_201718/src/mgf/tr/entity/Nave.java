/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.Keycode;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Constants;
import mgf.tr.scenario.BulletManager;

/**
 *
 * @author ferna
 */
public class Nave extends Entity
{

    /**
     * Funciones recomendadas: draw: Para dibujar update: Actualizar valores
     * dispatchEvents: capturar eventos de inputs
     */
    private static final double X_SPEED = 400;
    private static final double FRICTION = 22.5;
    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    private double fireCoolDown;
    private boolean fireEnabled;

    private int moveX;
    

    public Nave(SpriteLoader sprites, BulletManager bullets) {
        super(sprites, bullets);
        sprite1 = null;
        sprite2 = null;
        sprite3 = null;
    }
    
    @Override
    public final EntityType getEntityType() { return EntityType.SHIP; }

    public void setSprite(Sprite s1, Sprite s2, Sprite s3) {
        
        sprite1 = s1;
        sprite2 = s2;
        sprite3 = s3;

    }
    
    @Override
    public void draw(Graphics2D g)
    {
        Vector2 pos = position.difference(size.quotient(2));
        if (sprite1 != null && moveX < 0) {
            sprite1.draw(g, pos.x, pos.y, size.x, size.y);
        }
        if (sprite2 != null && moveX == 0) {
            sprite2.draw(g, pos.x, pos.y, size.x, size.y);
        }
        if (sprite3 != null && moveX > 0) {
            sprite3.draw(g, pos.x, pos.y, size.x, size.y);
        }
        drawSpecs(g);
    }

    private void drawSpecs(Graphics2D g) {
        Color old = g.getColor();
        g.setColor(Color.GREEN);
        g.drawString("Position = " + position, 12, 12);
        g.drawString("Speed = " + speed, 12, 24);
        g.setColor(old);
    }

    @Override
    public void update(double delta)
    {
        position.ensureRangeXLocal(size.x / 2, Constants.CANVAS_WIDTH - size.x / 2);

        if (moveX != 0) {
            speed.x = X_SPEED * moveX;
        } else {
            if (speed.x > 0) {
                speed.x -= FRICTION;
                if (speed.x < 0) {
                    speed.x = 0;
                }
            } else if (speed.x < 0) {
                speed.x += FRICTION;
                if (speed.x > 0) {
                    speed.x = 0;
                }
            }
        }
        speed.ensureRangeLocal(600, 600);

        super.update(delta);
        
        if(fireCoolDown < 0)
            fireCoolDown = 0;
        else if(fireCoolDown > 0)
            fireCoolDown -= delta;
        
        if(fireEnabled && fireCoolDown == 0)
        {
            fireCoolDown = 0.25;
            bullets.createShipBullet(new Vector2(
                    position.x - Constants.BULLET_SHIP_WIDTH / 2,
                    position.y - Constants.BULLET_SHIP_HEIGHT
            ));
        }
            
    }

    @Override
    public void dispatch(InputEvent event) {
        if (event.getIdType() == InputId.KEYBOARD_TYPE) {

            int code = event.getCode();

            if (code == Keycode.VK_LEFT) {
                moveX += event.isPressed() ? -1 : 1;
            }
            if (code == Keycode.VK_RIGHT) {
                moveX += event.isPressed() ? 1 : -1;
            }
            if(code == Keycode.VK_SPACE)
            {
                fireEnabled = event.isPressed();
            }
        }
    }
}
