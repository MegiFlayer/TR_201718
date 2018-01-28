/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import kp.jngg.Display;
import kp.jngg.DisplayMode;

/**
 *
 * @author ferna
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Display win = Display.create("Título", DisplayMode.getDisplayMode(800, 600));
        System.out.println(DisplayMode.getAllDisplayModes());
        
        win.setGameLoop(new Prueba());
        win.getDebugInfo().setEnabled(true);
        win.getDebugInfo().setColor(Color.white);
        win.getDebugInfo().setExactFps(true);
        win.getDebugInfo().setShowMemory(true);
        win.start();
        
    }
    
}
