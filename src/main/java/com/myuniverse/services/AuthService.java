package com.myuniverse.services;

import com.myuniverse.models.Administrador;
import com.myuniverse.models.Sesion;
import com.myuniverse.repositories.AdministradorRepository;

public class AuthService {
    private final AdministradorRepository repositorioAdministrador;
    private Sesion sesionActual;

    public AuthService(AdministradorRepository repositorioAdministrador) {
        this.repositorioAdministrador = repositorioAdministrador;
    }

    public Sesion autenticar(String nombreUsuario, String contrasena) {
        Administrador admin = repositorioAdministrador.obtenerPorId(nombreUsuario);
        if (admin == null || !admin.validarContrasena(contrasena)) {
            return null;
        }
        sesionActual = new Sesion(admin);
        return sesionActual;
    }

    public void cerrarSesion() {
        if (sesionActual != null) {
            sesionActual.setActiva(false);
            sesionActual = null;
        }
    }

    public Sesion obtenerSesionActual() {
        return sesionActual;
    }

    public boolean tieneSesionActiva() {
        return sesionActual != null && sesionActual.estaActiva();
    }
}