package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

public class Administrador {
    @SerializedName("usuario")
    private String nombreUsuario;

    @SerializedName("contraseña")
    private String contrasena;

    public Administrador() {
    }

    public Administrador(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean validarContrasena(String contrasenaEntrada) {
        return this.contrasena != null && this.contrasena.equals(contrasenaEntrada);
    }
}