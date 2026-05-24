package com.myuniverse.controllers;

import com.myuniverse.models.Visita;
import com.myuniverse.services.VisitaService;

public class VisitaController {
    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    public Visita iniciar() {
        return visitaService.iniciarVisita();
    }

    public boolean hayVisitaActiva() {
        return visitaService.hayVisitaActiva();
    }
}
