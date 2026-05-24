package com.myuniverse.models;

import java.time.LocalDateTime;

public class Visita {
    private boolean activa;
    private LocalDateTime fechaInicio;
    private String recorridoActualId;
    private String espacioActualId;

    public Visita() {
        this.activa = true;
        this.fechaInicio = LocalDateTime.now();
    }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getRecorridoActualId() { return recorridoActualId; }
    public void setRecorridoActualId(String recorridoActualId) { this.recorridoActualId = recorridoActualId; }

    public String getEspacioActualId() { return espacioActualId; }
    public void setEspacioActualId(String espacioActualId) { this.espacioActualId = espacioActualId; }
}
