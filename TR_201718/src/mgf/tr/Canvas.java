/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;
import kp.jngg.Display;

/**
 *
 * @author Asus
 */
public final class Canvas
{
    private final Display display;
    private final BufferedImage canvas;
    private final int width;
    private final int heitgh;
    private final Graphics2D gcanvas;
    
    public Canvas(Display display)
    {
        this.display = Objects.requireNonNull(display);
        this.canvas = new BufferedImage(Constants.CANVAS_WIDTH, Constants.CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.width = Constants.CANVAS_WIDTH;
        this.heitgh = Constants.CANVAS_HEIGHT;
        this.gcanvas = this.canvas.createGraphics();
    }
    
    public final int getWidth() { return width; }
    public final int getHeight() { return heitgh; }
    
    public final Graphics2D getGraphics() { return gcanvas; }
    
    public final void clear(Color color)
    {
        gcanvas.setColor(color);
        gcanvas.fillRect(0, 0, width, heitgh);
    }
    
    public final void draw(Graphics2D g)
    {
        g.drawImage(canvas, 0, 0, display.getWidth(), display.getHeight(), null);
    }
}
