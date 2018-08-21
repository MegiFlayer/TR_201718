/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

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
    
    
    /* SCENARIO_LOADER ENTITY IDS */
    String ID_WALL = "wall";
    String ID_BASIC_ENEMY = "basic_enemy";
    
    /* SPRITES */
    String SPRITE_EXPL_NORMAL = "explosion.normal";
    String SPRITE_EXPL_BLUE = "explosion.blue";
    String SPRITE_EXPL_GREEN = "explosion.green";
    String SPRITE_EXPL_PURPLE = "explosion.purple";
    String SPRITE_EXPL_BIG = "explosion.big";
    
    String SPRITE_ENEMY_BASIC = "enemy.basic";
    String SPRITE_ENEMY_WARRIOR = "enemy.warrior";
    String SPRITE_ENEMY_TANK = "enemy.tank";
}
