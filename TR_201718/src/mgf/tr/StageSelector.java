/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Graphics2D;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import kp.jngg.input.InputEvent;
import kp.jngg.json.JSONArray;
import kp.jngg.json.JSONException;
import kp.jngg.json.JSONObject;
import kp.jngg.json.JSONTokener;
import kp.jngg.menu.Menu;
import kp.jngg.menu.MenuController;
import kp.jngg.menu.MenuOption;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.ScenarioController;
import mgf.tr.scenario.ScenarioLoaderException;
import mgf.tr.scenario.ScenarioState;

/**
 *
 * @author Asus
 */
public final class StageSelector extends Menu
{
    private final Canvas screenCanvas;
    private final SpriteLoader sprites;
    private ScenarioController currentStage;
    private boolean autoStartNext;
    
    public StageSelector(Canvas screenCanvas, SpriteLoader sprites)
    {
        this.screenCanvas = screenCanvas;
        this.sprites = sprites;
    }
    
    public final void initStages()
    {
        loadStages();
        addSimpleAction("Back", "Back to Main Menu", (controller) -> { controller.goToRoot(); });
    }
    
    public final void setAutoStartNextStage(boolean flag) { autoStartNext = flag; }
    public final boolean isAutoStartNextStageEnabled() { return autoStartNext; }
    
    public final boolean hasCurrentStage() { return currentStage != null; }
    
    public final void updateCurrentStage(double delta)
    {
        if(currentStage != null)
        {
            currentStage.update(delta);
            if(currentStage.hasFinished())
            {
                if(autoStartNext && currentStage.getScenarioState() == ScenarioState.PLAYER_WIN)
                {
                    currentStage = null;
                    if(getSelectedIndex() + 2 < getOptionCount())
                    {
                        setSelectedIndex(getSelectedIndex() + 1);
                        loadScenario(super.<Stage>getSelectedOption().stageName);
                    }
                }
                else currentStage = null;
            }
        }
    }
    
    public final void drawCurrentStage(Graphics2D g)
    {
        if(currentStage != null)
            currentStage.draw(g);
    }
    
    public final void dispatchEventCurrentStage(InputEvent event)
    {
        if(currentStage != null)
            currentStage.dispatchEvent(event);
    }
    
    private void loadStages()
    {
        JSONObject base;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("data" + File.separator + "stage_config.json"))))
        {
            base = new JSONObject(new JSONTokener(new InputStreamReader(bis)));
        }
        catch(JSONException | IOException ex)
        {
            ex.printStackTrace(System.err);
            return;
        }
        
        JSONArray array = base.optJSONArray("stages");
        if(array == null)
            return;
        int len = array.length();
        int stageCount = 0;
        for(int i=0;i<len;i++)
        {
            JSONObject jsonModel = array.optJSONObject(i);
            if(jsonModel == null)
                continue;
            try { addOption(new Stage(jsonModel, stageCount)); stageCount++; }
            catch(Exception ex) { ex.printStackTrace(System.err); }
        }
    }
    
    private void loadScenario(String stageName)
    {
        try
        {
            currentStage = ScenarioController.loadScenario(screenCanvas, sprites, stageName);
            currentStage.start();
        }
        catch(ScenarioLoaderException ex)
        {
            ex.printStackTrace(System.err);
            currentStage = null;
        }
    }
    
    private final class Stage extends MenuOption
    {
        private final String stageName;
        
        private Stage(JSONObject base, int stageCount) throws Exception
        {
            stageName = base.getString("stage_path");
            String name = base.optString("stage_name");
            name = name.isEmpty() ? stageName : name;
            setOptionTitle("STAGE " + (stageCount + 1) + ": \"" + name + "\"");
            setOptionDescription("Play \"" + name + "\" stage.");
        }
        
        @Override
        public final void onAction(MenuController controller)
        {
            if(currentStage == null)
            {
                loadScenario(stageName);
            }
        }
    }
}
