/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import mgf.tr.Canvas;
import mgf.tr.utils.Constants;
import mgf.tr.entity.Nave;
import mgf.tr.scenario.label.Lives;

/**
 *
 * @author Asus
 */
public final class ShipController
{
    private final Scenario scenario;
    private final Lives lives;
    private final Vector2 startPosition;
    private final Canvas entityCanvas;
    private Nave ship;
    private double timeToRespawn;
    
    public ShipController(Scenario scenario, Lives lives, Canvas entityCanvas)
    {
        this.scenario = Objects.requireNonNull(scenario);
        this.lives = Objects.requireNonNull(lives);
        this.entityCanvas = Objects.requireNonNull(entityCanvas);
        this.startPosition = new Vector2(entityCanvas.getWidth() / 2, entityCanvas.getHeight() - (Constants.SHIP_HEIGHT * 0.8));
    }
    
    public final Lives getLives() { return lives; }
    
    public final void setStartPosition(Vector2 position) { startPosition.set(position); }
    public final Vector2 getStartPosition() { return startPosition.copy(); }
    
    public final boolean hasMoreShips() { return lives.hasLives() || (ship != null && ship.isAlive() && !ship.hasDestroyed()); }
    
    public final void newShip()
    {
        if(ship != null)
        {
            ship.destroy();
            scenario.getEntityManager().removeEntity(ship);
        }
        
        ship = createShip();
        scenario.getEntityManager().addEntity(ship);
    }
    
    public final void update(double delta)
    {
        if(ship != null)
        {
            if(!ship.isAlive())
                ship.destroy();
            if(ship.hasDestroyed())
            {
                if(lives.hasLives())
                {
                    lives.removeLives(1);
                    timeToRespawn = Constants.SHIP_TIME_TO_RESPAWN;
                    ship = null;
                }
            }
        }
        else
        {
            timeToRespawn -= delta;
            if(timeToRespawn <= 0)
            {
                timeToRespawn = 0;
                newShip();
            }
        }
    }
    
    public final void dispatchEvent(InputEvent event)
    {
        if(ship != null && ship.isAlive())
        {
            ship.dispatch(event);
        }
    }
    
    private Nave createShip()
    {
        Nave ship = new Nave(scenario.getSpriteLoader(), scenario.getBulletManager(), entityCanvas.getWidth());
        ship.init();
        ship.setPosition(startPosition);
        return ship;
    }
}
