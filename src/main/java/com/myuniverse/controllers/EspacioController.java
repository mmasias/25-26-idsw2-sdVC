package com.myuniverse.controllers;

import com.myuniverse.models.Espacio;
import com.myuniverse.models.Planta;
import com.myuniverse.services.GestionEspacioService;
import com.myuniverse.services.VisitaService;

import java.util.List;

public class EspacioController {
    private final GestionEspacioService gestionService;
    private final VisitaService visitaService;

    public EspacioController(GestionEspacioService gestionService, VisitaService visitaService) {
        this.gestionService = gestionService;
        this.visitaService = visitaService;
    }

    public List<Espacio> obtenerTodos() {
        return gestionService.obtenerTodosEspacios();
    }

    public List<Espacio> filtrar(String criterio) {
        return gestionService.filtrarEspacios(criterio);
    }

    public List<Planta> obtenerPlantas() {
        return gestionService.obtenerTodasPlantas();
    }

    public boolean validarDatosMinimos(String nombre, String plantaId) {
        if (nombre == null || nombre.isBlank()) return false;
        return !gestionService.verificarUnicidadEspacio(nombre, plantaId);
    }

    public Espacio crear(String nombre, String tipo, String descripcion,
                         int coordenadaX, int coordenadaY, String plantaId) {
        return gestionService.crearEspacio(nombre, tipo, descripcion, coordenadaX, coordenadaY, plantaId);
    }

    public Espacio obtenerPorId(String id) {
        return gestionService.obtenerEspacioPorId(id);
    }

    public boolean actualizar(String id, String nombre, String tipo, String descripcion,
                               int coordenadaX, int coordenadaY) {
        return gestionService.actualizarEspacio(id, nombre, tipo, descripcion, coordenadaX, coordenadaY);
    }

    public boolean eliminar(String id) {
        return gestionService.eliminarEspacio(id);
    }

    public List<Espacio> buscar(String criterio) {
        return visitaService.buscarEspacios(criterio);
    }

    public Espacio obtenerUbicacionActual() {
        return visitaService.obtenerEspacioActual();
    }

    public Espacio obtenerDetalles(String id) {
        return visitaService.obtenerDetallesEspacio(id);
    }

    public List<Espacio> obtenerCercanos() {
        return visitaService.obtenerEspaciosCercanos();
    }

    public List<Espacio> obtenerPorPlanta(String plantaId) {
        return visitaService.obtenerEspaciosPorPlanta(plantaId);
    }
}
