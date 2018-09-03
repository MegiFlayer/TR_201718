/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.AlphaComposite;
import java.awt.Composite;
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
    private static final double MID_DEADLINE_SECONDS = MAX_DEADLINE_SECONDS / 2;
    private static final AlphaComposite __ALPHA = AlphaComposite.SrcOver;
    
    private final Scenario stage;
    private double deadlineTime;
    
    private final Canvas screenCanvas;
    private final StaticSprite winSprite;
    private final StaticSprite loseSprite;
    
    private ScenarioController(Canvas screenCanvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        this.screenCanvas = Objects.requireNonNull(screenCanvas);
        this.stage = ScenarioLoader.loadScenario(screenCanvas, sprites, stageName);
        this.deadlineTime = MAX_DEADLINE_SECONDS;
        
        this.winSprite = sprites.getSprite("scenario.you_win");
        this.loseSprite = sprites.getSprite("scenario.game_over");
    }
    
    public static ScenarioController loadScenario(Canvas screenCanvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        return new ScenarioController(screenCanvas, sprites, stageName);
    }
    
    public final Scenario getScenario() { return stage; }
    
    public final void start() { stage.start(); }
    
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
            stage.draw(g);
            switch(stage.getScenarioState())
            {
                case PLAYER_WIN:
                    if(winSprite != null)
                        drawEndSprite(g, winSprite);
                    break;
                case PLAYER_LOSE:
                    if(loseSprite != null)
                        drawEndSprite(g, loseSprite);
                    break;
            }
        }
        else stage.draw(g);
    }
    
    private void drawEndSprite(Graphics2D g, StaticSprite sprite)
    {
        float alpha = deadlineTime <= MID_DEADLINE_SECONDS
                ? 1f
                : (float) (1f - ((deadlineTime - MID_DEADLINE_SECONDS) / MID_DEADLINE_SECONDS));
        Composite old = g.getComposite();
        g.setComposite(__ALPHA.derive(alpha));
        sprite.draw(g, 0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
        g.setComposite(old);
    }
    
    public final void dispatchEvent(InputEvent event)
    {
        if(deadlineTime < MAX_DEADLINE_SECONDS)
            return;
        stage.dispatchEvent(event);
    }
}
