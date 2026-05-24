package com.myuniverse.repositories;

import com.google.gson.reflect.TypeToken;
import com.myuniverse.models.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EspacioRepository {
    private static final String ARCHIVO = "universidad.json";
    private static final Type TIPO_UNIVERSIDAD = new TypeToken<Universidad>() {}.getType();

    private Universidad cargarUniversidad() {
        Universidad uni = JsonUtil.leer(ARCHIVO, TIPO_UNIVERSIDAD);
        if (uni == null) {
            uni = crearUniversidadPorDefecto();
            guardarUniversidad(uni);
        }
        return uni;
    }

    public void guardarUniversidad(Universidad uni) {
        JsonUtil.escribir(ARCHIVO, uni, TIPO_UNIVERSIDAD);
    }

    private Universidad crearUniversidadPorDefecto() {
        Universidad uni = new Universidad();
        uni.setId("uni-1");
        uni.setNombre("Universidad Europea del Atlántico");
        uni.setEdificios(new ArrayList<>());

        Edificio edif = new Edificio();
        edif.setId("edif-1");
        edif.setNombre("Edificio Principal");
        edif.setPlantas(new ArrayList<>());

        Planta pb = new Planta();
        pb.setId("planta-0");
        pb.setNombre("Planta Baja");
        pb.setNumero(0);
        pb.setEspacios(new ArrayList<>());

        Planta p1 = new Planta();
        p1.setId("planta-1");
        p1.setNombre("Primera Planta");
        p1.setNumero(1);
        p1.setEspacios(new ArrayList<>());

        edif.getPlantas().add(pb);
        edif.getPlantas().add(p1);
        uni.getEdificios().add(edif);
        return uni;
    }

    public Universidad obtenerUniversidad() {
        return cargarUniversidad();
    }

    public List<Espacio> obtenerTodosEspacios() {
        Universidad uni = cargarUniversidad();
        List<Espacio> todos = new ArrayList<>();
        for (Edificio e : uni.getEdificios()) {
            for (Planta p : e.getPlantas()) {
                todos.addAll(p.getEspacios());
            }
        }
        return todos;
    }

    public Espacio obtenerPorId(String id) {
        return obtenerTodosEspacios().stream()
                .filter(esp -> esp.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Espacio> filtrarPorCriterio(String criterio) {
        String lower = criterio.toLowerCase();
        return obtenerTodosEspacios().stream()
                .filter(esp -> esp.getNombre().toLowerCase().contains(lower)
                        || esp.getTipo().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public List<Espacio> obtenerPorPlanta(String plantaId) {
        Universidad uni = cargarUniversidad();
        for (Edificio e : uni.getEdificios()) {
            for (Planta p : e.getPlantas()) {
                if (p.getId().equals(plantaId)) {
                    return p.getEspacios();
                }
            }
        }
        return Collections.emptyList();
    }

    public List<Planta> obtenerTodasPlantas() {
        Universidad uni = cargarUniversidad();
        List<Planta> todas = new ArrayList<>();
        for (Edificio e : uni.getEdificios()) {
            todas.addAll(e.getPlantas());
        }
        return todas;
    }

    public Planta obtenerPlantaPorId(String plantaId) {
        return obtenerTodasPlantas().stream()
                .filter(p -> p.getId().equals(plantaId))
                .findFirst()
                .orElse(null);
    }

    public boolean existeNombreEnPlanta(String nombre, String plantaId) {
        return obtenerPorPlanta(plantaId).stream()
                .anyMatch(esp -> esp.getNombre().equalsIgnoreCase(nombre));
    }

    public Espacio crearEspacio(Espacio espacio, String plantaId) {
        Universidad uni = cargarUniversidad();
        for (Edificio e : uni.getEdificios()) {
            for (Planta p : e.getPlantas()) {
                if (p.getId().equals(plantaId)) {
                    p.getEspacios().add(espacio);
                    guardarUniversidad(uni);
                    return espacio;
                }
            }
        }
        return null;
    }

    public boolean actualizarEspacio(Espacio espacio) {
        Universidad uni = cargarUniversidad();
        for (Edificio e : uni.getEdificios()) {
            for (Planta p : e.getPlantas()) {
                for (int i = 0; i < p.getEspacios().size(); i++) {
                    if (p.getEspacios().get(i).getId().equals(espacio.getId())) {
                        p.getEspacios().set(i, espacio);
                        guardarUniversidad(uni);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean eliminarEspacio(String id) {
        Universidad uni = cargarUniversidad();
        for (Edificio e : uni.getEdificios()) {
            for (Planta p : e.getPlantas()) {
                boolean eliminado = p.getEspacios().removeIf(esp -> esp.getId().equals(id));
                if (eliminado) {
                    guardarUniversidad(uni);
                    return true;
                }
            }
        }
        return false;
    }
}
