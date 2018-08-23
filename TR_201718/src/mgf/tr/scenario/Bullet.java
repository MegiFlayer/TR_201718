/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.math.BoundingBox;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import kp.jngg.sprite.SpriteModel;
import mgf.tr.entity.Entity;
import mgf.tr.scenario.visual.Explosion;
import mgf.tr.utils.Constants;

/**
 *
 * @author ferna
 */
public class Bullet {

    private final Entity owner;
    private final Vector2 position;
    private final Vector2 spriteSize;
    private final Vector2 speed;
    private final Vector2 bboxPosition;
    private final Vector2 bboxSize;
    private final BoundingBox bbox;
    private Sprite sprite;
    private int power;
    private int explId;

    public Bullet(Entity owner, SpriteLoader sprites, BulletModel model) {

        this.owner = Objects.requireNonNull(owner);
        this.position = new Vector2();
        this.spriteSize = new Vector2(model.spriteWidth, model.spriteHeight);
        this.speed = new Vector2(0, model.speed);
        this.bboxPosition = new Vector2(model.bboxLocalX, model.bboxLocalY);
        this.bboxSize = new Vector2(model.bboxWidth, model.bboxHeight);
        this.bbox = new BoundingBox();
        this.power = model.power;
        this.explId = model.explosionId;
        this.sprite = sprites.getSprite(model.spriteId);
        if(sprite != null && sprite.getModel().getModelType() == SpriteModel.TYPE_ANIMATED)
        {
            AnimatedSprite as = (AnimatedSprite) sprite;
            as.setSpeed((float) model.spriteSpeed);
            as.setLoopMode();
            as.start();
        }
    }
    
    public final Entity getOwner() { return owner; }

    public void setPosition(double x, double y) {
        position.set(x, y);
    }
    
    public final void setBoundingBoxDeltaPosition(double x, double y)
    {
        bboxPosition.set(x, y);
    }

    public void setSpeed(double x, double y) {
        speed.x = x;
        speed.y = y;
    }
    
    public void setSpeed(Vector2 speed) { this.speed.set(speed); }

    public Vector2 getSpeed() {
        return speed.copy();
    }

    public void setSpriteSize(double width, double height) {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        spriteSize.set(width, height);
    }
    
    public void setBoundingBoxSize(double width, double height) {
        if (width < 1) {
            width = 1;
        }
        if (height < 1) {
            height = 1;
        }
        spriteSize.set(width, height);
    }

    public void setSprite(Sprite s1) {

        sprite = s1;

    }
    
    public final boolean hasCollision(BoundingBox other) { return bbox.hasCollision(other); }

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

    public double getSpriteSizeX() {

        return spriteSize.x;

    }

    public double getSpriteSizeY() {

        return spriteSize.y;

    }
    
    public double getBoundingBoxSizeX() {

        return bboxSize.x;

    }

    public double getBoundingBoxSizeY() {

        return bboxSize.y;

    }
    
    public final void setPower(int power)
    {
        this.power = power < 1 ? 1 : power;
    }
    public final int getPower() { return power; }


    public void draw(Graphics2D g) {
        Vector2 pos = position.difference(spriteSize.quotient(2));
        if (sprite != null) {
            sprite.draw(g, pos.x, pos.y, spriteSize.x, spriteSize.y);
        }
    }
    
    public final void drawBoundingBox(Graphics2D g)
    {
        if(bbox != null)
            bbox.draw(g, Color.RED);
    }
    
    public final void updateBoundingBox()
    {
        bbox.resituate(position.sum(bboxPosition), bboxSize);
    }
    
    public final void rotateBoundingBoxFromCenter(double radians)
    {
        bboxPosition.rotate(radians);
    }

    public void update(double delta)
    {
        sprite.update(delta);
        position.add(speed.product(delta));
        updateBoundingBox();
    }

    public void dispatch(InputEvent event) {
        if (event.getIdType() == InputId.KEYBOARD_TYPE) {

            int code = event.getCode();
            
        }
    }
    
    public final void explode(Scenario scenario)
    {
        double size = Math.max(spriteSize.x, spriteSize.y) * (1.25 + (0.25 * Math.abs(power)));
        Explosion expl = Explosion.createExplosion(scenario.getSpriteLoader(), getExplosionSpriteId(),
                position.x, position.y, size, size, 18);
        scenario.addVisualObject(expl);
    }
    
    private String getExplosionSpriteId()
    {
        switch(explId)
        {
            default:
            case 0: return Constants.SPRITE_EXPL_BULLET_0;
            case 1: return Constants.SPRITE_EXPL_BULLET_1;
            case 2: return Constants.SPRITE_EXPL_BULLET_2;
            case 3: return Constants.SPRITE_EXPL_BULLET_3;
        }
    }
}
