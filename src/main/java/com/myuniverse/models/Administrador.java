package com.myuniverse.models;

public class Administrador {
    private String usuario;
    private String contraseña;

    public Administrador() {}

    public Administrador(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public boolean validarContraseña(String contraseña) {
        return this.contraseña != null && this.contraseña.equals(contraseña);
    }
}
