package com.myuniverse.models;

import java.time.LocalDateTime;

public class Sesion {
    private Administrador administrador;
    private boolean activa;
    private LocalDateTime fechaInicio;

    public Sesion() {}

    public Sesion(Administrador administrador) {
        this.administrador = administrador;
        this.activa = true;
        this.fechaInicio = LocalDateTime.now();
    }

    public Administrador getAdministrador() { return administrador; }
    public void setAdministrador(Administrador administrador) { this.administrador = administrador; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
}
