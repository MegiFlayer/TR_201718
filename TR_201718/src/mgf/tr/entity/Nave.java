/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Graphics2D;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.KeyId;
import kp.jngg.input.Keycode;
import kp.jngg.input.virtual.VirtualBiBinaryButton;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.Bullet;
import mgf.tr.scenario.BulletManager;
import mgf.tr.scenario.Scenario;
import mgf.tr.scenario.visual.Explosion;
import mgf.tr.scenario.visual.ShieldEffect.ShieldColor;
import mgf.tr.utils.Constants;

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
    private static final double X_SPEED = 200;
    private static final double FRICTION = 22.5;
    private Sprite sprite1;
    private Sprite sprite2;
    private Sprite sprite3;
    private double fireCoolDown;
    private boolean fireEnabled;
    
    private double shieldEnabledTime;
    
    private final double maxX;

    private final VirtualBiBinaryButton moveX;
    

    public Nave(SpriteLoader sprites, BulletManager bullets, double maxX) {
        super(sprites, bullets);
        sprite1 = null;
        sprite2 = null;
        sprite3 = null;
        this.maxX = maxX;
        
        this.moveX = new VirtualBiBinaryButton(KeyId.getId(Keycode.VK_LEFT), KeyId.getId(Keycode.VK_RIGHT));
        
        setShieldColor(ShieldColor.BLUE);
    }
    
    public final void setShieldEnabledTime(double seconds)
    {
        shieldEnabledTime = seconds < 0 ? 0 : seconds;
        if(shieldEnabledTime > 0)
        {
            if(!isShieldEnabled())
                setShieldEnabled(true);
        }
        else if(isShieldEnabled())
            setShieldEnabled(false);
    }
    public final double getShieldEnabledTime() { return shieldEnabledTime < 0 ? 0 : shieldEnabledTime; }
    
    @Override
    public final EntityType getEntityType() { return EntityType.SHIP; }

    public void setSprite(Sprite s1, Sprite s2, Sprite s3) {
        
        sprite1 = s1;
        sprite2 = s2;
        sprite3 = s3;

    }
    
    public final void tryExplode(Scenario scenario)
    {
        if(!isAlive())
        {
            double size = Math.max(this.size.x, this.size.y) * 2.5;
            Explosion expl = Explosion.createExplosion(sprites, Constants.SPRITE_EXPL_BIG, position.x, position.y,
                    size , size, 30);
            scenario.addVisualObject(expl);
        }
    }
    
    @Override
    protected final void onCollide(Scenario scenario, Bullet bullet)
    {
        tryExplode(scenario);
    }
    
    @Override
    protected void onDestroying() {}
            
    @Override
    public void init()
    {
        setSize(Constants.SHIP_WIDTH, Constants.SHIP_HEIGHT);
        setSprite(sprites.getSprite("shipLeft"), sprites.getSprite("shipMid"), sprites.getSprite("shipRight"));
    }
    
    @Override
    public void innerDraw(Graphics2D g)
    {
        Vector2 pos = position.difference(size.quotient(2));
        if (sprite1 != null && moveX.isLeftPressed()) {
            sprite1.draw(g, pos.x, pos.y, size.x, size.y);
        }
        if (sprite2 != null && !moveX.isAnyPressed()) {
            sprite2.draw(g, pos.x, pos.y, size.x, size.y);
        }
        if (sprite3 != null && moveX.isRightPressed()) {
            sprite3.draw(g, pos.x, pos.y, size.x, size.y);
        }
    }

    @Override
    public void update(double delta)
    {
        position.ensureRangeXLocal(size.x / 2, maxX - size.x / 2);

        if (moveX.isAnyPressed()) {
            speed.x = (fireEnabled ? X_SPEED * 0.5 : X_SPEED) * moveX.getDirection();
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
            bullets.createBullet("ship_bullet", this,
                    position.x, position.y - Constants.BULLET_SHIP_HEIGHT / 2 - size.y / 2, Math.PI);
        }
        
         if(isShieldEnabled())
        {
            if(shieldEnabledTime <= 0)
            {
                setShieldEnabled(false);
                shieldEnabledTime = 0;
            }
            else
            {
                shieldEnabledTime -= delta;
            }
        }
            
    }

    @Override
    public void dispatch(InputEvent event)
    {
        if(event.getIdType() == InputId.KEYBOARD_TYPE)
        {
            int code = event.getCode();

            moveX.dispatchEvent(event);
            if(code == Keycode.VK_SPACE)
                fireEnabled = event.isPressed();
        }
    }
}
