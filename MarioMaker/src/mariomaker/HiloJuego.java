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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author Adrian
 */
public class HiloJuego implements Runnable{
    
    JLabel lVidas = new JLabel();
    JLabel lPunteo = new JLabel();
    
    JFrame ventanaJuego = new JFrame("Juego");
    JPanel panelMatriz;
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
    static int contTeclado = 0;
    //variables utilizadas para control de vidas y punteo en el juego
    static int punteo = 0;
    static int vidas = 1;
    boolean gano = false;
    String dir = "C:/Users/Adrian/Documents/GitHub/Practica1s22015_201114683/MarioMaker/src";

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
        
        //se crea y agrega el boton para pausar y reanudar el juego
        url = getClass().getResource("/imagenes/pausa.png");
        img = new ImageIcon(url);
        JButton bPausa = new JButton(img);
        bPausa.setBounds(145, 15, 50, 50);
        bPausa.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (cambio) 
                {
                    cambio = false;                    
                }
                else
                {
                    cambio = true;
                }
            }
            
        });
        
        //se crea y agrega el boton para pausar y reanudar el juego
//        url = getClass().getResource("/imagenes/juego.png");
//        img = new ImageIcon(url);
//        JButton bReiniciar = new JButton("matriz");
//        bReiniciar.setBounds(210, 15, 50, 50);
//        bReiniciar.addActionListener(new ActionListener(){
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                matriz = m;
//                x = matriz.clone();
//                m = (MatrizOrtogonal) x;
//            }
//        });
        
        //se crea el boton para generar grafo de matriz
        url = getClass().getResource("/imagenes/matriz.png");
        img = new ImageIcon(url);
        JButton bMatriz = new JButton(img);
        bMatriz.setBounds(80, 15, 50, 50);
        bMatriz.addActionListener(new ActionMatrizGrafica(matriz));
        
        //se agregan los botones de grafica, pausa, play y reinicio a la ventanaJuego
        ventanaJuego.add(bMatriz);
        ventanaJuego.add(bPausa);
        //ventanaJuego.add(bReiniciar);
        
        //se crean los labels de vidas y punteo
        lVidas.setBounds(210, 15, 100, 20);
        lPunteo.setBounds(210, 35, 100, 20);
        
        //se agregan los labels de vidas y punteo a ventanaJuego
        ventanaJuego.add(lVidas);
        ventanaJuego.add(lPunteo);
        
        
        //se establecen los valores iniciales de la ventanaJuego
        ventanaJuego.setBackground(new Color(245, 210, 57));
        ventanaJuego.setLayout(null);
        ventanaJuego.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaJuego.setSize(816, 488);
        ventanaJuego.setLocationRelativeTo(null);
        ventanaJuego.addKeyListener(new Teclado(matriz));
        ventanaJuego.setFocusable(true);
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
                    columnaActual.lObjeto.setIcon(null);
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
        lVidas.setText("Vidas: " + vidas);
        lPunteo.setText("Punteo: " + punteo);
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
                            if(columnaActual.abajo != null)
                            {//se tienen filas abajo
                                Elemento abajo = (Elemento)columnaActual.abajo.dato;
                                if(abajo == null)
                                {//no hay objeto para detenerce
                                    columnaActual.abajo.dato = columnaActual.dato;
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
                                            if(derecha.getTipo() == 7)
                                            {//tiene al personaje principal del lado derecho se resta 1 a la vida
                                                vidas--;
                                            }
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
                                            if(izquierda.getTipo() == 7)
                                            {//tiene al personaje principal del lado izquierdo se resta 1 a la vida
                                                vidas--;
                                            }
                                            x.setDireccion(1);
                                            columnaActual.dato = x;
                                        }                                
                                    }
                                }
                            }
                            else
                                columnaActual.dato = null;
                            break;
                        case 7://luigi
                            if(columnaActual.abajo != null)
                            {//se tienen filas abajo
                                Elemento abajo = (Elemento)columnaActual.abajo.dato;
                                if(abajo == null)
                                {//no hay objeto para detenerce
                                    columnaActual.abajo.dato = columnaActual.dato;
                                    columnaActual.dato = null;
                                }
                                else
                                {
                                    switch(abajo.getTipo())
                                    {
                                        case 3://ficha
                                            columnaActual.abajo.dato = columnaActual.dato;
                                            columnaActual.dato = null;
                                            punteo++;
                                            break;
                                        case 4://vida
                                            columnaActual.abajo.dato = columnaActual.dato;
                                            columnaActual.dato = null;
                                            vidas++;
                                            break;
                                        case 5://goomba
                                            columnaActual.abajo.dato = columnaActual.dato;
                                            columnaActual.dato = null;
                                            break;
                                        case 6://koopa
                                            if(abajo.getDireccion() == 1)
                                                columnaActual.abajo.izquierda.dato = columnaActual.dato;
                                            else
                                                columnaActual.abajo.derecha.dato = columnaActual.dato;
                                            columnaActual.dato = null;
                                            break;
                                        case 8://castillo
                                            columnaActual.abajo.dato = columnaActual.dato;
                                            columnaActual.dato = null;
                                            gano = true;
                                            break;
                                    }
                                }
                            }
                            else
                            {//el personaje cayo al vacio (se muere)
                                vidas = 0;
                                columnaActual.dato = null;
                            }
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
        if (vidas <= 0)
        {
            ejecutando = false;
            url = getClass().getResource("/imagenes/luigipierde.png");
            img = new ImageIcon(url);
            JOptionPane.showMessageDialog(null, img);
        }
    }

    @Override
    public void run() 
    {
        while(ejecutando)
        {
            if(!gano)
            {
                if(cambio)
                {
                    inicializar();
                    graficar();
                    actualizar();
                    try {
                        hilo.sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HiloVista.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    try {
                        hilo.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(HiloVista.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            else
            {
                ejecutando = false;
                url = getClass().getResource("/imagenes/luigi.png");
                img = new  ImageIcon(url);
                JOptionPane.showMessageDialog(null, img);
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
    
    public class ActionMatrizGrafica implements ActionListener
    {
        MatrizOrtogonal matriz;

        public ActionMatrizGrafica(MatrizOrtogonal matriz) 
        {
            this.matriz = matriz;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            FileWriter direccion = null;
            PrintWriter print = null;
            try
            {
                direccion = new FileWriter(dir + "/estructuras/grafo1.dot");
                print = new PrintWriter(direccion);
                print.println(matriz.generarGrafo());
                print.close();
                direccion.close();
            }
            catch(IOException ex)
            {
                System.out.println("no se pudo guardar el archivo");
            }
            try
            {
                ProcessBuilder process;
                process = new ProcessBuilder("C:/Program Files/Graphviz2.38/bin/dot.exe", "-Tpng", "-o", dir + "/estructuras/grafo1.png", dir +  "/estructuras/grafo1.dot");
                process.redirectErrorStream(true);
                process.start();
            }
            catch(Exception ex)
            {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try
            {
                Runtime.getRuntime().exec("cmd /c " + dir + "/estructuras/grafo1.png");
                Runtime.getRuntime().exec("cmd /c " + dir + "/estructuras/grafo1.png");
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
        
    

    public class Teclado implements KeyListener
    {
        MatrizOrtogonal matriz;
        MultiNodo filaActual, columnaActual, personaje;
        public Teclado(MatrizOrtogonal matriz) {
            this.matriz = matriz;
        }
        
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            contTeclado++;
            if (contTeclado % 10000 == 1)
            {
                filaActual = columnaActual = matriz.inicio;
                //se busca al personaje
                while(filaActual != null)
                {
                    while(columnaActual != null)
                    {

                        Elemento x = (Elemento) columnaActual.dato;
                        if(x != null)
                            if(x.getTipo() == 7)//se obtiene el nodo del personaje
                                personaje = columnaActual;
                        columnaActual = columnaActual.derecha;
                    }
                    filaActual = filaActual.arriba;
                    columnaActual = filaActual;
                }
                if (e.VK_LEFT == e.getKeyCode())
                {
                    Elemento x = (Elemento) personaje.dato;
                    if(x.getDireccion() == 1)
                        x.setDireccion(-1);
                    else
                    {
                        if(personaje.izquierda.dato == null)
                        {//el personaje se puede mover para la izquierda
                            personaje.izquierda.dato = x;
                            personaje.dato = null;
                        }
                        else
                        {//hay algun objeto a la izquierda del personaje
                            Elemento izquierda = (Elemento) personaje.izquierda.dato;
                            switch(izquierda.getTipo())
                            {
                                case 3://ficha
                                    personaje.izquierda.dato = x;
                                    personaje.dato = null;
                                    punteo++;
                                    break;
                                case 4://vida
                                    personaje.izquierda.dato = x;
                                    personaje.dato = null;
                                    vidas++;
                                    break;
                                case 5://goomba
                                    vidas--;
                                    break;
                                case 6://koopa
                                    vidas--;
                                    break;
                                case 8://castillo
                                    gano = true;
                                    break;
                            }                   
                        }
                    }
                }
                if (e.VK_RIGHT == e.getKeyCode())
                {
                    Elemento x = (Elemento) personaje.dato;
                    if(x.getDireccion() == -1)
                        x.setDireccion(1);
                    else
                    {
                        if(personaje.derecha.dato == null)
                        {//el personaje se puede mover para la derecha
                            personaje.derecha.dato = x;
                            personaje.dato = null;
                        }
                        else
                        {//hay algun objeto a la izquierda del personaje
                            Elemento derecha = (Elemento) personaje.derecha.dato;
                            switch(derecha.getTipo())
                            {
                                case 3://ficha
                                    personaje.derecha.dato = x;
                                    personaje.dato = null;
                                    punteo++;
                                    break;
                                case 4://vida
                                    personaje.derecha.dato = x;
                                    personaje.dato = null;
                                    vidas++;
                                    break;
                                case 5://goomba
                                    vidas--;
                                    break;
                                case 6://koopa
                                    vidas--;
                                    break;
                                case 8://castillo
                                    gano = true;
                                    break;
                            }                   
                        }
                    }
                }
                if (e.VK_UP == e.getKeyCode())
                {
                    Elemento x = (Elemento) personaje.dato;
                    if(x.getDireccion() == 1)
                    {//el personaje esta viendo hacia la derecha
                        //VERIFICACIONES PARA EL SALTO
                        if(personaje.arriba != null)
                        {
                            if(personaje.arriba.arriba != null)
                            {
                                if(personaje.arriba.arriba.derecha != null)
                                {
                                    if(personaje.arriba.arriba.derecha.dato == null)
                                        personaje.arriba.arriba.derecha.dato = x;
                                    else                                      
                                        personaje.arriba.arriba.dato = x;
                                    personaje.dato = null;
                                }
                            }
                        }
                    }
                    else
                    {//el personaje esta viendo hacia la izquierda
                        //VERIFICACIONES PARA EL SALTO
                        if(personaje.arriba != null)
                        {
                            if(personaje.arriba.arriba != null)
                            {
                                if(personaje.arriba.arriba.izquierda != null)
                                {
                                    if(personaje.arriba.arriba.izquierda.dato == null)
                                        personaje.arriba.arriba.izquierda.dato = x;
                                    else
                                        personaje.arriba.arriba.dato = x;
                                    personaje.dato = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            contTeclado = 0;
        }
        
    }
    
    
}
