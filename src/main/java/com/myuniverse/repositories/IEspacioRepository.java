package com.myuniverse.repositories;

import com.myuniverse.models.Espacio;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Universidad;

import java.util.List;

public interface IEspacioRepository {
    Universidad cargarUniversidad();

    void guardarUniversidad(Universidad universidad);

    List<Espacio> obtenerTodosLosEspacios();

    Espacio buscarEspacioPorId(String id);

    List<Espacio> obtenerEspaciosPorIdPlanta(String idPlanta);

    Planta buscarPlantaPorId(String idPlanta);

    List<Planta> obtenerTodasLasPlantas();

    boolean existeNombreEspacioEnPlanta(String nombre, String idPlanta);

    Espacio guardarEspacio(Espacio espacio, String idPlanta);

    boolean actualizarEspacio(Espacio espacio);

    boolean eliminarEspacioPorId(String id);
}