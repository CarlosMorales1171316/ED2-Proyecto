package com.example.ivana.proyecto.AlgoritmoCompresion;

public class Mensajes
{
    private String Emisor,Mensajes,Recepetor;
    public Mensajes(String emisor,String mensaje,String recepetor)
    {
        this.Emisor = emisor;
        this.Mensajes = mensaje;
        this.Recepetor = recepetor;
    }

    public String getEmisor() {
        return Emisor;
    }

    public String getRecepetor() {
        return Recepetor;
    }

    public String getMensajes() {
        return Mensajes;
    }
}
