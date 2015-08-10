package estructuras;

import javax.swing.JOptionPane;
import mariomaker.Elemento;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */


/*
    Las filas se agregan en la parte superior
    Las columnas se agregan a la derecha de la matriz
*/

public class MatrizOrtogonal implements Cloneable{
    public MultiNodo inicio, filaActual, columnaActual, filaSiguiente, columnaAnterior;
    int filas, columnas;

    public MatrizOrtogonal() 
    {
        //se inicializa la matriz (inicio)
        inicio = new MultiNodo(null);
        filaActual = columnaActual = inicio;
        filas = 2;
        columnas = 4;
        //se crea la primera fila
        inicio.arriba = new MultiNodo(null);
        inicio.arriba.abajo = inicio;
        int cont = 1;
        //se genera la matriz de 2 X 4
        for(int i = 2; i <= columnas; i++)
        {
            columnaActual.derecha = new MultiNodo(null);
            columnaActual.derecha.izquierda = columnaActual;
            columnaActual = columnaActual.derecha;
        }
        filaActual = filaActual.arriba;
        columnaActual = filaActual;
        columnaAnterior = filaActual.abajo.derecha;
        for(int i = 2; i <= columnas; i++)
        {
            columnaActual.derecha = new MultiNodo(null);
            columnaActual.derecha.izquierda = columnaActual;
            columnaActual = columnaActual.derecha;
            columnaActual.abajo = columnaAnterior;
            columnaAnterior.arriba = columnaActual;
            columnaAnterior = columnaAnterior.derecha;
        }
    }
    
    private void subirFilas()
    {
        filaActual = inicio;
        while(filaActual.arriba != null)
            filaActual = filaActual.arriba;
    }
    
    private void avanzarColumnas()
    {
        columnaActual = inicio;
        while (columnaActual.derecha != null)
            columnaActual = columnaActual.derecha;
    }
    
    public void agregarFila()
    {
        //se avanzan las filas hasta llegar al final
        subirFilas();
        //se crea el primer nodo de la columna a agregar con sus apuntadores
        filaActual.arriba = new MultiNodo(null);
        filaActual.arriba.abajo = filaActual;
        filaActual = filaActual.arriba;
        columnaActual = filaActual;
        columnaAnterior = filaActual.abajo.derecha;
        for(int i = 2; i <= columnas; i++)
        {//se insertan los nodos de la fila que se desa agregar
            columnaActual.derecha = new MultiNodo(null);
            columnaActual.derecha.izquierda = columnaActual;
            columnaActual = columnaActual.derecha;
            columnaActual.abajo = columnaAnterior;
            columnaAnterior.arriba = columnaActual;
            columnaAnterior = columnaAnterior.derecha;
        }
        //se aumneta la cantidad de filas en la matriz
        filas++;
    }
    
    public void agregarColumna()
    {
        //se avanzan las columnas hasta llegar al final
        avanzarColumnas();
        //se crea el primer nodo de la columna a agregar con sus apuntadores
        columnaActual.derecha = new MultiNodo(null);
        columnaActual.derecha.izquierda = columnaActual;
        columnaActual = columnaActual.derecha;
        filaActual = columnaActual;
        filaSiguiente = columnaActual.izquierda.arriba;
        for(int i = 2; i<= filas; i++)
        {//se insertan los nodos de la columna que se desea agregar
            filaActual.arriba = new MultiNodo(null);
            filaActual.arriba.abajo = filaActual;
            filaActual = filaActual.arriba;
            filaActual.izquierda = filaSiguiente;
            filaSiguiente.derecha = filaActual;
            filaSiguiente = filaSiguiente.arriba;
        }
        //se aumenta la cantidad de columnas en la matriz
        columnas++;
    }
    
    public ListaDoble eliminarFila(int fila)
    {
        ListaDoble eliminados = new ListaDoble();
        eliminados.setTipoEstrcutura(2);
        if(fila > 1 && filas > 1)
        {
            //se obtiene la fila que se desea eliminar
            filaActual = inicio;
            for(int i = 1; i < fila; i++)
                filaActual = filaActual.arriba;
            columnaActual = filaActual;        
            //se recorren todas las columnas a eliminar
            while(columnaActual != null)
            {
                if(columnaActual.arriba == null)
                {//se desea eliminar la fila de hasta arriba
                    columnaActual.abajo.arriba = null;
                    columnaActual.abajo = null;
                }
                else
                {//se desea eliminar una fila de en medio
                    columnaActual.arriba.abajo = columnaActual.abajo;
                    columnaActual.abajo.arriba = columnaActual.arriba;
                    columnaActual.arriba = null;
                    columnaActual.abajo = null;
                }
                if(columnaActual.izquierda != null)
                {//existe un elemento a la izquierda de la columna que es esta eliminando
                    columnaActual.izquierda.derecha = null;
                    columnaActual.izquierda = null;
                }
                if(columnaActual.dato != null)
                    eliminados.insertar(columnaActual.dato);
                columnaActual = columnaActual.derecha;
            }
            filas--;
        }
        else
        {//se desea eliminar la fila que contiene inicio
            JOptionPane.showMessageDialog(null, "Se desea eliminar la final que contiene inicio");
        }
        System.out.println("fila: " + filas + 1 + " ELIMINADA");
        return eliminados;
    }
    
