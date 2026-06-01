package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class Sesion {
    @SerializedName("administrador")
    private Administrador administrator;

    @SerializedName("activa")
    private boolean activa;

    @SerializedName("fechaInicio")
    private LocalDateTime startDate;

    public Sesion() {
    }

    public Sesion(Administrador administrator) {
        this.administrator = administrator;
        this.activa = true;
        this.startDate = LocalDateTime.now();
    }

    public Administrador getAdministrador() {
        return administrator;
    }

    public void setAdministrador(Administrador administrator) {
        this.administrator = administrator;
    }

    public boolean estaActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}