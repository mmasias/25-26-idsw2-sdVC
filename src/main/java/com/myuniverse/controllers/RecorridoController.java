package com.myuniverse.controllers;

import com.myuniverse.models.Recorrido;
import com.myuniverse.services.IRecorridoService;

import java.util.List;

public class RecorridoController {
    private final IRecorridoService servicioRecorrido;

    public RecorridoController(IRecorridoService servicioRecorrido) {
        this.servicioRecorrido = servicioRecorrido;
    }

    public List<Recorrido> obtenerTodos() {
        return servicioRecorrido.obtenerTodosLosRecorridos();
    }

    public List<Recorrido> filtrar(String criterio) {
        return servicioRecorrido.filtrarRecorridos(criterio);
    }

    public boolean validarDatosMinimos(String nombre) {
        if (nombre == null || nombre.isBlank())
            return false;
        return servicioRecorrido.esNombreRecorridoUnico(nombre);
    }

    public Recorrido crear(String nombre, String descripcion) {
        return servicioRecorrido.crearRecorrido(nombre, descripcion);
    }

    public Recorrido obtenerPorId(String id) {
        return servicioRecorrido.obtenerRecorridoPorId(id);
    }

    public boolean actualizar(String id, String nombre, String descripcion, List<String> idsEspacios) {
        return servicioRecorrido.actualizarRecorrido(id, nombre, descripcion, idsEspacios);
    }

    public boolean eliminar(String id) {
        return servicioRecorrido.eliminarRecorrido(id);
    }

    public boolean estaEspacioReferenciadoEnAlgunRecorrido(String idEspacio) {
        return servicioRecorrido.estaEspacioReferenciadoEnAlgunRecorrido(idEspacio);
    }
}