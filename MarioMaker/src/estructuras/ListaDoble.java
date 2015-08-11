/*
 * lista doblemente enlazada con inicio y fin para tener 
 * tener control del primer y ultimo elemento de la misma
 * para poder tratarlo como una pila o una cola.
 *
 */
package estructuras;
import mariomaker.Elemento;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */

public class ListaDoble {
    //ATRIBUTOS
    public NodoSimple inicio, fin;
    //pila o cola
    int tipoEstrcutura;
    int numDatos;
    
    public ListaDoble()
    {
        inicio = null;
        fin = null;
        numDatos = 0;
    }
    
    public boolean estaVacia()
    {
        return inicio == null;
    }
    
    private boolean insertarInicio(Object elemento)
    {
        if(estaVacia())
        {
            inicio = fin = new NodoSimple(elemento);
            return true;
        }
        NodoSimple nuevoNodo = new NodoSimple(elemento);
        inicio.anterior = nuevoNodo;
        nuevoNodo.siguiente = inicio;
        inicio = nuevoNodo;
        return true;
    }
    
    private boolean insertarFinal(Object elemento)
    {
        if(estaVacia())
        {
            inicio = fin = new NodoSimple(elemento);
            return true;
        }
        NodoSimple nuevoNodo = new NodoSimple(elemento);
        fin.siguiente = nuevoNodo;
        nuevoNodo.anterior = fin;
        fin = nuevoNodo;
        return true;
    }
    
    public Object eliminarInicio()
    {
        if(estaVacia())
            return null;
        Object datoEliminado = inicio.dato;
        if(inicio == fin)
        {//es el unico elemento en la lista
            inicio = fin = null;
            numDatos = 0;
            return datoEliminado;
        }
        inicio = inicio.siguiente;
        inicio.anterior.siguiente = null;
        inicio.anterior = null;
        numDatos--;
        return datoEliminado;
    }
    
    public Object eliminarFinal()
    {
        if (estaVacia())
            return null;
        Object datoEliminado = fin.dato;
        if(inicio == fin)
        {
            inicio = fin = null;
            numDatos = 0;
            return datoEliminado;
        }
        fin = fin.anterior;
        fin.siguiente.anterior = null;
        fin.siguiente = null;
        numDatos--;
        return datoEliminado;
    }
    
    public boolean insertar(Object elemento)
    {
        //cantidad de datos insertados en la lista para la representacion grafica
        numDatos++;
        //no importa si es pila o cola los elementos se insertan al final
        return insertarFinal(elemento);
    }
    
    public Object eliminar()
    {
        if(tipoEstrcutura == 1)
        {//la lista (this) se comporta como una pila
            return eliminarFinal();
        }
        else
        {//la lista (this) se comporta como una cola
            return eliminarInicio();
        }
    }
    
    public Object eliminar(int pos)
    {
        if (pos == 1)
            return eliminarInicio();
        else
            if (pos == getNumDatos())
                return eliminarFinal();
            else
            {//se eliminara un elemento del final
                NodoSimple actual = inicio;
                int cont = 1;
                while(actual != null && cont <= pos)
                {
                    if(cont == pos)
                    {//existe un elemento en la posicion deseada
                        actual.siguiente.anterior = actual.anterior;
                        actual.anterior.siguiente = actual.siguiente;
                        actual.siguiente = null;
                        actual.anterior = null;
                        numDatos--;
                        return actual.dato;
                    }
                    cont++;
                    actual = actual.siguiente;
                }
            }
        return null;
    }
    
    public boolean buscarTipo(int tipo)
    {//busca si existe el tipo de personaje deseado dentro de la lista
        NodoSimple actual = inicio;
        while(actual != null)
        {
            Elemento x = (Elemento) actual.dato;
            if(x.getTipo() == tipo)
                return true;
            actual = actual.siguiente;
        }
        return false;
    }
    
    private NodoSimple buscarElemento(int pos)
    {
        NodoSimple actual = inicio;
        int cont = 1;
        while(actual != null)
        {
            if(cont == pos)
                return actual;
            actual = actual.siguiente;
            cont++;
        }
        //no se encontro el Elemento
        return null;
    }
    
    public boolean modificar(int pos, String nombre)
    {
        NodoSimple x = buscarElemento(pos);
        Elemento y = (Elemento) x.dato;
        Elemento ele = new Elemento(nombre, y.getTipo());
        if(x != null)
        {
            x.dato = ele;
            return true;
        }
        return false;
    }
    
    public String recorrido()
    {
        NodoSimple actual = inicio;
        String salida = "";
        while(actual != null)
        {
            Elemento x = (Elemento) actual.dato;
            salida += "Nombre:" + x.getNombre() + " Tipo:" + Integer.toString(x.getTipo()) + "\n";
            actual = actual.siguiente;
        }
        return salida;
    }
    
    public String generarGrafo()
    {
        String grafo = "digraph G\n{\n";
        grafo += "node [shape = box, style = \"rounded, filled\", color = black, fontcolor = white];\n";
        grafo += "style = filled;\n";
        grafo += "bgcolor = lightgray;\n";
        grafo += "orientatio = landscape;\n";
        grafo += "center = true;\n";
        grafo += "edge [arrowhead = odot, arrowtail = odot, color = red, dir = both];\n";
        grafo += "label = \" Lista doblemente enlazada de OBJETOS \";\n";
        
        NodoSimple actual = inicio;
        int cont = 1;
        Elemento x;
        while (actual != null)
        {//se crean los nodos del grafo
            x = (Elemento) actual.dato;
            grafo += "nodo" + cont + "[label = \"Nombre: " + x.getNombre() + "\"];\n";
            cont++;
            actual = actual.siguiente;
        }
        
        actual = inicio;
        cont = 1;
        while (actual != null)
        {//se crean los enlaces entre nodos del grafo
            if(actual.siguiente != null)
            {
                grafo += "nodo" + cont + " -> nodo" + (cont+1) + ";\n";
                cont++;
            }
            actual = actual.siguiente;
        }
        grafo += "}";
        return grafo;
    }
    
    public String resumen()
    {
        NodoSimple actual = inicio;
        int suelos = 0, paredes = 0, fichas = 0, vidas = 0, goombas = 0, koopas = 0, personaje = 0, castillo = 0;
        Elemento x;
        while (actual != null)
        {
            x = (Elemento) actual.dato;
            switch(x.getTipo())
            {
                case 1:
                    suelos++;
                    break;
                case 2:
                    paredes++;
                    break;
                case 3:
                    fichas++;
                    break;
                case 4:
                    vidas++;
                    break;
                case 5:
                    goombas++;
                    break;
                case 6:
                    koopas++;
                    break;
                case 7:
                    personaje++;
                    break;
                case 8:
                    castillo++;
                    break;
            }
            actual = actual.siguiente;
        }
        String salida = "Suelos: " + suelos + "\n";
        salida += "Paredes: " + paredes + "\n";
        salida += "Fichas: " + fichas + "\n";
        salida += "Vidas: " + vidas + "\n";
        salida += "Goomba: " + goombas + "\n";
        salida += "Koopa: " + koopas + "\n";
        salida += "Personaje Principal: " + personaje + "\n";
        salida += "Castillo: " + castillo + "\n";
        return salida;
    }

    public int getTipoEstrcutura() {
        return tipoEstrcutura;
    }

    public void setTipoEstrcutura(int tipoEstrcutura) {
        this.tipoEstrcutura = tipoEstrcutura;
    }

    public int getNumDatos() {
        return numDatos;
    }
}



