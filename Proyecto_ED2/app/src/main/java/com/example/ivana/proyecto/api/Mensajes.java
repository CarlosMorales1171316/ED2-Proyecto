package com.example.ivana.proyecto.api;

import com.google.gson.annotations.SerializedName;

public class Mensajes
{
    private String Emisor;

    private String Mensaje;

    private String Receptor;

    private String Fecha;

    private String Asunto;

    private String Tabla;

    private String NombreArchivo;

    public Mensajes(String emisor,String receptor,String mensaje,String tabla,String asunto,String nombreArchivo,String fecha)
    {
        Emisor = emisor;
        Receptor = receptor;
        Mensaje = mensaje;
        Tabla = tabla;
        Asunto = asunto;
        NombreArchivo =nombreArchivo;
        Fecha = fecha;
    }

}
