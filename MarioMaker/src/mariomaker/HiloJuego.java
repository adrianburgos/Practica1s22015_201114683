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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    //se clona la matriz para poder reiniciar el juego
    private Object x;
    private MatrizOrtogonal m;
    private BufferedImage imagen = null;
    private static Thread hilo = new Thread();
    private boolean ejecutando = false;
    private final int ancho = 25, alto = 25, espacio = 10;
    private SpriteSheet sheet;
    //si surge algun cambio en la lista de elementos
    private static boolean cambio = true;
    private int cont = 0;

    public HiloJuego(MatrizOrtogonal matriz) 
    {
        this.matriz = matriz;
        x = matriz.clone();
        m = (MatrizOrtogonal) x;
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
        ventanaJuego.setSize(816, 488);
        ventanaJuego.setLocationRelativeTo(null);
        ventanaJuego.setLayout(null);
    }
    
    private void graficar()
    {
        JScrollPane spMatriz = new JScrollPane(panelMatriz, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //se establece la posicion de los ScrollPane
        spMatriz.setBounds(15, 80, 770, 355);
        //obtiene la direccion del presonaje que se va a graficar
        int direccion = 1;
        filaActual = columnaActual = matriz.inicio;
        int fila = 1, columna = 1;
        while(filaActual != null)
        {
            while(columnaActual != null)
            {
                if(columnaActual.dato != null)
                {
                    //se obtiene el tipo del objeto de la fila y columna deseadas
                    int seleccionado = matriz.getTipo(fila, columna);
                    if(seleccionado != -1)
                    {
                        switch (seleccionado)
                        {
                            case 1://suelo
                                url = getClass().getResource("/imagenes/suelo.png");
                                img = new ImageIcon(url);
                                break;
                            case 2://pared
                                url = getClass().getResource("/imagenes/pared.png");
                                img = new ImageIcon(url);
                                break;
                            case 3://ficha
                                url = getClass().getResource("/imagenes/ficha.png");
                                img = new ImageIcon(url);
                                break;
                            case 4://hongo de vida
                                url = getClass().getResource("/imagenes/vida.png");
                                img = new ImageIcon(url);
                                break;
                            case 5://goomba
                                try 
                                {
                                    Elemento x = (Elemento) columnaActual.dato;
                                    direccion = x.getDireccion();
                                    if(direccion == 1)
                                        url = getClass().getResource("/imagenes/goomba.png");
                                    else
                                        url = getClass().getResource("/imagenes/goombaizq.png");
                                    imagen = ImageIO.read(url);
                                } 
                                catch (IOException ex) 
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar((cont % 8) * 25, 0, ancho, alto));
                                cont++;
                                break;
                            case 6://koopa
                                try {
                                    Elemento x = (Elemento) columnaActual.dato;
                                    direccion = x.getDireccion();
                                    if(direccion == 1)
                                        url = getClass().getResource("/imagenes/koopader.png");
                                    else
                                        url = getClass().getResource("/imagenes/koopaizq.png");
                                    imagen = ImageIO.read(url);
                                } 
                                catch (IOException ex)
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar((cont % 2) * 25, 0, ancho, alto));
                                cont++;
                                break;
                            case 7://luigi
                                try {
                                    Elemento x = (Elemento) columnaActual.dato;
                                    direccion = x.getDireccion();
                                    if(direccion == 1)
                                        url = getClass().getResource("/imagenes/luigider.png");
                                    else
                                        url = getClass().getResource("/imagenes/luigiizq.png");
                                    imagen = ImageIO.read(url); 
                                } 
                                catch (IOException ex) 
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar((cont % 3) * 25, 0, ancho, alto));
                                cont++;
                                break;
                            case 8://castillo
                                url = getClass().getResource("/imagenes/castillo.png");
                                img = new ImageIcon(url);
                                break;
                        }
                        columnaActual.lObjeto.setIcon(img);
                    }
                }
                else
                {
                    url = getClass().getResource("/imagenes/vacio.png");
                    img = new ImageIcon(url);
                    columnaActual.lObjeto.setIcon(img);
                }
                columnaActual.lObjeto.setBounds(15 + (columna - 1) * ancho, (15 + 25 * matriz.getFilas()) - (fila * alto), ancho, alto);
                panelMatriz.add(columnaActual.lObjeto);
                columnaActual = columnaActual.derecha;
                columna++;
            }
            fila++;
            columna = 1;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
        panelMatriz.setLayout(null);
        
        //se agrega el spMatriz a la ventana
        ventanaJuego.add(spMatriz);
        //se establecen las preferencias del panelMatriz
        panelMatriz.setBackground(new Color(250, 218, 87));
        ventanaJuego.setVisible(true);
    }
    
    private void actualizar()
    {
        int direccion = 1;
        int fila = 1 ,columna = 1;
        filaActual = columnaActual = matriz.inicio;
        while(filaActual != null)
        {
            while (columnaActual != null)
            {
                Elemento x = (Elemento) columnaActual.dato;
                if(x != null)
                {
                    switch(x.getTipo())
                    {
                        //el objeto en la columna es goomba o koopa
                        case 5://goomba
                        case 6://koopa
                            Elemento abajo = (Elemento)columnaActual.abajo.dato;
                            if(abajo == null)
                            {
                                if(columnaActual.abajo != null)
                                {//se tienen filas abajo
                                    columnaActual.abajo.dato = columnaActual.dato;
                                }
                                columnaActual.dato = null;
                            }
                            else
                            {//si hay algun objeto abajo se mueve en la direccion que le corresponde
                                direccion = x.getDireccion();
                                if(direccion == 1)
                                {//el objeto se tiene que mover a la derecha
                                    Elemento derecha = (Elemento) columnaActual.derecha.dato;
                                    if(derecha == null)
                                    {//se mueve el objeto a la derecha
                                        columnaActual.derecha.dato = columnaActual.dato;
                                        columnaActual.dato = null;
                                        columnaActual = columnaActual.derecha;
                                    }
                                    else
                                    {//se le cambia la direccion al objeto
                                        x.setDireccion(-1);
                                        columnaActual.dato = x;
                                    }
                                }
                                else
                                {//el objeto se tiene que mover a la izquierda
                                    Elemento izquierda = (Elemento) columnaActual.izquierda.dato;
                                    if(izquierda == null)
                                    {//se mueve el objeto a la derecha
                                        columnaActual.izquierda.dato = columnaActual.dato;
                                        columnaActual.dato = null;
                                    }
                                    else
                                    {//se le cambia la direccion al objeto
                                        x.setDireccion(1);
                                        columnaActual.dato = x;
                                    }                                
                                }
                            }
                            break;
                        case 7://luigi
                            break;
                        case 8://castillo
                            break;
                    }
                }
                columna++;
                columnaActual = columnaActual.derecha;
            }
            fila++;
            columna = 1;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
    }

    @Override
    public void run() 
    {
        while(ejecutando)
        {
            if(cambio)
            {
                inicializar();
                graficar();
                actualizar();
            }
            try {
                hilo.sleep(300);
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
