/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariomaker;

import estructuras.ListaDoble;
import estructuras.MatrizOrtogonal;
import estructuras.MultiNodo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Adrian
 */
public class HiloJuego implements Runnable{
    JFrame ventanaJuego = new JFrame("Juego");
    JPanel panelMatriz;
    
    int vidas = 1;
    URL url = null;
    ImageIcon img = null;
    MultiNodo filaActual, columnaActual;
    private MatrizOrtogonal matriz;
    private BufferedImage imagen = null;
    private static Thread hilo = new Thread();
    private boolean ejecutando = false;
    private final int ancho = 25, alto = 25, espacio = 10;
    private SpriteSheet sheet;
    //si surge algun cambio en la lista de elementos
    private static boolean cambio = true;

    public HiloJuego(MatrizOrtogonal matriz) 
    {
        this.matriz = matriz;
    }
    
    private void inicializar()
    {
        final int filas = matriz.getFilas();
        final int columnas = matriz.getColumnas();
        ventanaJuego.getContentPane().removeAll();
        //se crean las dimensiones del manel de la matriz
        panelMatriz = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30 + 25 * columnas, 15 + 25 * filas);
            }
        };
        //se establecen los valores iniciales de la ventanaJuego
        ventanaJuego.setBackground(new Color(245, 210, 57));
        ventanaJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaJuego.setSize(863, 488);
        ventanaJuego.setLocationRelativeTo(null);
        ventanaJuego.setLayout(null);
    }
    
    private void graficar()
    {
        
    }

    @Override
    public void run() 
    {
        while(ejecutando)
        {
            if(cambio)
            {
                cambio = false;
                inicializar();
                cambio = false;
                graficar();
                cambio = false;
            }
            try {
                hilo.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloVista.class.getName()).log(Level.SEVERE, null, ex);
            }
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
