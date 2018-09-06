/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputId;
import kp.jngg.input.KeyId;
import kp.jngg.input.Keycode;
import kp.jngg.menu.Menu;
import kp.jngg.menu.MenuController;
import kp.jngg.sprite.SpriteLoader;
import kp.jngg.sprite.StaticSprite;
import mgf.tr.Canvas;
import mgf.tr.utils.Constants;

/**
 *
 * @author Asus
 */
public final class ScenarioController
{
    private static final double MAX_DEADLINE_SECONDS = 5;
    private static final double MID_DEADLINE_SECONDS = MAX_DEADLINE_SECONDS / 2;
    private static final AlphaComposite __ALPHA = AlphaComposite.SrcOver;
    private static final Color PAUSE_BACK_COLOR = new Color(0f, 0f, 0f, 0.5f);
    private static final InputId PAUSE_INPUT_ID = KeyId.getId(Keycode.VK_ESCAPE);
    
    private final Scenario stage;
    private double deadlineTime;
    
    private final Canvas screenCanvas;
    private final StaticSprite winSprite;
    private final StaticSprite loseSprite;
    
    private final MenuController pause;
    private boolean enabledPause;
    
    private ScenarioController(Canvas screenCanvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        this.screenCanvas = Objects.requireNonNull(screenCanvas);
        this.stage = ScenarioLoader.loadScenario(screenCanvas, sprites, stageName);
        this.deadlineTime = MAX_DEADLINE_SECONDS;
        
        this.winSprite = sprites.getSprite("scenario.you_win");
        this.loseSprite = sprites.getSprite("scenario.game_over");
        
        this.pause = new MenuController();
        initPause();
    }
    
    public static ScenarioController loadScenario(Canvas screenCanvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        return new ScenarioController(screenCanvas, sprites, stageName);
    }
    
    private void initPause()
    {
        pause.setPosition(0, 0);
        pause.setSize(screenCanvas.getWidth(), screenCanvas.getHeight());
        Constants.initMenuController(pause);
        
        Menu exit = new Menu();
        Menu main = new Menu();
        
        exit.setOptionTitle("EXIT");
        exit.setOptionDescription("Return to the stage selector.");
        exit.setShowTitle(true);
        exit.setTitlePosition(50);
        exit.setFirstOptionPosition(screenCanvas.getHeight() / 2);
        exit.setPrintCenteredOptions(true);
        exit.setKeepSelectedIndex(false);
        exit.setBack(main);
        exit.addSimpleAction("NO", "Return to the pause menu.", (controller) -> { controller.goTo(main); });
        exit.addSimpleAction("YES", "Return to the stage selector.", (controller) -> { deadlineTime = 0; });
        
        
        main.setOptionTitle("PAUSE");
        main.setShowTitle(true);
        main.setTitlePosition(50);
        main.setFirstOptionPosition(screenCanvas.getHeight() / 2);
        main.setPrintCenteredOptions(true);
        main.setKeepSelectedIndex(false);
        main.setBack(main);
        main.setCustomOnBack((controller) -> { enabledPause = false; return false; });
        main.addSimpleAction("CONTINUE", "Resume the game.", (controller) -> { enabledPause = false; });
        main.addOption(exit);
        pause.setRoot(main);
        pause.goToRoot();
    }
    
    public final Scenario getScenario() { return stage; }
    
    public final void start() { stage.start(); }
    
    public final boolean hasFinished() { return deadlineTime <= 0; }
    public final ScenarioState getScenarioState() { return stage.getScenarioState(); }
    
    public final void update(double delta)
    {
        if(deadlineTime <= 0)
            return;
        if(enabledPause && deadlineTime == MAX_DEADLINE_SECONDS)
            pause.update(delta);
        else stage.update(delta);
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
        else
        {
            stage.draw(g);
            if(enabledPause)
            {
                Color old = g.getColor();
                g.setColor(PAUSE_BACK_COLOR);
                g.fillRect(0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());
                g.setColor(old);
                pause.draw(g);
            }
        }
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
        if(enabledPause)
            pause.dispatchEvent(event);
        else
        {
            if(event.isPressed() && event.getId().equals(PAUSE_INPUT_ID))
            {
                enabledPause = true;
                pause.goToRoot();
            }
            else stage.dispatchEvent(event);
        }
    }
}
