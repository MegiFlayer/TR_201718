/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Canvas;
import mgf.tr.utils.Constants;
import mgf.tr.entity.Enemy;
import mgf.tr.entity.EnemyModel;
import mgf.tr.entity.Entity;
import mgf.tr.entity.EntityManager;
import mgf.tr.entity.Wall;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Asus
 */
public final class ScenarioLoader
{
    private ScenarioLoader() {}
    
    /*public static final Scenario loadScenario(Canvas canvas, SpriteLoader sprites, InputStream input) throws ScenarioLoaderException
    {
        return loadScenario(canvas, sprites, loadJson(input));
    }*/
    
    public static Scenario loadScenario(Canvas canvas, SpriteLoader sprites, String stageName) throws ScenarioLoaderException
    {
        File file = new File("data" + File.separator + "stages" + File.separator + stageName + ".json");
        return loadScenario(canvas, sprites, file);
    }
    
    private static Scenario loadScenario(Canvas canvas, SpriteLoader sprites, File file) throws ScenarioLoaderException
    {
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)))
        {
            return loadScenario(canvas, sprites, loadJson(bis));
        }
        catch(IOException ex)
        {
            throw new ScenarioLoaderException("Error scenario json file.", ex);
        }
    }
    
    private static JSONObject loadJson(InputStream input) throws ScenarioLoaderException
    {
        try { return new JSONObject(new JSONTokener(new InputStreamReader(input))); }
        catch(JSONException ex) { throw new ScenarioLoaderException("Malformed scenario json file.", ex); }
    }
    
    public static final Scenario loadScenario(Canvas screenCanvas, SpriteLoader sprites, JSONObject base) throws ScenarioLoaderException
    {
        Scenario scenario = createScenario(screenCanvas, sprites, base);
        
        /* Debug Grid */
        scenario.setEnabledDebugGrid(base.optBoolean(Constants.TAG_DEBUG_GRID, false));
        
        /* Start Lives */
        scenario.getLives().addLives(base.optInt(Constants.TAG_LIVES, 0));
        
        /* Background */
        loadBackground(base, scenario);
        
        /* Start Position */
        loadStartPosition(base, scenario);
        
        /* Enemy Behavior */
        loadEnemyBehavior(base, scenario);
        
        /* Entities */
        loadEntities(base, scenario);
        
        return scenario;
    }
    
    private static Scenario createScenario(Canvas screenCanvas, SpriteLoader sprites, JSONObject base)
    {
        JSONArray array = base.getJSONArray(Constants.TAG_GRID_SIZE);
        if(array == null || array.length() < 2)
            return Scenario.createScenario(screenCanvas, sprites, Constants.CELL_DEFAULT_ROWS, Constants.CELL_DEFAULT_COLUMNS);
        int rows = array.optInt(0, Constants.CELL_DEFAULT_ROWS);
        int columns = array.optInt(1, Constants.CELL_DEFAULT_COLUMNS);
        return Scenario.createScenario(screenCanvas, sprites, rows, columns);
    }
    
    private static void loadBackground(JSONObject base, Scenario scenario)
    {
        String backName = base.optString(Constants.TAG_BACKGROUND, "");
        if(backName.isEmpty())
            return;
        
        File file = new File("data" + File.separator + "sprites" + File.separator + "background" + File.separator + backName + ".png");
        try
        {
            Sprite back = SpriteLoader.createStaticSprite(file);
            scenario.setBackground(back);
        }
        catch(IOException ex) { return; }
    }
    
    private static void loadStartPosition(JSONObject base, Scenario scenario)
    {
        JSONArray array = base.optJSONArray(Constants.TAG_START_POSITION);
        if(array == null || array.length() < 2)
            return;
        Vector2 pos = new Vector2(array.optDouble(0, 0), array.optDouble(1, 0));
        scenario.getShipController().setStartPosition(pos);
    }
    
    private static void loadEnemyBehavior(JSONObject base, Scenario scenario)
    {
        JSONObject jobj = base.optJSONObject(Constants.TAG_ENEMY_BEHAVIOR);
        if(jobj == null)
            return;
        
        scenario.setEnemySpeed(jobj.optDouble(Constants.TAG_SPEED, 10));
        scenario.setEnemySpeedIncrement(jobj.optDouble(Constants.TAG_SPEED_INCREMENT, 2));
        scenario.setEnemyFallAmount(jobj.optDouble(Constants.TAG_FALL_AMOUNT, 20));
    }
    
    private static void loadEntities(JSONObject base, Scenario scenario)
    {
        JSONArray array = base.optJSONArray(Constants.TAG_ENTITIES);
        if(array == null)
            return;
        
        EntityManager entities = scenario.getEntityManager();
        SpriteLoader sprites = scenario.getSpriteLoader();
        BulletManager bullets = scenario.getBulletManager();
        int len = array.length();
        for(int i=0;i<len;i++)
        {
            JSONObject object = array.optJSONObject(i);
            if(object == null)
                continue;
            
            String id = object.optString(Constants.TAG_ID, "");
            if(id.isEmpty())
                continue;
            switch(id)
            {
                case Constants.ID_WALL: {
                    Wall wall = new Wall(sprites, bullets);
                    wall.init();
                    setEntityPosition(object, wall);
                    setEntitySize(object, wall);
                    entities.addEntity(wall);
                } break;
                default: {
                    EnemyModel model = EnemyModel.getModel(id);
                    if(model == null)
                        break;
                    Enemy enemy = model.build(sprites, bullets);
                    setEntityPosition(object, enemy);
                    setEntitySize(object, enemy);
                    entities.addEntity(enemy);
                } break;
                /*case Constants.ID_ENEMY_BASIC: {
                    Enemy enemy = new Enemy(sprites, scenario.getBulletManager(), Constants.SPRITE_EXPL_BLUE);
                    enemy.init();
                    setEntityPosition(object, enemy);
                    setEntitySize(object, enemy);
                    setEnemySprite(sprites, enemy, Constants.SPRITE_ENEMY_BASIC, 5);
                    entities.addEntity(enemy);
                } break;
                case Constants.ID_ENEMY_WARRIOR: {
                    Enemy enemy = new Enemy(sprites, scenario.getBulletManager(), Constants.SPRITE_EXPL_PURPLE);
                    enemy.init();
                    setEntityPosition(object, enemy);
                    setEntitySize(object, enemy);
                    setEnemySprite(sprites, enemy, Constants.SPRITE_ENEMY_WARRIOR, 5);
                    enemy.setHealthPoints(2);
                    entities.addEntity(enemy);
                } break;*/
            }
        }
    }
    
    private static void setEntityPosition(JSONObject json, Entity entity)
    {
        JSONArray array = json.optJSONArray(Constants.TAG_POSITION);
        if(array == null || array.length() < 2)
            entity.setPosition(0, 0);
        else
        {
            int row = array.optInt(0, 0);
            int column = array.optInt(1, 0);
            entity.setPosition(column * Constants.CELL_WIDTH + Constants.CELL_WIDTH / 2, row * Constants.CELL_HEIGHT + Constants.CELL_HEIGHT / 2);
        }
    }
    
    private static void setEntitySize(JSONObject json, Entity entity)
    {
        JSONArray array = json.optJSONArray(Constants.TAG_SIZE);
        if(array == null || array.length() < 2)
            return;
        double width = array.optDouble(0, 1);
        double height = array.optDouble(1, 1);
        Vector2 size = entity.getSize();
        size.x *= width;
        size.y *= height;
        entity.setSize(size);
    }
    
    private static void setEnemySprite(SpriteLoader sprites, Entity entity, String spriteId, double spriteSpeed)
    {
        try
        {
            AnimatedSprite sprite = sprites.getSprite(spriteId);
            if(sprite == null)
                return;
            sprite.setSpeed((float) spriteSpeed);
            ((Enemy) entity).setSprite(sprite);
        }
        catch(Exception ex) { ex.printStackTrace(System.err); }
    }
}
