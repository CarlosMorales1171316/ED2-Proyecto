package com.example.ivana.proyecto.api;

public class PostMensajes
{
    private String Emisor;
    private String Mensaje;
    private String Receptor;
    private String Fecha;
    private String Asunto;
    private String Tabla;
    private String NombreArchivo;
    private String token;


    public PostMensajes(String emisor, String mensaje, String receptor, String fecha, String asunto, String tabla, String nombreArchivo,String token) {
        Emisor = emisor;
        Mensaje = mensaje;
        Receptor = receptor;
        Fecha = fecha;
        Asunto = asunto;
        Tabla = tabla;
        NombreArchivo = nombreArchivo;
        this.token=token;
    }
}
