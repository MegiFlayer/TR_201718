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
import java.io.InputStreamReader;
import java.util.HashMap;
import kp.jngg.json.JSONArray;
import kp.jngg.json.JSONException;
import kp.jngg.json.JSONObject;
import kp.jngg.json.JSONTokener;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.entity.Entity;

/**
 *
 * @author Asus
 */
public final class BulletModel
{
    public final String spriteId;
    public final double spriteSpeed;
    public final double spriteWidth;
    public final double spriteHeight;
    
    public final double bboxLocalX;
    public final double bboxLocalY;
    public final double bboxWidth;
    public final double bboxHeight;
    
    public final double speed;
    
    public final int power;
    public final boolean ignoreAllies;
    public final int explosionId;
    
    private BulletModel(JSONObject base)
    {
        this.spriteId = base.optString("sprite_id", "");
        this.spriteSpeed = base.optDouble("sprite_speed", 1);
        this.spriteWidth = base.optDouble("sprite_width", 1);
        this.spriteHeight = base.optDouble("sprite_height", 1);
        
        this.bboxLocalX = base.optDouble("bbox_x", 0);
        this.bboxLocalY = base.optDouble("bbox_y", 0);
        this.bboxWidth = base.optDouble("bbox_width", 1);
        this.bboxHeight = base.optDouble("bbox_height", 1);
        
        this.speed = base.optDouble("speed", 1);
        
        this.power = base.optInt("power", 1);
        this.ignoreAllies = base.optBoolean("ignore_allies");
        this.explosionId = base.optInt("explosion_id", 0);
    }
    
    public final Bullet build(Entity owner, SpriteLoader sprites) { return new Bullet(owner, sprites, this); }
    
    
    
    
    /* MODEL REPOSITORY */
    private static final HashMap<String, BulletModel> MODELS = new HashMap<>();
    
    public static final BulletModel getModel(String id) { return MODELS.getOrDefault(id, null); }
    
    public static final void loadBulletModels()
    {
        JSONObject base;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("data" + File.separator + "bullet_config.json"))))
        {
            base = new JSONObject(new JSONTokener(new InputStreamReader(bis)));
        }
        catch(JSONException | IOException ex)
        {
            ex.printStackTrace(System.err);
            return;
        }
        
        JSONArray array = base.optJSONArray("bullet_models");
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
            MODELS.put(modelId, new BulletModel(jsonModel));
        }
    }
}
