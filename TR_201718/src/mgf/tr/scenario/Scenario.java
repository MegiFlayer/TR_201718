/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;
import kp.jngg.input.InputEvent;
import kp.jngg.math.Vector2;
import kp.jngg.sprite.Sprite;
import kp.jngg.sprite.SpriteLoader;
import mgf.tr.Canvas;
import mgf.tr.entity.Entity;
import mgf.tr.entity.EntityManager;
import mgf.tr.entity.EntityType;
import mgf.tr.scenario.label.Lives;
import mgf.tr.scenario.label.Score;
import mgf.tr.scenario.visual.VisualObject;
import mgf.tr.utils.Constants;

/**
 *
 * @author Asus
 */
public final class Scenario
{
    private final SpriteLoader sprites;
    private final EntityManager entities;
    private final BulletManager bullets;
    private final Canvas screenCanvas;
    private final Canvas entityCanvas;
    private final Score score;
    private final Lives lives;
    private final LinkedList<VisualObject> vobjs;
    private Sprite background;
    private boolean enabledGrid;
    private boolean enabledDrawBbox;
    
    private double enemySpeed;
    private double enemySpeedIncrement;
    private double enemyFallAmount;
    private boolean enemyFall;
    
    private boolean firstUpdate = true;
    
    
    private final ShipController ship;
    
    private Scenario(Canvas screenCanvas, Canvas entityCanvas, SpriteLoader sprites)
    {
        this.screenCanvas = Objects.requireNonNull(screenCanvas);
        this.entityCanvas = entityCanvas;
        this.sprites = Objects.requireNonNull(sprites);
        this.entities = new EntityManager();
        this.bullets = new BulletManager(this,
                new Vector2(entityCanvas.getWidth() / 2, entityCanvas.getHeight() / 2),
                new Vector2(entityCanvas.getWidth(), entityCanvas.getHeight()));
        
        this.score = new Score();
        this.lives = new Lives(sprites);
        this.ship = new ShipController(this, lives, entityCanvas);
        this.vobjs = new LinkedList<>();
        
        this.enemySpeed = 10;
        this.enemySpeedIncrement = 2;
        this.enemyFallAmount = 20;
        this.enemyFall = false;
        
        init();
    }
    
    /* Usa este metodo para crear una instancia de Scenario */
    public static final Scenario createScenario(Canvas screenCanvas, SpriteLoader sprites, int rows, int columns)
    {
        Canvas canvas = screenCanvas.createChild(columns * Constants.CELL_WIDTH, rows * Constants.CELL_HEIGHT, false);
        return new Scenario(screenCanvas, canvas, sprites);
    }
    
    private void init()
    {
        /* Situar puntuación arriba a la izquierda */
        score.setPosition(10, 25);
        score.setText("Score:");
        score.setEnabled(true);
        
        /* Situar vidas justo debajo de la puntuación */
        lives.setPosition(10, 40);
        lives.setEnabled(true);
        
        /* Puedes colocar aqui otras cosas a inicializar */
        ship.newShip();
    }
    
    public final SpriteLoader getSpriteLoader() { return sprites; }
    public final BulletManager getBulletManager() { return bullets; }
    public final EntityManager getEntityManager() { return entities; }
    public final ShipController getShipController() { return ship; }
    public final Lives getLives() { return lives; }
    public final Score getScore() { return score; }
    public final Canvas getEntityCanvas() { return entityCanvas; }
    
    public final void setBackground(Sprite background) { this.background = background; }
    public final void setEnabledDebugGrid(boolean flag) { enabledGrid = flag; }
    public final void setEnabledDrawBoundingBox(boolean flag)
    {
        enabledDrawBbox = flag;
        bullets.setEnabledDrawBoundingBox(flag);
    }
    
    public final void setEnemySpeed(double speed) { this.enemySpeed = Math.abs(speed); }
    public final void setEnemySpeedIncrement(double increment) { this.enemySpeedIncrement = Math.abs(increment); }
    public final void setEnemyFallAmount(double amount) { this.enemyFallAmount = Math.abs(amount); }
    
