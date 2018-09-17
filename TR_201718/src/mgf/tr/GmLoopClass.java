/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;


import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.util.Objects;
import kp.jngg.Display;
import kp.jngg.GameLoop;
import kp.jngg.input.InputEvent;
import kp.jngg.input.InputListener;
import kp.jngg.input.KeyId;
import kp.jngg.input.Keycode;
import kp.jngg.menu.Menu;
import kp.jngg.menu.MenuController;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.entity.EnemyModel;
import mgf.tr.scenario.BulletModel;
import mgf.tr.utils.Constants;
import mgf.tr.utils.SpriteConfigLoader;
/**
 *
 * @author ferna
 */
public class GmLoopClass implements GameLoop, InputListener
{
    private final Display display;
    private final Canvas canvas;      
    
    private final SpriteLoader sprites;
    
    private final MenuController menu;
    private final StageSelector stages;
    
    private final Sprite background;
    
    public GmLoopClass(Display display)
    {
        this.display = Objects.requireNonNull(display);
        this.canvas = new Canvas(display, Constants.SCREEN_CANVAS_WIDTH, Constants.SCREEN_CANVAS_HEIGHT, true);
        this.sprites = new SpriteLoader(new File("data" + File.separator + "sprites"));
        this.menu = new MenuController();
        this.stages = new StageSelector(canvas, sprites);
        this.background = loadBackground();
    }
    
    @Override
    public void init() {
             
        try
        {
            SpriteConfigLoader.loadSprites(sprites);

            /* Load Bullet models */
            BulletModel.loadBulletModels();
            
            /* Load Enemy models */
            EnemyModel.loadEnemyModels();
            
            initMenu();
        
            //stage = ScenarioController.loadScenario(canvas, sprites, "testLevel");
            /*stage = ScenarioController.loadScenario(canvas, sprites, "lvl2");
            stage.getScenario().setEnabledDrawBoundingBox(Config.getBoolean("debug.show_bounding_box", false));
            
            stage.start();*/
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            display.abort();
        }
    }
    
    private Sprite loadBackground()
    {
        String path = Config.getString("main.background", "");
        if(path == null || path.isEmpty())
            return null;
        return Constants.loadBackground(path);
    }

    @Override
    public void draw(Graphics2D gd){
        
        Graphics2D gc = canvas.getGraphics();
        
        canvas.clear(Color.BLACK);
        if(stages.hasCurrentStage())
            stages.drawCurrentStage(gc);
        else
        {
            if(background != null)
                background.draw(gc, 0, 0, canvas.getWidth(), canvas.getHeight());
            menu.draw(gc);
        }
        canvas.draw(gd);
        
    }

    @Override
    public void update(double d) {
        
        if(stages.hasCurrentStage())
            stages.updateCurrentStage(d);
        else
        {
            if(background != null)
                background.update(d);
            menu.update(d);
        }
        //stage.update(d);
        
    }

    @Override
    public void dispatchEvent(InputEvent ie) {
        if(stages.hasCurrentStage())
            stages.dispatchEventCurrentStage(ie);
        else menu.dispatchEvent(ie);
        //stage.dispatchEvent(ie);
        
    }
    
    
    private void initMenu()
    {
        menu.setPosition(0, 0);
        menu.setSize(canvas.getWidth(), canvas.getHeight());
        Constants.initMenuController(menu);
        menu.setActionInputId(KeyId.getId(Keycode.VK_ENTER));
        menu.setBackInputId(KeyId.getId(Keycode.VK_ESCAPE));
        menu.setUpInputId(KeyId.getId(Keycode.VK_UP));
        menu.setDownInputId(KeyId.getId(Keycode.VK_DOWN));
        menu.setTitleSize(64);
        menu.setNormalSize(32);
        menu.setSelectedSize(38);
        menu.setDescriptionSize(18);
        menu.setFont(Constants.DEFAULT_FONT.copy());
        
        Menu main = new Menu();
        main.setOptionTitle("MAIN MENU");
        main.setShowTitle(true);
        main.setTitlePosition(50);
        main.setFirstOptionPosition(canvas.getHeight() / 2);
        main.setPrintCenteredOptions(true);
        main.setBack(main);
        //main.addSimpleAction("PLAY STAGE", "Start a game at the stage you want.", (controller) -> { controller.goTo(stages); });
        main.addOption(stages);
        main.addSimpleAction("QUIT", "Quit the game.", (controller) -> { display.stop(); });
        menu.setRoot(main);
        
        stages.setOptionTitle("PLAY STAGE");
        stages.setOptionDescription("Start a game at the stage you want.");
        stages.setShowTitle(true);
        stages.setTitlePosition(50);
        stages.setFirstOptionPosition(canvas.getHeight() / 2);
        stages.setPrintCenteredOptions(true);
        stages.setBack(main);
        stages.setAutoStartNextStage(true);
        stages.initStages();
        
        menu.goToRoot();
    }
           
}