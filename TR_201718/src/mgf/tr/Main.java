/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import kp.jngg.Display;
import kp.jngg.DisplayMode;
import kp.jngg.input.Keyboard;

/**
 *
 * @author ferna
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        Display ventana = Display.create("Práctica", DisplayMode.getDisplayMode(1280, 720));
    
        GmLoopClass gl = new GmLoopClass(ventana);
        
        ventana.addInput(new Keyboard());
        ventana.addInputListener(gl);
        
        ventana.setGameLoop(gl);
        ventana.start();
        
        
        
        //Display win = Display.create("Título", DisplayMode.getDisplayMode(1280, 720));
        //System.out.println(DisplayMode.getAllDisplayModes());
        
        /*win.setGameLoop(new Prueba());
        win.getDebugInfo().setEnabled(true);
        win.getDebugInfo().setColor(Color.white);
        win.getDebugInfo().setExactFps(true);
        win.getDebugInfo().setShowMemory(true);*/
        //win.start();
        
    }
    
}
