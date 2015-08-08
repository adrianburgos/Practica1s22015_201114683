/*
 * Clase NodoSimple utilizada para el nodo de cualquier estructura
 */
package estructuras;

import javax.swing.JLabel;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class NodoSimple {
    //ATRIBUTOS
    public Object dato;
    public NodoSimple siguiente, anterior;
    public JLabel lObjeto;
    
    //METODOS
    public NodoSimple(Object nuevoDato)
    {
        this.dato = nuevoDato;
        siguiente = null;
        anterior = null;
        lObjeto = new JLabel();
    }
}
