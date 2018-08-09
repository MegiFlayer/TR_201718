/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr.scenario.label;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Objects;
import kp.jngg.font.Font;
import kp.jngg.math.Vector2;

/**
 *
 * @author Asus
 */
public class Label
{
    protected final Vector2 position = new Vector2();
    protected Font font = Font.getNativeFont("arial", 12, Color.WHITE);
    protected String text;
    protected boolean enabled;
    
    public final void setPosition(double x, double y) { position.set(x, y); }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    public final Vector2 getPosition() { return position.copy(); }
    
    public final void setEnabled(boolean flag) { this.enabled = flag; }
    public final boolean isEnabled() { return enabled; }
    
    public final void setFont(Font font) { this.font = Objects.requireNonNull(font); }
    public final Font getFont() { return font; }
    
    public final void setText(String text) { this.text = Objects.requireNonNull(text); }
    public final String getText() { return text; }
    
    public boolean update(double delta)
    {
        return enabled;
    }
    
    public void draw(Graphics2D g)
    {
        if(enabled)
            font.print(g, text, (int) position.x, (int) position.y);
    }
}
