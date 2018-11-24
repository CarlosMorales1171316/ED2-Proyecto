package com.example.ivana.proyecto.CompresionArchivos;

public class Nodo
{
    public int frecuencia;
    public char c;
    public Nodo izquierda;
    public Nodo derecha;

    public Nodo(int frecuencia, char c, Nodo izq, Nodo der)
    {
        this.frecuencia = frecuencia;
        this.c = c;
        this.izquierda = izq;
        this.derecha = der;
    }

    public Nodo()
    {

    }

    public Nodo AgregarNodo(Nodo nodo1, Nodo nodo2)
    {
        if(nodo1.frecuencia < nodo2.frecuencia)
        {
            izquierda = nodo1;
            derecha = nodo2;
        }
        else
        {
            derecha = nodo1;
            izquierda = nodo2;
        }
        frecuencia = nodo1.frecuencia + nodo2.frecuencia;

        return this;
    }

}