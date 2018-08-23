/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.entity;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.AnimatedSprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.scenario.BulletManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Asus
 */
public final class EnemyModel
{
    public final String spriteId;
    public final String explosionSpriteId;
    public final double spriteSpeed;
    
    public final double deltaWidth;
    public final double deltaheight;
    
    public final int healthPoints;
    
    public final String bulletModelId;
    public final int fireRatio;
    
    public final int score;
    public final Color scoreColor;
    
    private EnemyModel(JSONObject base)
    {
        this.spriteId = base.optString("sprite_id");
        this.explosionSpriteId = base.optString("explosion_sprite_id");
        this.spriteSpeed = base.optDouble("sprite_speed");
        
        this.deltaWidth = base.optDouble("delta_width");
        this.deltaheight = base.optDouble("delta_height");
        
        this.healthPoints = base.optInt("health_points");
        
        this.bulletModelId = base.optString("bullet_model_id");
        this.fireRatio = base.optInt("fire_ratio");
        
        this.score = base.optInt("score");
        this.scoreColor = loadColor(base, "score_color");
    }
    
    public final Enemy build(SpriteLoader sprites, BulletManager bullets)
    {
        Enemy enemy = new Enemy(sprites, bullets, explosionSpriteId);
        enemy.init();
        
        try
        {
            AnimatedSprite sprite = sprites.getSprite(spriteId);
            sprite.setLoopMode();
            sprite.setSpeed((float) spriteSpeed);
            sprite.start();
            enemy.setSprite(sprite);
        }
        catch(Exception ex) { ex.printStackTrace(System.err); }
        
        enemy.setSize(enemy.getSize().product(new Vector2(deltaWidth, deltaheight)));
        enemy.setHealthPoints(healthPoints);
        enemy.setBulletModelId(bulletModelId);
        enemy.setFireRatio(fireRatio);
        enemy.setScore(score);
        enemy.setScoreColor(scoreColor);
        enemy.updateFireDelay();
        
        return enemy;
    }
    
    /* MODEL REPOSITORY */
    private static final HashMap<String, EnemyModel> MODELS = new HashMap<>();
    
    public static final EnemyModel getModel(String id) { return MODELS.getOrDefault(id, null); }
    
    public static final void loadEnemyModels()
    {
        JSONObject base;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("data" + File.separator + "enemy_config.json"))))
        {
            base = new JSONObject(new JSONTokener(new InputStreamReader(bis)));
        }
        catch(JSONException | IOException ex)
        {
            ex.printStackTrace(System.err);
            return;
        }
        
        JSONArray array = base.optJSONArray("enemy_models");
        if(array == null)
            return;
        int len = array.length();
        for(int i=0;i<len;i++)
        {
            JSONObject jsonModel = array.optJSONObject(i);
            if(jsonModel == null)
                continue;
            String modelId = jsonModel.optString("id", "");
            if(modelId.isEmpty())
                continue;
            MODELS.put(modelId, new EnemyModel(jsonModel));
        }
    }
    
    private static Color loadColor(JSONObject base, String fieldName)
    {
        JSONArray array = base.optJSONArray(fieldName);
        if(array == null || array.length() < 3)
            return Color.WHITE;
        
        int red = Math.max(0, Math.min(255, array.optInt(0)));
        int green = Math.max(0, Math.min(255, array.optInt(1)));
        int blue = Math.max(0, Math.min(255, array.optInt(2)));
        
        return new Color(red, green, blue);
    }
}
