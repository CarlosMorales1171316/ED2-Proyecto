package com.example.ivana.proyecto.CompresionArchivos;

public class Arbol extends Nodo
{
    private Nodo ruta;

    public Arbol()
    {
        ruta = null;
    }

    public Arbol(Nodo nodo1, Nodo nodo2)
    {
        ruta = super.AgregarNodo(nodo1, nodo2);
    }

    public void InsertarNodo(int freq, char c)
    {
        ruta.frecuencia =  freq;
        ruta.c = c;
        ruta.izquierda = null;
        ruta.derecha = null;
    }

    public void Insertarnodo(int freq, char c, Nodo izq, Nodo der)
    {
        ruta.frecuencia =  freq;
        ruta.c = c;
        this.ruta.izquierda = izq;
        this.ruta.derecha = der;
    }

    public void insertarNodo(Nodo nodo)
    {
        this.ruta.frecuencia = nodo.frecuencia;
        this.ruta.c = nodo.c;
        this.ruta.izquierda = nodo.izquierda;
        this.ruta.derecha = nodo.derecha;
    }

    public void insertar(Nodo nodo1, Nodo nodo2)
    {
        ruta = super.AgregarNodo(nodo1, nodo2);
    }
}
