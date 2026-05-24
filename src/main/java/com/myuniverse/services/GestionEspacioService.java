package com.myuniverse.services;

import com.myuniverse.models.*;
import com.myuniverse.repositories.EspacioRepository;
import com.myuniverse.repositories.RecorridoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestionEspacioService {
    private final EspacioRepository espacioRepo;
    private final RecorridoRepository recorridoRepo;

    public GestionEspacioService(EspacioRepository espacioRepo, RecorridoRepository recorridoRepo) {
        this.espacioRepo = espacioRepo;
        this.recorridoRepo = recorridoRepo;
    }

    public Universidad obtenerUniversidad() {
        return espacioRepo.obtenerUniversidad();
    }

    public void guardarUniversidad(Universidad universidad) {
        espacioRepo.guardarUniversidad(universidad);
    }

    public List<Espacio> obtenerTodosEspacios() {
        return espacioRepo.obtenerTodosEspacios();
    }

    public List<Espacio> filtrarEspacios(String criterio) {
        return espacioRepo.filtrarPorCriterio(criterio);
    }

    public Espacio obtenerEspacioPorId(String id) {
        return espacioRepo.obtenerPorId(id);
    }

    public List<Planta> obtenerTodasPlantas() {
        return espacioRepo.obtenerTodasPlantas();
    }

    public boolean verificarUnicidadEspacio(String nombre, String plantaId) {
        return espacioRepo.existeNombreEnPlanta(nombre, plantaId);
    }

public Espacio crearEspacio(String nombre, String tipo, String descripcion,
                                  int coordenadaX, int coordenadaY, String plantaId) {
        return crearEspacio(nombre, tipo, descripcion, coordenadaX, coordenadaY, 1, 1, plantaId);
    }

    public Espacio crearEspacio(String nombre, String tipo, String descripcion,
                                  int coordenadaX, int coordenadaY, int ancho, int alto, String plantaId) {
        String id = "esp-" + UUID.randomUUID().toString().substring(0, 8);
        Espacio espacio = new Espacio(id, nombre, tipo, descripcion, coordenadaX, coordenadaY, ancho, alto);
        espacioRepo.crearEspacio(espacio, plantaId);
        return espacio;
    }

    public boolean actualizarEspacio(String id, String nombre, String tipo, String descripcion,
                                       int coordenadaX, int coordenadaY) {
        Espacio espacio = espacioRepo.obtenerPorId(id);
        if (espacio == null) return false;
        espacio.setNombre(nombre);
        espacio.setTipo(tipo);
        espacio.setDescripcion(descripcion);
        espacio.setCoordenadaX(coordenadaX);
        espacio.setCoordenadaY(coordenadaY);
        return espacioRepo.actualizarEspacio(espacio);
    }

    public boolean actualizarEspacioCompleto(String id, String nombre, String tipo, String descripcion,
                                               int coordenadaX, int coordenadaY, int ancho, int alto) {
        Espacio espacio = espacioRepo.obtenerPorId(id);
        if (espacio == null) return false;
        espacio.setNombre(nombre);
        espacio.setTipo(tipo);
        espacio.setDescripcion(descripcion);
        espacio.setCoordenadaX(coordenadaX);
        espacio.setCoordenadaY(coordenadaY);
        espacio.setAncho(ancho);
        espacio.setAlto(alto);
        return espacioRepo.actualizarEspacio(espacio);
    }

    public boolean eliminarEspacio(String id) {
        return espacioRepo.eliminarEspacio(id);
    }

    public List<Recorrido> obtenerTodosRecorridos() {
        return recorridoRepo.findAll();
    }

    public List<Recorrido> filtrarRecorridos(String criterio) {
        return recorridoRepo.filtrarPorCriterio(criterio);
    }

    public boolean verificarUnicidadRecorrido(String nombre) {
        return recorridoRepo.existeNombre(nombre);
    }

    public Recorrido crearRecorrido(String nombre, String descripcion) {
        String id = "rec-" + UUID.randomUUID().toString().substring(0, 8);
        Recorrido recorrido = new Recorrido(id, nombre, descripcion, new ArrayList<>());
        recorridoRepo.save(recorrido);
        return recorrido;
    }

    public Recorrido obtenerRecorridoPorId(String id) {
        return recorridoRepo.findById(id);
    }

    public boolean actualizarRecorrido(String id, String nombre, String descripcion, List<String> espacioIds) {
        Recorrido recorrido = recorridoRepo.findById(id);
        if (recorrido == null) return false;

        recorrido.setNombre(nombre);
        recorrido.setDescripcion(descripcion);
        recorrido.setEspacioIds(espacioIds);

        return recorridoRepo.update(recorrido);
    }

    public boolean eliminarRecorrido(String id) {
        return recorridoRepo.deleteById(id);
    }
}
