/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mariomaker;

import estructuras.ListaDoble;
import estructuras.NodoSimple;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author Adrian
 */
public class HiloVista implements Runnable{
    private ListaDoble listaObjetos;
    
    JFrame vistaPrevia = new JFrame("Vista Previa");
    JPanel panel;
    private BufferedImage imagen = null;
    private static Thread hilo = new Thread();
    private boolean ejecutando = false;
    private final int ancho = 25, alto = 25, espacio = 84;
    private SpriteSheet sheet;
    //si surge algun cambio en la lista de elementos
    public static boolean cambio = true;
    
    public HiloVista(ListaDoble listaObjetos)
    {
        this.listaObjetos = listaObjetos;
    }
    
    
    class ActionEliminar implements ActionListener
    {
        int posEliminar;
        
        ActionEliminar(int pos)
        {
            posEliminar = pos;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            Elemento x = (Elemento)listaObjetos.eliminar(posEliminar);
            if(x.getTipo() == 7)
                Principal.luigi = false;
            if(x.getTipo() == 8)
                Principal.meta = false;
            System.out.println(listaObjetos.recorrido());
            cambio = true;
        }
        
    }
    
    class ActionModificar implements ActionListener
    {
        int posModificar;
        JTextField tbNombre;
        
        ActionModificar(int pos, JTextField tbNombre)
        {
            posModificar = pos;
            this.tbNombre = tbNombre;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            listaObjetos.modificar(posModificar, tbNombre.getText());
            System.out.println("Modificado");
        }
        
    }
    
    public void inicializar()
    {
        final int cantidad = listaObjetos.getNumDatos();
        vistaPrevia.getContentPane().removeAll();
        panel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(655, 10 + 35 * cantidad);
            }
        };
    }
    
    private void graficar()
    {
        JScrollPane spVistaPrevia = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spVistaPrevia.setBounds(10, 190, 655, 240);
        vistaPrevia.add(spVistaPrevia);
        NodoSimple actual = listaObjetos.inicio;
        JLabel lObjeto = new JLabel();
        JTextField tbNombre = new JTextField();
        URL url = null;
        ImageIcon img = null;
        panel.setLayout(null);
        panel.setBackground(new Color(250, 218, 87));
        int x = espacio + 20, y = 10;
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
                        lObjeto = new JLabel(img);
                        break;
                    case 2://pared
                        url = getClass().getResource("/imagenes/pared.png");
                        img = new ImageIcon(url);
                        lObjeto = new JLabel(img);
                        break;
                    case 3://ficha
                        url = getClass().getResource("/imagenes/ficha.png");
                        img = new ImageIcon(url);
                        lObjeto = new JLabel(img);
                        break;
                    case 4://hongo de vida
                        url = getClass().getResource("/imagenes/vida.png");
                        img = new ImageIcon(url);
                        lObjeto = new JLabel(img);
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
                        lObjeto = new JLabel(img);
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
                        lObjeto = new JLabel(img);
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
                        lObjeto = new JLabel(img);
                        break;
                    case 8://castillo
                        url = getClass().getResource("/imagenes/castillo.png");
                        img = new ImageIcon(url);
                        lObjeto = new JLabel(img);
                        break;
                }
                lObjeto.setBounds(x, y, ancho, alto);
                x += ancho + espacio;
                //text box nombre de cada objeto
                tbNombre = new JTextField(ele.getNombre());
                tbNombre.setBounds(x, y, 100, alto);
                x += 100 + espacio;
                
                //boton modificar de cada objeto
                url = getClass().getResource("/imagenes/modificar.png");
                img = new ImageIcon(url);
                JButton bModificar = new JButton(img);
                bModificar.setContentAreaFilled(false);
                bModificar.setBounds(x, y, ancho, alto);
                bModificar.addActionListener(new ActionModificar(numElemento, tbNombre));
                x += 25 + espacio;
                //boton eliminar de cada objeto
                url = getClass().getResource("/imagenes/eliminar.png");
                img = new ImageIcon(url);
                JButton bEliminar = new JButton(img);
                bEliminar.setContentAreaFilled(false);
                bEliminar.setBounds(x, y, ancho, alto);
                bEliminar.addActionListener(new ActionEliminar(numElemento));
                panel.add(lObjeto);
                panel.add(tbNombre);
                panel.add(bModificar);
                panel.add(bEliminar);
                panel.setVisible(false);
                panel.setVisible(true);
            }
            actual = actual.siguiente;
            x = espacio + 20;
            y += alto + 10;
        }
        vistaPrevia.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        vistaPrevia.setSize(813, 487);
        url = getClass().getResource("/imagenes/fondo.png");
        img = new ImageIcon(url);
        JLabel lFondoPrevia = new JLabel(img);
        lFondoPrevia.setBounds(0, -5, 800, 460);
        vistaPrevia.add(lFondoPrevia);
        vistaPrevia.setLocationRelativeTo(null);
        vistaPrevia.setLayout(null);
        vistaPrevia.setVisible(true);
        //vistaPrevia.repaint();
        panel.repaint();
        cambio = false;
    }
    
    
    @Override
    public void run() 
    {
        //inicializar();
        while(ejecutando)
        {
            if(cambio)
            {
                inicializar();
                graficar();
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
