/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariomaker;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class HiloCarga implements Runnable{
    private static Thread hilo = new Thread();
    private boolean ejecutando = false;
    private Principal ventana = new Principal();
    private URL url = null;
    private SpriteSheet sheet;
    private BufferedImage imagen;
    private int cont = 0;
    private final int ancho = 25, alto = 25;
    public HiloCarga()
    {}
    
    private void inicializar()
    {
        ventana.setVisible(true);
    }
    
    private void actualizar()
    {
        
    }
    
    private void graficar()
    {
        int seleccionado = ventana.radioButtonSeleccionado();
        ImageIcon img = null;
        if(seleccionado != -1)
        {
            switch (seleccionado)
            {
                case 1://suelo
                    url = getClass().getResource("/imagenes/suelo.png");
                    img = new ImageIcon(url);
                    ventana.lImagen.setIcon(img);
                    //ejecutando = false;
                    break;
                case 2://pared
                    url = getClass().getResource("/imagenes/pared.png");
                    img = new ImageIcon(url);
                    ventana.lImagen.setIcon(img);
                    //ejecutando = false;
                    break;
                case 3://ficha
                    url = getClass().getResource("/imagenes/ficha.png");
                    img = new ImageIcon(url);
                    ventana.lImagen.setIcon(img);
                    //ejecutando = false;
                    break;
                case 4://hongo de vida
                    url = getClass().getResource("/imagenes/vida.png");
                    img = new ImageIcon(url);
                    ventana.lImagen.setIcon(img);
                    //ejecutando = false;
                    break;
                case 5://goomba
                    try {
                        url = getClass().getResource("/imagenes/goomba.png");
                        imagen = ImageIO.read(url);
                    } 
                    catch (IOException ex) 
                    {System.out.println(ex.getMessage());}                    
                    sheet = new SpriteSheet(imagen);
                    img = new ImageIcon(sheet.recortar((cont % 8) * 25, 0, ancho, alto));
                    ventana.lImagen.setIcon(img);
                    cont++;
                    try
                    {hilo.sleep(100);}
                    catch(InterruptedException e)
                    {}
                    break;
                case 6://koopa
                    try {
                        url = getClass().getResource("/imagenes/koopader.png");
                        imagen = ImageIO.read(url);
                    } 
                    catch (IOException ex)
                    {System.out.println(ex.getMessage());}                    
                    sheet = new SpriteSheet(imagen);
                    img = new ImageIcon(sheet.recortar((cont % 2) * 25, 0, ancho, alto));
                    ventana.lImagen.setIcon(img);
                    cont++;
                    try
                    {hilo.sleep(175);}
                    catch(InterruptedException e)
                    {}
                    break;
                case 7://luigi
                    try {
                        url = getClass().getResource("/imagenes/luigider.png");
                        imagen = ImageIO.read(url); 
                    } 
                    catch (IOException ex) 
                    {System.out.println(ex.getMessage());}                    
                    sheet = new SpriteSheet(imagen);
                    img = new ImageIcon(sheet.recortar((cont % 3) * 25, 0, ancho, alto));
                    ventana.lImagen.setIcon(img);
                    cont++;
                    try
                    {hilo.sleep(140);}
                    catch(InterruptedException e)
                    {}
                    break;
                case 8://castillo
                    url = getClass().getResource("/imagenes/castillo.png");
                    img = new ImageIcon(url);
                    ventana.lImagen.setIcon(img);
                    //ejecutando = false;
                    break;
            }
        }
    }

    @Override
    public void run() {
        inicializar();
        while(ejecutando)
        {
            actualizar();
            graficar();
        }
        
        detener();
        
    }
    
    public synchronized void iniciar()
    {
        if(ejecutando)
            return;
        ejecutando = true;
        hilo = new Thread(this);
        hilo.start();
    }
    
    public synchronized void detener()
    {
        if(!ejecutando)
            return;
        ejecutando = false;
        try 
        {
            hilo.join();
        } catch (InterruptedException ex) 
        {
            System.out.println("Error al detener el hilo");
        }
    }
    
}
