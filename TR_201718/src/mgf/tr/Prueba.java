/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.awt.Graphics2D;
import kp.jngg.GameLoop;

/**
 *
 * @author ferna
 */
public class Prueba implements GameLoop{

    private double pX;
    
    @Override
    public void init() {
    
        pX = 0;
        
    }

    @Override
    public void draw(Graphics2D gd) {
    
        gd.setColor(Color.red);
        gd.fillRect(0, 0, 800, 600);
        gd.setColor(Color.blue);
        gd.fillRect((int) (20 + pX), 20, 30, 100);
        gd.setColor(Color.orange);
        gd.fillPolygon(new int []{50,60,80,90,70}, new int []{60,90,90,60,40}, 5);
        
    }

    @Override
    public void update(double delta) {
    
        pX += 20 * delta;
        
    }
    
    
    
}
