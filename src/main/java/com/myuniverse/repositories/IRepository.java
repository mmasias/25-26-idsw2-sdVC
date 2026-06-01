package com.myuniverse.repositories;

import java.util.List;

public interface IRepository<T> {
    List<T> obtenerTodos();
    T obtenerPorId(String id);
    T guardar(T entity);
    boolean actualizar(T entity);
    boolean eliminarPorId(String id);
}