    public ListaDoble eliminarColumna(int columna)
    {
        ListaDoble eliminados = new ListaDoble();
        eliminados.setTipoEstrcutura(2);
        if(columna > 1 && columnas > 1)
        {
            //se obtiene la columna que se desea eliminar
            columnaActual = inicio;
            for(int i = 1; i < columna; i++)
                columnaActual = columnaActual.derecha;
            filaActual = columnaActual;        
            //se recorren todas las filas a eliminar
            while(filaActual != null)
            {
                if(filaActual.derecha == null)
                {//se desea eliminar la columna de hasta derecha
                    filaActual.izquierda.derecha = null;
                    filaActual.izquierda = null;
                }
                else
                {//se desea eliminar una columna de en medio
                    filaActual.derecha.izquierda = filaActual.izquierda;
                    filaActual.izquierda.derecha = filaActual.derecha;
                    filaActual.derecha = null;
                    filaActual.izquierda = null;
                }
                if(filaActual.abajo != null)
                {//existe un elemento abajo de la fila que es esta eliminando
                    filaActual.abajo.arriba = null;
                    filaActual.abajo = null;
                }
                if(filaActual.dato != null)
                    eliminados.insertar(filaActual.dato);
                filaActual = filaActual.arriba;
            }
            columnas--;
        }
        else
        {//se desea eliminar la fila que contiene inicio
            JOptionPane.showMessageDialog(null, "Se desea eliminar la final que contiene inicio");
        }
        System.out.println("fila: " + filas + 1 + " ELIMINADA");
        return eliminados;
    }
    
    public String recorrido()
    {
        String salida = "";
        filaActual = columnaActual = inicio;
        int fila = 1, columna = 1;
        while(filaActual != null)
        {
            while(columnaActual != null)
            {
                salida += Integer.toString(fila) + ", " + Integer.toString(columna) + "\n";
                columnaActual = columnaActual.derecha;
                columna++;
            }
            columna = 1;
            fila++;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
        return salida;
    }
    
    public void insertarDato(int fila, int columna, Object dato)
    {
        MultiNodo nodo = inicio;
        for(int i = 1; i < fila; i++)
            nodo = nodo.arriba;
        for(int i = 1; i < columna; i++)
            nodo = nodo.derecha;
        nodo.dato = dato;
    }
    
    public int getTipo(int fila, int columna)
    {
        MultiNodo nodo = inicio;
        for(int i = 1; i < fila; i++)
            nodo = nodo.arriba;
        for(int i = 1; i < columna; i++)
            nodo = nodo.derecha;
        Elemento x = (Elemento) nodo.dato;
        int tipo = x.getTipo();
        return tipo;
    }
    
    public String generarGrafo()
    {
        String grafo = "digraph G\n{\n";
        grafo += "node [shape = box, style = \"rounded, filled\", color = black, fontcolor = white];\n";
        grafo += "style = filled;\n";
        grafo += "bgcolor = lightgray;\n";
        grafo += "orientatio = landscape;\n";
        grafo += "rankdir = LR;\n";
        grafo += "center = true;\n";
        grafo += "edge [arrowhead = dot, arrowtail = dot, color = red, dir = both];\n";
        grafo += "label = \" Matriz ortogonal TABLERO \";\n";
        
        filaActual = columnaActual = inicio;
        Elemento x = null;
        int fila = 1, columna = 1;
        //se generan los nodos de la matriz
        while (filaActual != null)
        {
            while (columnaActual != null) 
            {
                x = (Elemento) columnaActual.dato;
                grafo += "nodo" + ((fila * 10) + columna);
                if(x != null)
                {
                    switch(x.getTipo())
                    {
                        case 1://suelo
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Suelo\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 2:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Pared\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 3:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Ficha\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 4:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Vida\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 5:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Goomba\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 6:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Koopa\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 7:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Personaje principal\n [" + fila + "," + columna +"]\"]";
                            break;
                        case 8:
                            grafo+= "[label = \"Nombre: " + x.getNombre() + "\nTipo: Castillo\n [" + fila + "," + columna +"]\"]";
                            break;
                    }
                }
                else
                {
                    grafo+= "[label = \"Vacio\n [" + fila + "," + columna +"]\"]";
                }
                grafo += ";\n";
                columna++;
                columnaActual = columnaActual.derecha;
            }
            columna = 1;
            fila++;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
        filaActual = columnaActual = inicio;
        fila =1;
        columna = 1;
        //se crean los enlaces de la matriz
        while (filaActual != null)
        {
            while (columnaActual != null) 
            {
                if(columnaActual.arriba != null)
                {
                    grafo += "nodo" + ((fila * 10) + columna) + " -> nodo" + ((fila+1) * 10 + columna) +";\n";
                }
                if(columnaActual.derecha != null)
                {
                    grafo += "nodo" + ((fila * 10) + columna) + " -> nodo" + (fila * 10 + (columna+1)) +";\n";
                }
                columna++;
                columnaActual = columnaActual.derecha;
            }
            columna = 1;
            fila++;
            filaActual = filaActual.arriba;
            columnaActual = filaActual;
        }
        
        grafo += "}";
        return grafo;
    }
    
    
    
    public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    
    
}
