package com.myuniverse.services;

import com.myuniverse.models.Administrador;
import com.myuniverse.models.Sesion;
import com.myuniverse.repositories.AdministradorRepository;

public class AuthService {
    private final AdministradorRepository adminRepo;
    private Sesion sesionActual;

    public AuthService(AdministradorRepository adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Sesion autenticar(String usuario, String contraseña) {
        Administrador admin = adminRepo.findById(usuario);
        if (admin == null || !admin.validarContraseña(contraseña)) {
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

    public Sesion getSesionActual() {
        return sesionActual;
    }

    public boolean haySesionActiva() {
        return sesionActual != null && sesionActual.isActiva();
    }
}
