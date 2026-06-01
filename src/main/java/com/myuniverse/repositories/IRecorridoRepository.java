package com.myuniverse.repositories;

import com.myuniverse.models.Recorrido;

import java.util.List;

public interface IRecorridoRepository {
    List<Recorrido> obtenerTodos();
    Recorrido obtenerPorId(String id);
    Recorrido guardar(Recorrido recorrido);
    boolean actualizar(Recorrido recorrido);
    boolean eliminarPorId(String id);
    boolean existePorNombre(String nombre);
    List<Recorrido> filtrarPorCriterio(String criterio);
}