package com.example.ivana.proyecto.api;

import com.google.gson.annotations.SerializedName;

public class logIn
{
    private String Username;
    private String Contraseña;

    public logIn(String Username,String Contraseña)
    {
        this.Username = Username;
        this.Contraseña = Contraseña;
    }
}
