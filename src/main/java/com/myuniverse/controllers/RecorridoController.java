package com.myuniverse.controllers;

import com.myuniverse.models.Espacio;
import com.myuniverse.models.Recorrido;
import com.myuniverse.services.GestionEspacioService;
import com.myuniverse.services.VisitaService;

import java.util.List;

public class RecorridoController {
    private final GestionEspacioService gestionService;
    private final VisitaService visitaService;

    public RecorridoController(GestionEspacioService gestionService, VisitaService visitaService) {
        this.gestionService = gestionService;
        this.visitaService = visitaService;
    }

    public List<Recorrido> obtenerTodos() {
        return gestionService.obtenerTodosRecorridos();
    }

    public List<Recorrido> filtrar(String criterio) {
        return gestionService.filtrarRecorridos(criterio);
    }

    public boolean validarDatosMinimos(String nombre) {
        if (nombre == null || nombre.isBlank()) return false;
        return !gestionService.verificarUnicidadRecorrido(nombre);
    }

    public Recorrido crear(String nombre, String descripcion) {
        return gestionService.crearRecorrido(nombre, descripcion);
    }

    public Recorrido obtenerPorId(String id) {
        return gestionService.obtenerRecorridoPorId(id);
    }

    public boolean actualizar(String id, String nombre, String descripcion, List<String> espacioIds) {
        return gestionService.actualizarRecorrido(id, nombre, descripcion, espacioIds);
    }

    public boolean eliminar(String id) {
        return gestionService.eliminarRecorrido(id);
    }

    public List<Recorrido> obtenerRecorridosVisitante() {
        return visitaService.obtenerRecorridos();
    }

    public Recorrido obtenerDetallesRecorrido(String id) {
        return visitaService.obtenerRecorrido(id);
    }

    public Recorrido iniciarRecorrido(String recorridoId) {
        return visitaService.iniciarRecorrido(recorridoId);
    }

    public Espacio cambiarDeEspacio(String direccion) {
        return visitaService.cambiarDeEspacio(direccion);
    }
}
