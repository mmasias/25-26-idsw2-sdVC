package com.myuniverse.controllers;

import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.Region;
import com.myuniverse.models.TipoEspacio;
import com.myuniverse.models.Universidad;
import com.myuniverse.services.IGestionEspacioService;

import java.util.List;

public class EspacioController {
    private final IGestionEspacioService servicioEspacio;

    public EspacioController(IGestionEspacioService servicioEspacio) {
        this.servicioEspacio = servicioEspacio;
    }

    public Universidad obtenerUniversidad() {
        return servicioEspacio.obtenerUniversidad();
    }

    public void guardarUniversidad(Universidad universidad) {
        servicioEspacio.guardarUniversidad(universidad);
    }

    public List<Espacio> obtenerTodos() {
        return servicioEspacio.obtenerTodosLosEspacios();
    }

    public List<Espacio> filtrar(String criterio) {
        return servicioEspacio.filtrarEspacios(criterio);
    }

    public List<Planta> obtenerPlantas() {
        return servicioEspacio.obtenerTodasLasPlantas();
    }

    public boolean validarDatosMinimos(String nombre, String idPlanta) {
        if (nombre == null || nombre.isBlank()) return false;
        return servicioEspacio.esNombreEspacioUnicoEnPlanta(nombre, idPlanta);
    }

    public Espacio crear(String nombre, TipoEspacio tipo, String descripcion,
                        int x, int y, int ancho, int alto, String idPlanta) {
        return servicioEspacio.crearEspacio(nombre, tipo, descripcion, x, y, ancho, alto, idPlanta);
    }

    public Espacio obtenerPorId(String id) {
        return servicioEspacio.obtenerEspacioPorId(id);
    }

    public boolean actualizarCompleto(String id, String nombre, TipoEspacio tipo, String descripcion,
                              int x, int y, int ancho, int alto) {
        return servicioEspacio.actualizarEspacioCompleto(id, nombre, tipo, descripcion, x, y, ancho, alto);
    }

    public boolean mover(String id, int newX, int newY) {
        return servicioEspacio.moverEspacio(id, newX, newY);
    }

    public boolean eliminar(String id) {
        return servicioEspacio.eliminarEspacio(id);
    }

    public boolean comprobarSolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excludeId) {
        return servicioEspacio.tieneSolapamiento(espacios, x, y, ancho, alto, excludeId);
    }

    public void actualizarNombreUniversidad(String nombre) {
        servicioEspacio.actualizarNombreUniversidad(nombre);
    }

    public void actualizarNombreRegion(String idRegion, String nombre) {
        servicioEspacio.actualizarNombreRegion(idRegion, nombre);
    }

    public Region agregarRegion(String nombre) {
        return servicioEspacio.agregarRegion(nombre);
    }
}