    public final void update(double delta)
    {
        updateEntities(delta);
        updateVisualObjects(delta);
        bullets.update(delta);
        score.update(delta);
        lives.update(delta);
        ship.update(delta);
        
        if(background != null)
            background.update(delta);
        entities.update();
        if(firstUpdate)
            for(Entity e : entities)
                if(e.getEntityType() == EntityType.ENEMY)
                    e.setSpeed(enemySpeed, 0); 
        
        if(firstUpdate)
            firstUpdate = false;
    }
    
    private void updateEntities(double delta)
    {
        final boolean fall = enemyFall;
        if(fall)
        {
            enemySpeed += enemySpeedIncrement;
            enemySpeed *= -1;
            enemySpeedIncrement *= -1;
            enemyFall = false;
        }
        entities.forEachEntity((e) -> {
            if(!e.hasDestroyed())
            {
                e.update(delta);
                if(!e.isAlive())
                    e.destroy();
                else if(e.getEntityType() == EntityType.ENEMY)
                {
                    if(fall)
                    {
                        e.setPosition(e.getPosition().add(0, enemyFallAmount));
                        e.setSpeed(enemySpeed, 0); 
                    }
                    else if(!enemyFall)
                    {
                        double x = e.getPositionX();
                        double midWidth = e.getWidth() / 2;
                        if(enemySpeed > 0 && x + midWidth >= entityCanvas.getWidth())
                            enemyFall = true;
                        else if(enemySpeed < 0 && x - midWidth <= 0)
                            enemyFall = true;
                    }
                }
            }
        });
    }
    
    private void updateVisualObjects(double delta)
    {
        ListIterator<VisualObject> it = vobjs.listIterator();
        while(it.hasNext())
        {
            VisualObject vobj = it.next();
            vobj.update(delta);
            if(vobj.isDead())
                it.remove();
        }
    }
    
    public final void draw(Graphics2D g)
    {
        if(background != null)
            background.draw(g, 0, 0, screenCanvas.getWidth(), screenCanvas.getHeight());

        drawEntities(g);
        score.draw(g);
        lives.draw(g);
    }
    
    private void drawEntities(Graphics2D g)
    {
        final Graphics2D entityG = entityCanvas.getGraphics();
        entityCanvas.clear();
        drawGrid(entityG);
        entities.forEachEntity((e) -> {
            if(!e.hasDestroyed())
            {
                e.draw(entityG);
                if(enabledDrawBbox)
                    e.drawBoundingBox(entityG);
            }
        });
        bullets.draw(entityG);
        drawVisualObjects(entityG);
        entityCanvas.draw(g);
    }
    
    private void drawVisualObjects(Graphics2D g)
    {
        for(VisualObject vobj : vobjs)
            vobj.draw(g);
    }
    
    public final void dispatch(InputEvent event)
    {
        entities.forEachEntity((Entity e) -> {
            if(!e.hasDestroyed())
            {
                e.dispatch(event);
            }
        });
        
        ship.dispatchEvent(event);
        
    }
    
    private void drawGrid(Graphics2D g)
    {
        if(!enabledGrid)
            return;
        final int rows = entityCanvas.getHeight() / Constants.CELL_HEIGHT;
        final int columns = entityCanvas.getWidth()/ Constants.CELL_WIDTH;
        g.setColor(Color.RED);
        for(int row = 0; row < rows; row++)
            for(int column = 0; column < columns; column++)
            {
                g.drawRect(column * Constants.CELL_WIDTH, row * Constants.CELL_HEIGHT,
                        Constants.CELL_WIDTH, Constants.CELL_HEIGHT);
            }
    }
    
    public final void addVisualObject(VisualObject vobj)
    {
        if(vobj == null)
            throw new NullPointerException();
        vobjs.add(vobj);
    }
}
