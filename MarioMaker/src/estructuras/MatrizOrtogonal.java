package estructuras;

import mariomaker.Elemento;

/**
 * @author Adrian Fernando Burgos Herrera
 * 2011 14683
 */


/*
    Las filas se agregan en la parte superior
    Las columnas se agregan a la derecha de la matriz
*/

public class MatrizOrtogonal {
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
    
    public void eliminarFila(int fila)
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
            {//se desean eliminar los elementos de hasta arriba
                if(columnaActual.abajo == null)
                {//se desea eliminar la unica fila
                    columnaActual.abajo.arriba = null;
                    columnaActual.abajo = null;
                }
            }
            columnaActual = columnaActual.derecha;
        }
        filas--;
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

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    
    
}
