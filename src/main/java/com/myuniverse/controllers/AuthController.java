package com.myuniverse.controllers;

import com.myuniverse.models.Sesion;
import com.myuniverse.services.AuthService;

public class AuthController {
    private final AuthService servicioAutenticacion;

    public AuthController(AuthService servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    public Sesion autenticar(String nombreUsuario, String contrasena) {
        return servicioAutenticacion.autenticar(nombreUsuario, contrasena);
    }

    public void cerrarSesion() {
        servicioAutenticacion.cerrarSesion();
    }

    public boolean tieneSesionActiva() {
        return servicioAutenticacion.tieneSesionActiva();
    }
}