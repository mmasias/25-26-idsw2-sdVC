package com.myuniverse.services;

import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.Region;
import com.myuniverse.models.TipoEspacio;
import com.myuniverse.models.Universidad;

import java.util.List;

public interface IGestionEspacioService {
    Universidad obtenerUniversidad();
    void guardarUniversidad(Universidad universidad);
    List<Espacio> obtenerTodosLosEspacios();
    Espacio obtenerEspacioPorId(String id);
    List<Espacio> filtrarEspacios(String criterio);
    List<Planta> obtenerTodasLasPlantas();
    boolean esNombreEspacioUnicoEnPlanta(String nombre, String idPlanta);
    Espacio crearEspacio(String nombre, TipoEspacio tipo, String descripcion,
                      int coordenadaX, int coordenadaY, int ancho, int alto, String idPlanta);
    boolean actualizarEspacioCompleto(String id, String nombre, TipoEspacio tipo, String descripcion,
                           int coordenadaX, int coordenadaY, int ancho, int alto);
    boolean moverEspacio(String id, int newX, int newY);
    boolean eliminarEspacio(String id);
    boolean tieneSolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excludeId);
    boolean estaEspacioReferenciadoEnAlgunRecorrido(String idEspacio);
    void actualizarNombreUniversidad(String nombre);
    void actualizarNombreRegion(String idRegion, String nombre);
    Region agregarRegion(String nombre);
}