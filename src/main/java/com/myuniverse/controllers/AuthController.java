package com.myuniverse.controllers;

import com.myuniverse.models.Sesion;
import com.myuniverse.services.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public Sesion autenticar(String usuario, String contraseña) {
        return authService.autenticar(usuario, contraseña);
    }

    public void cerrarSesion() {
        authService.cerrarSesion();
    }

    public boolean haySesionActiva() {
        return authService.haySesionActiva();
    }
}
