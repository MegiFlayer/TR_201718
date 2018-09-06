/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import kp.jngg.font.DefaultFont;
import kp.jngg.input.KeyId;
import kp.jngg.input.Keycode;
import kp.jngg.menu.MenuController;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;

/**
 *
 * @author Marc
 */
public interface Constants
{   
    /* CELLS */
    int CELL_WIDTH = 32;
    int CELL_HEIGHT = 32;
    int CELL_DEFAULT_ROWS = 14;
    int CELL_DEFAULT_COLUMNS = 19;
    
    /* CANVAS */
    int SCREEN_CANVAS_WIDTH = 1280;
    int SCREEN_CANVAS_HEIGHT = 960;
    
    /* BULLETS */
    double BULLET_SHIP_WIDTH = 4;
    double BULLET_SHIP_HEIGHT = 10;
    double BULLET_SHIP_SPEEDX = 0;
    double BULLET_SHIP_SPEEDY = -200;
    
    /* SHIP */
    double SHIP_WIDTH = CELL_WIDTH * 0.8;
    double SHIP_HEIGHT = CELL_HEIGHT;
    double SHIP_TIME_TO_RESPAWN = 3; // 3 Seconds to respawn.
    
    /* LABEL */
    int FONT_SIZE = 12;
    
    /* WALLS */
    double WALL_WIDTH = CELL_WIDTH * 2.5;
    double WALL_HEIGHT = CELL_HEIGHT * 1.5;
    
    /* SCENARIO_LOADER TAGS */
    String TAG_GRID_SIZE = "grid_size";
    String TAG_DEBUG_GRID = "debug_grid";
    String TAG_LIVES = "lives";
    String TAG_START_POSITION = "start_position";
    String TAG_BACKGROUND = "background";
    String TAG_ENTITIES = "entities";
    String TAG_ID = "id";
    String TAG_POSITION = "position";
    String TAG_SIZE = "size";
    String TAG_ENEMY_BEHAVIOR = "enemy_behavior";
    String TAG_SPEED = "speed";
    String TAG_SPEED_INCREMENT = "speed_increment";
    String TAG_FALL_AMOUNT = "fall_amount";
    
    
    /* SCENARIO_LOADER ENTITY IDS */
    String ID_WALL = "wall";
    String ID_ENEMY_BASIC = "enemy_basic";
    String ID_ENEMY_WARRIOR = "enemy_warrior";
    String ID_ENEMY_TANK = "enemy_tank";
    
    /* SPRITES */
    String SPRITE_EXPL_NORMAL = "explosion.normal";
    String SPRITE_EXPL_BLUE = "explosion.blue";
    String SPRITE_EXPL_GREEN = "explosion.green";
    String SPRITE_EXPL_PURPLE = "explosion.purple";
    String SPRITE_EXPL_BIG = "explosion.big";
    String SPRITE_EXPL_BULLET_0 = "explosion.bullet_0";
    String SPRITE_EXPL_BULLET_1 = "explosion.bullet_1";
    String SPRITE_EXPL_BULLET_2 = "explosion.bullet_2";
    String SPRITE_EXPL_BULLET_3 = "explosion.bullet_3";
    
    String SPRITE_ENEMY_BASIC = "enemy.basic";
    String SPRITE_ENEMY_WARRIOR = "enemy.warrior";
    String SPRITE_ENEMY_TANK = "enemy.tank";
    
    /* FONTS */
    DefaultFont DEFAULT_FONT = new DefaultFont("data" + File.separator + "yoster.ttf", 32, Color.WHITE);
    
    /* MENU CONTROLLER CREATOR */
    static void initMenuController(MenuController controller)
    {
        controller.setActionInputId(KeyId.getId(Keycode.VK_ENTER));
        controller.setBackInputId(KeyId.getId(Keycode.VK_ESCAPE));
        controller.setUpInputId(KeyId.getId(Keycode.VK_UP));
        controller.setDownInputId(KeyId.getId(Keycode.VK_DOWN));
        controller.setTitleSize(64);
        controller.setNormalSize(32);
        controller.setSelectedSize(38);
        controller.setDescriptionSize(18);
        controller.setFont(Constants.DEFAULT_FONT.copy());
    }
    
    static Sprite loadBackground(String path)
    {
        File file = new File("data" + File.separator + "sprites" + File.separator + "background" + File.separator + path + ".png");
        try
        {
            return SpriteLoader.createStaticSprite(file);
        }
        catch(IOException ex) { return null; }
    }
}
