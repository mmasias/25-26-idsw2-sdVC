package com.myuniverse.services;

import com.myuniverse.exceptions.ExcepcionReglaNegocio;
import com.myuniverse.models.Recorrido;
import com.myuniverse.repositories.IRecorridoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecorridoService implements IRecorridoService {
    private final IRecorridoRepository repositorioRecorrido;

    public RecorridoService(IRecorridoRepository repositorioRecorrido) {
        this.repositorioRecorrido = repositorioRecorrido;
    }

    @Override
    public List<Recorrido> obtenerTodosLosRecorridos() {
        return repositorioRecorrido.obtenerTodos();
    }

    @Override
    public Recorrido obtenerRecorridoPorId(String id) {
        return repositorioRecorrido.obtenerPorId(id);
    }

    @Override
    public List<Recorrido> filtrarRecorridos(String criterio) {
        return repositorioRecorrido.filtrarPorCriterio(criterio);
    }

    @Override
    public boolean esNombreRecorridoUnico(String nombre) {
        return !repositorioRecorrido.existePorNombre(nombre);
    }

    @Override
    public Recorrido crearRecorrido(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new ExcepcionReglaNegocio("BR-03", "Recorrido nombre cannot be empty.");
        }
        String id = "recorrido-" + UUID.randomUUID().toString().substring(0, 8);
        Recorrido recorrido = new Recorrido(id, nombre, descripcion, new ArrayList<>());
        return repositorioRecorrido.guardar(recorrido);
    }

    @Override
    public boolean actualizarRecorrido(String id, String nombre, String descripcion, List<String> idsEspacios) {
        Recorrido recorrido = repositorioRecorrido.obtenerPorId(id);
        if (recorrido == null) {
            return false;
        }
        if (idsEspacios == null || idsEspacios.isEmpty()) {
            throw new ExcepcionReglaNegocio("BR-03",
                    "A recorrido must contain at least one espacio.");
        }
        recorrido.setNombre(nombre);
        recorrido.setDescripcion(descripcion);
        recorrido.setEspacioIds(idsEspacios);
        return repositorioRecorrido.actualizar(recorrido);
    }

    @Override
    public boolean eliminarRecorrido(String id) {
        return repositorioRecorrido.eliminarPorId(id);
    }

    @Override
    public boolean estaEspacioReferenciadoEnAlgunRecorrido(String idEspacio) {
        List<Recorrido> routes = repositorioRecorrido.obtenerTodos();
        for (Recorrido recorrido : routes) {
            if (recorrido.getEspacioIds().contains(idEspacio)) {
                return true;
            }
        }
        return false;
    }
}