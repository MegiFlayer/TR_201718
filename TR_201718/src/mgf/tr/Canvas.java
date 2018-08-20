/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import kp.jngg.Display;

/**
 *
 * @author Asus
 */
public final class Canvas
{
    private static final Color CLEAN_COLOR = new Color(1f, 1f, 1f, 0f);
    
    private final BufferedImage canvas;
    private final int width;
    private final int height;
    private final int superWidth;
    private final int superHeight;
    private final boolean deformedExpansion;
    private final Graphics2D gcanvas;
    
    public Canvas(Display display, int width, int height, boolean deformedExpansion)
    {
        this(width, height, display.getWidth(), display.getHeight(), deformedExpansion);
    }
    
    private Canvas(int width, int height, int superWidth, int superHeight, boolean deformedExpansion)
    {
        this.canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        this.superWidth = superWidth;
        this.superHeight = superHeight;
        this.deformedExpansion = deformedExpansion;
        this.gcanvas = this.canvas.createGraphics();
    }
    
    public final int getWidth() { return width; }
    public final int getHeight() { return height; }
    
    public final Canvas createChild(int width, int height, boolean deformedExpansion)
    {
        return new Canvas(width, height, this.width, this.height, deformedExpansion);
    }
    
    public final Graphics2D getGraphics() { return gcanvas; }
    
    public final void clear(Color color)
    {
        gcanvas.setBackground(color);
        gcanvas.clearRect(0, 0, width, height);
    }
    public final void clear() { clear(CLEAN_COLOR); }
    
    public final void draw(Graphics2D g)
    {
        if(deformedExpansion || superWidth == superHeight)
            g.drawImage(canvas, 0, 0, superWidth, superHeight, null);
        else
        {
            if(superWidth > superHeight)
            {
                int newWidth = width * (superHeight / height);
                g.drawImage(canvas, (superWidth - newWidth) / 2, 0, newWidth, superHeight, null);
            }
            else
            {
                int newHeight = height * (superWidth / width);
                g.drawImage(canvas, 0, (superHeight - newHeight) / 2, superWidth, newHeight, null);
            }
        }
    }
}
