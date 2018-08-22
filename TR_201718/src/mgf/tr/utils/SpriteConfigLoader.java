/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import kp.jngg.sprite.SpriteLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 *
 * @author Asus
 */
public final class SpriteConfigLoader
{
    private SpriteConfigLoader() {}
    
    private static final File FILE = new File("data" + File.separator + "sprite_config.json");
    
    
    public static final void loadSprites(SpriteLoader sprites)
    {
        JSONObject base;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(FILE)))
        {
            base = new JSONObject(new JSONTokener(new InputStreamReader(bis)));
        }
        catch(JSONException | IOException ex)
        {
            ex.printStackTrace(System.err);
            return;
        }
        
        loadStaticSprites(sprites, base);
        loadSheetSprites(sprites, base);
        loadAnimatedSprites(sprites, base);
    }
    
    private static void loadStaticSprites(SpriteLoader sprites, JSONObject base)
    {
        JSONArray array = base.optJSONArray("static_sprites");
        if(array == null)
            return;
        
        int len = array.length();
        for(int i=0;i<len;i++)
        {
            JSONObject obj = array.optJSONObject(i);
            if(obj == null)
                continue;
            
            String id = obj.optString("id");
            String path = obj.optString("path");
            
            try { sprites.loadStaticSprite(id, path); }
            catch(IOException ex) { ex.printStackTrace(System.err); }
        }
    }
    
    private static void loadSheetSprites(SpriteLoader sprites, JSONObject base)
    {
        JSONArray array = base.optJSONArray("sheet_sprites");
        if(array == null)
            return;
        
        int len = array.length();
        for(int i=0;i<len;i++)
        {
            JSONObject obj = array.optJSONObject(i);
            if(obj == null)
                continue;
            
            String id = obj.optString("id");
            String path = obj.optString("path");
            int x = obj.optInt("offset_x");
            int y = obj.optInt("offset_y");
            int width = obj.optInt("width");
            int height = obj.optInt("height");
            
            try { sprites.loadSheetSprite(id, path, x, y, x + width, y + height); }
            catch(IOException ex) { ex.printStackTrace(System.err); }
        }
    }
    
    private static void loadAnimatedSprites(SpriteLoader sprites, JSONObject base)
    {
        JSONArray array = base.optJSONArray("animated_sprites");
        if(array == null)
            return;
        
        int len = array.length();
        for(int i=0;i<len;i++)
        {
            JSONObject obj = array.optJSONObject(i);
            if(obj == null)
                continue;
            
            String id = obj.optString("id");
            String path = obj.optString("path");
            int x = obj.optInt("offset_x");
            int y = obj.optInt("offset_y");
            int width = obj.optInt("frame_width");
            int height = obj.optInt("frame_height");
            int frames = obj.optInt("frames");
            
            try { sprites.loadAnimatedSprite(id, path, x, y, width, height, frames); }
            catch(IOException ex) { ex.printStackTrace(System.err); }
        }
    }
}
