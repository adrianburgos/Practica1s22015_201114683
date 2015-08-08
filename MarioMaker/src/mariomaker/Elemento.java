/*
 * Seran los objetos que se agregaran a los 
 */
package mariomaker;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class Elemento {
    private String nombre;
    private int tipo;

    public Elemento(String nombre, int tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
    
}
