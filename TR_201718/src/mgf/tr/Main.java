/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgf.tr;

import java.awt.Color;
import java.awt.Font;
import kp.jngg.DebugInfo;
import kp.jngg.Display;
import kp.jngg.DisplayMode;
import kp.jngg.input.Keyboard;

/**
 *
 * @author ferna
 */
public class Main {
    
    /* Enable native 2D hardware acceleration */
    static { System.setProperty("sun.java2d.opengl","True"); }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //System.out.println(DisplayMode.toStringAllDisplayModes());
        Display ventana = Display.create("Space Invaders by FSanz", DisplayMode.getDisplayMode(1280, 960));
        enableDebugInfo(ventana);
        /*ventana.getDebugInfo().setEnabled(true);
        ventana.getDebugInfo().setExactFps(true);
        ventana.getDebugInfo().setColor(Color.GREEN);*/
        //ventana.setFullscreen(true);
    
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
    
    private static void enableDebugInfo(Display display)
    {
        if(Config.getBoolean("debug.enabled_info", false))
        {
            DebugInfo info = display.getDebugInfo();
            info.setEnabled(true);
            
            if(Config.getBoolean("debug.enabled_memory", false))
                info.setShowMemory(true);
            
            if(Config.getBoolean("debug.enabled_fps_physics", false))
                info.setShowUpdateFps(true);
            
            info.setColor(Config.getColor("debug.info_color", Color.MAGENTA));
            
            info.setFont(new Font("arial", Font.BOLD, Config.getInt("debug.info_size", 24)));
        }
    }
    
}
