/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.sprite.SpriteLoader;
import kp.jngg.sprite.StaticSprite;
import mgf.tr.Canvas;

/**
 *
 * @author Asus
 */
public final class ScenarioController
{
    private static final double MAX_DEADLINE_SECONDS = 5;
    
    private final Scenario stage;
    private double deadlineTime;
    
    private final Canvas screenCanvas;
    private final StaticSprite winSprite = null;
    private final StaticSprite loseSprite = null;
    
    private ScenarioController(Canvas screenCanvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        this.screenCanvas = Objects.requireNonNull(screenCanvas);
        this.stage = ScenarioLoader.loadScenario(screenCanvas, sprites, stageName);
        this.deadlineTime = MAX_DEADLINE_SECONDS;
        
        //this.winSprite = sprites.getSprite("scenario.")
    }
    
    public final void update(double delta)
    {
        if(deadlineTime <= 0)
            return;
        stage.update(delta);
        if(stage.getScenarioState() == ScenarioState.PLAYER_WIN ||
                stage.getScenarioState() == ScenarioState.PLAYER_LOSE)
            deadlineTime -= delta;
    }
    
    public final void draw(Graphics2D g)
    {
        if(deadlineTime <= 0)
            return;
        if(deadlineTime < MAX_DEADLINE_SECONDS)
        {
            switch(stage.getScenarioState())
            {
                case PLAYER_WIN:
                    if(winSprite != null)
                        winSprite.draw(g, 0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
                    break;
                case PLAYER_LOSE:
                    if(loseSprite != null)
                        loseSprite.draw(g, 0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
                    break;
            }
            stage.draw(g);
        }
        else stage.draw(g);
    }
    
    public final void dispatchEvent(InputEvent event)
    {
        if(deadlineTime < MAX_DEADLINE_SECONDS)
            return;
        stage.dispatchEvent(event);
    }
}
