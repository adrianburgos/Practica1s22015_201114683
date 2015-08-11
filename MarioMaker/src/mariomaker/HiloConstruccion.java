/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariomaker;

import estructuras.ListaDoble;
import estructuras.MatrizOrtogonal;
import estructuras.MultiNodo;
import estructuras.NodoSimple;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
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
import javax.swing.JSpinner;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class HiloConstruccion implements Runnable {
    public static JFrame ventanaConstruccion = new JFrame("Construccion");
    JPanel panelLista, panelMatriz;
    
    URL url = null;
    ImageIcon img = null;
    MultiNodo filaActual, columnaActual;
    private ListaDoble listaObjetos;
    private MatrizOrtogonal matriz;
    private BufferedImage imagen = null;
    private static Thread hilo = new Thread();
    private boolean ejecutando = false;
    private final int ancho = 25, alto = 25, espacio = 10;
    private SpriteSheet sheet;
    //si surge algun cambio en la lista de elementos
    private static boolean cambio = true;
    String dir = "C:/Users/Adrian/Documents/GitHub/Practica1s22015_201114683/MarioMaker/src";
    
    public HiloConstruccion(ListaDoble listaObjetos, MatrizOrtogonal matriz)
    {
        this.listaObjetos = listaObjetos;
        this.matriz = matriz;
    }
    
    public void inicializar()
    {
        final int cantidad = listaObjetos.getNumDatos();
        final int filas = matriz.getFilas();
        final int columnas = matriz.getColumnas();
        ventanaConstruccion.getContentPane().removeAll();
        //se crean las dimensiones del panel de la lista
        panelLista = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(10 + 25 * cantidad, 50);
            }
        };
        //se crean las dimensiones del manel de la matriz
        panelMatriz = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30 + 25 * columnas, 15 + 25 * filas);
            }
        };
        
        //se establecen los valores iniciales de la ventanaConstruccion
        ventanaConstruccion.setBackground(new Color(245, 210, 57));
        ventanaConstruccion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaConstruccion.setSize(863, 488);
        ventanaConstruccion.setLocationRelativeTo(null);
        ventanaConstruccion.setLayout(null);
        
        //botones para agregar filas y columnas
        url = getClass().getResource("/imagenes/agregar1.png");
        img = new ImageIcon(url);
        JButton bAgregarFila = new JButton(img);
        JButton bAgregarColumna = new JButton(img);
        //botones para eliminar filas y columnas
        url = getClass().getResource("/imagenes/reducir.png");
        img = new ImageIcon(url);
        JButton bEliminarFila = new JButton(img);
        JButton bEliminarColumna = new JButton(img);
        //posicion de los 4 botones
        bAgregarFila.setBounds(15, 75, ancho + 1, alto + 1);
        bAgregarColumna.setBounds(55, 35, ancho + 1, alto + 1);
        bEliminarFila.setBounds(15, 115, ancho + 1, alto + 1);
        bEliminarColumna.setBounds(95, 35, ancho + 1, alto + 1);
        //se agregan los metodos de agregar filas y columnas
        bAgregarFila.addActionListener(new ActionAgregar("fila"));
        bAgregarColumna.addActionListener(new ActionAgregar("columna"));
        
        //spinners para la fila o columna que se desea eliminar
        JSpinner sFila = new JSpinner();
        JSpinner sColumna = new JSpinner();
        sFila.setValue(2);
        sColumna.setValue(2);
        sFila.setBounds(10, 140, ancho + 11, alto + 1);
        sColumna.setBounds(90, 10, ancho + 11, alto);
        
        //se agregan los metodos de eliminar filas y columnas
        bEliminarFila.addActionListener(new ActionEliminar("fila", sFila));
        bEliminarColumna.addActionListener(new ActionEliminar("columna", sColumna));
        
        //se crea y agrega el boton para continuar a la pantalla de juego
        url = getClass().getResource("/imagenes/juego.png");
        img = new ImageIcon(url);
        JButton bJuego = new JButton(img);
        bJuego.setBounds(790, 75, 50, 50);
        bJuego.addActionListener(new ActionJuego(matriz));
        ventanaConstruccion.add(bJuego);
        
        //se crea y agrega el boton para generar grafo de listaObjetos
        url = getClass().getResource("/imagenes/lista.png");
        img = new ImageIcon(url);
        JButton bLita = new JButton(img);
        bLita.setBounds(790, 175, 50, 50);
        bLita.addActionListener(new ActionLista(listaObjetos));
        bLita.setContentAreaFilled(false);
        ventanaConstruccion.add(bLita);
        
        //se crea y agrega el boton para generar el resumen de listaObjetos
        url = getClass().getResource("/imagenes/resumen.png");
        img = new ImageIcon(url);
        JButton bResumen = new JButton(img);
        bResumen.setBounds(790, 275, 50, 50);
        bResumen.addActionListener(new ActionResumen(listaObjetos));
        
        //se crea y agrega el boton para generar grafo de matriz
        url = getClass().getResource("/imagenes/matriz.png");
        img = new ImageIcon(url);
        JButton bMatriz = new JButton(img);
        bMatriz.setBounds(790, 375, 50, 50);
        bMatriz.setContentAreaFilled(false);
        bMatriz.addActionListener(new ActionMatrizGrafica(matriz));
        
        //se agregan los 4 botones a la ventanaConstruccion
        ventanaConstruccion.add(bAgregarFila);
        ventanaConstruccion.add(bAgregarColumna);
        ventanaConstruccion.add(bEliminarFila);
        ventanaConstruccion.add(bEliminarColumna);
        //se agregan los spinners a la ventanaConstruccion
        ventanaConstruccion.add(sFila);
        ventanaConstruccion.add(sColumna);
        //se agregan los botones de la matriz y el boton de resumen
        ventanaConstruccion.add(bResumen);
        ventanaConstruccion.add(bMatriz);
    }
    
    public void graficar()
    {
        JScrollPane spLista = new JScrollPane(panelLista, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane spMatriz = new JScrollPane(panelMatriz, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //se establece la posicion de los ScrollPane
        spLista.setBounds(135, 10, 650, 55);
        spMatriz.setBounds(55, 75, 730, 360);
        
        //cargar lista al panel
        NodoSimple actual = listaObjetos.inicio;
        JLabel lLista = new JLabel();
        int x = espacio, y = 10;
        int numElemento = 0;
        while(actual != null)
        {
            numElemento++;
            Elemento ele = (Elemento) actual.dato;
            int seleccionado = ele.getTipo();
            if(seleccionado != -1)
            {
                switch (seleccionado)
                {
                    case 1://suelo
                        url = getClass().getResource("/imagenes/suelo.png");
                        img = new ImageIcon(url);
                        lLista = new JLabel(img);
                        break;
                    case 2://pared
                        url = getClass().getResource("/imagenes/pared.png");
                        img = new ImageIcon(url);
                        lLista = new JLabel(img);
                        break;
                    case 3://ficha
                        url = getClass().getResource("/imagenes/ficha.png");
                        img = new ImageIcon(url);
                        lLista = new JLabel(img);
                        break;
                    case 4://hongo de vida
                        url = getClass().getResource("/imagenes/vida.png");
                        img = new ImageIcon(url);
                        lLista = new JLabel(img);
                        break;
                    case 5://goomba
                        try 
                        {
                            url = getClass().getResource("/imagenes/goomba.png");
                            imagen = ImageIO.read(url);
                        } 
                        catch (IOException ex) 
                        {System.out.println(ex.getMessage());}                    
                        sheet = new SpriteSheet(imagen);
                        img = new ImageIcon(sheet.recortar(0, 0, ancho, alto));
                        lLista = new JLabel(img);
                        break;
                    case 6://koopa
                        try {
                            url = getClass().getResource("/imagenes/koopader.png");
                            imagen = ImageIO.read(url);
                        } 
                        catch (IOException ex)
                        {System.out.println(ex.getMessage());}                    
                        sheet = new SpriteSheet(imagen);
                        img = new ImageIcon(sheet.recortar(0, 0, ancho, alto));
                        lLista = new JLabel(img);
                        break;
                    case 7://luigi
                        try {
                            url = getClass().getResource("/imagenes/luigider.png");
                            imagen = ImageIO.read(url); 
                        } 
                        catch (IOException ex) 
                        {System.out.println(ex.getMessage());}                    
                        sheet = new SpriteSheet(imagen);
                        img = new ImageIcon(sheet.recortar(25, 0, ancho, alto));
                        lLista = new JLabel(img);
                        break;
                    case 8://castillo
                        url = getClass().getResource("/imagenes/castillo.png");
                        img = new ImageIcon(url);
                        lLista = new JLabel(img);
                        break;
                }
                lLista.setBounds(x, y, ancho, alto);
                x += ancho + espacio;
                panelLista.add(lLista);
            }
            actual = actual.siguiente;
        }
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
                                    url = getClass().getResource("/imagenes/goomba.png");
                                    imagen = ImageIO.read(url);
                                } 
                                catch (IOException ex) 
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar(0, 0, ancho, alto));
                                break;
                            case 6://koopa
                                try {
                                    url = getClass().getResource("/imagenes/koopader.png");
                                    imagen = ImageIO.read(url);
                                } 
                                catch (IOException ex)
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar(0, 0, ancho, alto));
                                break;
                            case 7://luigi
                                try {
                                    url = getClass().getResource("/imagenes/luigider.png");
                                    imagen = ImageIO.read(url); 
                                } 
                                catch (IOException ex) 
                                {System.out.println(ex.getMessage());}                    
                                sheet = new SpriteSheet(imagen);
                                img = new ImageIcon(sheet.recortar(25, 0, ancho, alto));
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
                System.out.println("Action del label: " + fila + ", " + columna);
                MouseListener[] mouse = columnaActual.lObjeto.getMouseListeners();
                for(int i = 0; i < mouse.length; i++)
                {
                    columnaActual.lObjeto.removeMouseListener(mouse[i]);
                }
                columnaActual.lObjeto.addMouseListener(new ActionMatriz(fila, columna));
                panelMatriz.add(columnaActual.lObjeto);
                columnaActual = columnaActual.derecha;
                columna++;
            }
            fila++;
            columna = 1;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
        panelLista.setLayout(null);
        panelMatriz.setLayout(null);
        panelLista.setBounds(0, 0, 10 + 25 * listaObjetos.getNumDatos(), 50);
        panelLista.setBackground(new Color(250, 218, 87));
        panelMatriz.setBackground(new Color(250, 218, 87));
        ventanaConstruccion.add(spLista);
        ventanaConstruccion.add(spMatriz);
        ventanaConstruccion.setVisible(true);   
        System.out.println("Cambio");
        cambio = false;
    }
    
    public class ActionEliminar implements ActionListener
    {
        String tipo;
        JSpinner pos;

        public ActionEliminar(String tipo, JSpinner pos) 
        {
            this.tipo = tipo;
            this.pos = pos;
        }        

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            ListaDoble eliminados = new ListaDoble();
            if(tipo.equals("fila"))
            {//se agregara una fila a la matriz
                eliminados = matriz.eliminarFila((int) pos.getValue());
            }
            if (tipo.equals("columna"))
            {//se agregara una columna a la matriz
                eliminados = matriz.eliminarColumna((int) pos.getValue());
            }
            while(!eliminados.estaVacia())
                listaObjetos.insertar(eliminados.eliminar());
            cambio = true;
        }
    }
    
    
    public class ActionAgregar implements ActionListener
    {
        String tipo;

        public ActionAgregar(String tipo) 
        {
            this.tipo = tipo;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(tipo.equals("fila"))
            {//se agregara una fila a la matriz
                matriz.agregarFila();
            }
            if (tipo.equals("columna"))
            {//se agregara una columna a la matriz
                matriz.agregarColumna();
            }
            cambio = true;
        }
        
    }
    
    public class ActionJuego implements ActionListener
    {
        MatrizOrtogonal matriz = null;

        public ActionJuego(MatrizOrtogonal matriz) 
        {
            this.matriz = matriz;
        }
        
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            HiloJuego juego = new HiloJuego(matriz);
            juego.iniciar();
            HiloConstruccion.ventanaConstruccion.setVisible(false);
            hilo.stop();
        }
    }
    
    public class ActionMatriz extends MouseAdapter
    {
        int fila, columna;
        MultiNodo nodo;
        public ActionMatriz(int fila, int columna)
        {
            this.fila = fila;
            this.columna = columna;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) 
        {
            nodo = matriz.inicio;
            //se busca el nodo deseado
            //recorriendo las filas y columnas deseadas
            for(int i = 1; i < fila; i++)
                nodo = nodo.arriba;
            for(int i = 1; i < columna; i++)
                nodo = nodo.derecha;
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                if(!listaObjetos.estaVacia())
                {
                    if(nodo.dato == null)
                    {
                        Elemento x = (Elemento)listaObjetos.eliminar();
                        System.out.println("Nombre: " + x.getNombre() + "\nTipo: " + x.getTipo());
                        matriz.insertarDato(fila, columna, x);
                    }
                }
            }
            if(e.getButton() == MouseEvent.BUTTON3)
            {
                if(nodo.dato != null)
                {
                    listaObjetos.insertar(nodo.dato);
                    nodo.dato = null;
                    url = getClass().getResource("/imagenes/vacio.png");
                    img = new ImageIcon(url);
                    nodo.lObjeto.setIcon(img);
                }
            }
            cambio = true;
        }
    }
    
    public class ActionResumen implements ActionListener
    {
        ListaDoble listaObjetos;

        public ActionResumen(ListaDoble listaObjetos) 
        {
            this.listaObjetos = listaObjetos;
        }

        @Override
        public void actionPerformed(ActionEvent e) 
        {
            JOptionPane.showMessageDialog(null, listaObjetos.resumen());
        }
    }
    
    public class ActionLista implements ActionListener
    {
        ListaDoble listaObjetos;

        public ActionLista(ListaDoble listaObjetos) 
        {
            this.listaObjetos = listaObjetos;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            FileWriter direccion = null;
            PrintWriter print = null;
            try
            {
                direccion = new FileWriter(dir + "/estructuras/grafo.dot");
                print = new PrintWriter(direccion);
                print.println(listaObjetos.generarGrafo());
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
                process = new ProcessBuilder("C:/Program Files/Graphviz2.38/bin/dot.exe", "-Tpng", "-o", dir + "/estructuras/grafo.png", dir +  "/estructuras/grafo.dot");
                process.redirectErrorStream(true);
                process.start();
            }
            catch(Exception ex)
            {
                System.out.println("ERROR: " + ex.getMessage());
            }
            try {
                hilo.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloConstruccion.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                Runtime.getRuntime().exec("cmd /c " + dir + "/estructuras/grafo.png");
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
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
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
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
