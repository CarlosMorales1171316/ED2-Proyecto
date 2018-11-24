package com.example.ivana.proyecto.api;

public class Registro
{
    private String Username;
    private String Contraseña;
    private String Nombre;
    private String Correo;

    public Registro(String username, String contraseña, String nombre, String correo) {
        Username = username;
        Contraseña = contraseña;
        Nombre = nombre;
        Correo = correo;
    }
}
