/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package estructuras;

import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class MultiNodo {
    //los 4 puntoreros de un nodo de matriz (ortogonal o dispersa)
    public MultiNodo arriba, abajo, izquierda, derecha;
    public Object dato;
    public JLabel lObjeto;

    public MultiNodo(Object dato) 
    {
        arriba = abajo = izquierda = derecha = null;
        this.dato = dato;
        lObjeto = new JLabel();
    }
}
