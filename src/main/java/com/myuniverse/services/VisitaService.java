package com.myuniverse.services;

import com.myuniverse.models.*;
import com.myuniverse.repositories.EspacioRepository;
import com.myuniverse.repositories.RecorridoRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VisitaService {
    private final EspacioRepository espacioRepo;
    private final RecorridoRepository recorridoRepo;
    private Visita visitaActual;

    public VisitaService(EspacioRepository espacioRepo, RecorridoRepository recorridoRepo) {
        this.espacioRepo = espacioRepo;
        this.recorridoRepo = recorridoRepo;
    }

    public Visita iniciarVisita() {
        visitaActual = new Visita();
        return visitaActual;
    }

    public Visita getVisitaActual() {
        return visitaActual;
    }

    public boolean hayVisitaActiva() {
        return visitaActual != null && visitaActual.isActiva();
    }

    public List<Recorrido> obtenerRecorridos() {
        return recorridoRepo.findAll();
    }

    public Recorrido obtenerRecorrido(String id) {
        return recorridoRepo.findById(id);
    }

    public Recorrido iniciarRecorrido(String recorridoId) {
        Recorrido recorrido = recorridoRepo.findById(recorridoId);
        if (recorrido == null || recorrido.getEspacioIds().isEmpty()) return null;

        visitaActual.setRecorridoActualId(recorridoId);
        visitaActual.setEspacioActualId(recorrido.getEspacioIds().get(0));
        return recorrido;
    }

    public Espacio obtenerEspacioActual() {
        if (visitaActual == null || visitaActual.getEspacioActualId() == null) return null;
        return espacioRepo.obtenerPorId(visitaActual.getEspacioActualId());
    }

    public List<Espacio> buscarEspacios(String termino) {
        if (termino == null || termino.isBlank()) return espacioRepo.obtenerTodosEspacios();
        return espacioRepo.filtrarPorCriterio(termino);
    }

    public Espacio obtenerDetallesEspacio(String id) {
        return espacioRepo.obtenerPorId(id);
    }

    public List<Espacio> obtenerEspaciosCercanos() {
        Espacio actual = obtenerEspacioActual();
        if (actual == null) return new ArrayList<>();

        return espacioRepo.obtenerTodosEspacios().stream()
                .filter(e -> !e.getId().equals(actual.getId()))
                .sorted(Comparator.comparingDouble(e ->
                        Math.sqrt(Math.pow(e.getCoordenadaX() - actual.getCoordenadaX(), 2)
                                + Math.pow(e.getCoordenadaY() - actual.getCoordenadaY(), 2))))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Espacio> obtenerEspaciosPorPlanta(String plantaId) {
        return espacioRepo.obtenerPorPlanta(plantaId);
    }

    public Espacio cambiarDeEspacio(String direccion) {
        if (visitaActual == null || visitaActual.getRecorridoActualId() == null) return null;

        Recorrido recorrido = recorridoRepo.findById(visitaActual.getRecorridoActualId());
        if (recorrido == null) return null;

        List<String> espacioIds = recorrido.getEspacioIds();
        int indiceActual = espacioIds.indexOf(visitaActual.getEspacioActualId());
        if (indiceActual < 0) return null;

        int nuevoIndice;
        if ("siguiente".equalsIgnoreCase(direccion)) {
            nuevoIndice = (indiceActual + 1) % espacioIds.size();
        } else if ("anterior".equalsIgnoreCase(direccion)) {
            nuevoIndice = (indiceActual - 1 + espacioIds.size()) % espacioIds.size();
        } else {
            return null;
        }

        visitaActual.setEspacioActualId(espacioIds.get(nuevoIndice));
        return espacioRepo.obtenerPorId(visitaActual.getEspacioActualId());
    }

    public List<Planta> obtenerTodasPlantas() {
        return espacioRepo.obtenerTodasPlantas();
    }
}
