package com.myuniverse.services;

import com.myuniverse.models.Recorrido;

import java.util.List;

public interface IRecorridoService {
    List<Recorrido> obtenerTodosLosRecorridos();
    Recorrido obtenerRecorridoPorId(String id);
    List<Recorrido> filtrarRecorridos(String criterio);
    boolean esNombreRecorridoUnico(String nombre);
    Recorrido crearRecorrido(String nombre, String descripcion);
    boolean actualizarRecorrido(String id, String nombre, String descripcion, List<String> idsEspacios);
    boolean eliminarRecorrido(String id);
    boolean estaEspacioReferenciadoEnAlgunRecorrido(String idEspacio);
}