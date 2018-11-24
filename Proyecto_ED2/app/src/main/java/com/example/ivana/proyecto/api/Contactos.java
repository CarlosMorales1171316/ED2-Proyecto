package com.example.ivana.proyecto.api;

import com.google.gson.annotations.SerializedName;

public class Contactos
{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int  id;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String Username;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String nombre;

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String Correo;

}
