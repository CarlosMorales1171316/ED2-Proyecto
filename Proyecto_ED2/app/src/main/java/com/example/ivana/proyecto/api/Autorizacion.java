package com.example.ivana.proyecto.api;

public class Autorizacion
{
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}
