package com.myuniverse.repositories;

import com.myuniverse.models.Region;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.Universidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EspacioRepository implements IEspacioRepository {
    private static final String FILE_NAME = "universidad.json";

    private Universidad cargarDatosUniversidad() {
        Universidad universidad = JsonUtil.read(FILE_NAME, Universidad.class);
        if (universidad == null) {
            universidad = crearUniversidadPorDefecto();
            guardarUniversidad(universidad);
        }
        return universidad;
    }

    private Universidad crearUniversidadPorDefecto() {
        Universidad universidad = new Universidad();
        universidad.setId("uni-1");
        universidad.setNombre("My University");
        universidad.setRegiones(new ArrayList<>());

        Region region = new Region();
        region.setId("region-1");
        region.setNombre("Main Campus");
        region.setPlantas(new ArrayList<>());

        Planta groundPlanta = new Planta();
        groundPlanta.setId("planta-0");
        groundPlanta.setNombre("Ground Floor");
        groundPlanta.setNumero(0);
        groundPlanta.setEspacios(new ArrayList<>());

        Planta firstPlanta = new Planta();
        firstPlanta.setId("planta-1");
        firstPlanta.setNombre("First Floor");
        firstPlanta.setNumero(1);
        firstPlanta.setEspacios(new ArrayList<>());

        region.agregarPlanta(groundPlanta);
        region.agregarPlanta(firstPlanta);
        universidad.addRegion(region);
        return universidad;
    }

    @Override
    public Universidad cargarUniversidad() {
        return cargarDatosUniversidad();
    }

    @Override
    public void guardarUniversidad(Universidad universidad) {
        JsonUtil.write(FILE_NAME, universidad);
    }

    @Override
    public List<Espacio> obtenerTodosLosEspacios() {
        Universidad universidad = cargarDatosUniversidad();
        return universidad.obtenerTodosLosEspacios();
    }

    @Override
    public Espacio buscarEspacioPorId(String id) {
        return obtenerTodosLosEspacios().stream()
                .filter(espacio -> espacio.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Espacio> obtenerEspaciosPorIdPlanta(String idPlanta) {
        Universidad universidad = cargarDatosUniversidad();
        Planta planta = universidad.buscarPlantaPorId(idPlanta);
        return planta != null ? planta.getEspacios() : Collections.emptyList();
    }

    @Override
    public Planta buscarPlantaPorId(String idPlanta) {
        return cargarDatosUniversidad().buscarPlantaPorId(idPlanta);
    }

    @Override
    public List<Planta> obtenerTodasLasPlantas() {
        return cargarDatosUniversidad().obtenerTodasLasPlantas();
    }

    @Override
    public boolean existeNombreEspacioEnPlanta(String nombre, String idPlanta) {
        return obtenerEspaciosPorIdPlanta(idPlanta).stream()
                .anyMatch(espacio -> espacio.getNombre().equalsIgnoreCase(nombre));
    }

    @Override
    public Espacio guardarEspacio(Espacio espacio, String idPlanta) {
        Universidad universidad = cargarDatosUniversidad();
        for (Region region : universidad.getRegiones()) {
            for (Planta planta : region.obtenerPlantas()) {
                if (planta.getId().equals(idPlanta)) {
                    List<Espacio> mutableEspacios = new ArrayList<>(planta.getEspacios());
                    mutableEspacios.add(espacio);
                    planta.setEspacios(mutableEspacios);
                    guardarUniversidad(universidad);
                    return espacio;
                }
            }
        }
        return null;
    }

    @Override
    public boolean actualizarEspacio(Espacio espacio) {
        Universidad universidad = cargarDatosUniversidad();
        for (Region region : universidad.getRegiones()) {
            for (Planta planta : region.obtenerPlantas()) {
                List<Espacio> espacios = planta.getEspacios();
                for (int i = 0; i < espacios.size(); i++) {
                    if (espacios.get(i).getId().equals(espacio.getId())) {
                        List<Espacio> mutableEspacios = new ArrayList<>(espacios);
                        mutableEspacios.set(i, espacio);
                        planta.setEspacios(mutableEspacios);
                        guardarUniversidad(universidad);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean eliminarEspacioPorId(String id) {
        Universidad universidad = cargarDatosUniversidad();
        for (Region region : universidad.getRegiones()) {
            for (Planta planta : region.obtenerPlantas()) {
                boolean removed = planta.eliminarEspacio(id);
                if (removed) {
                    guardarUniversidad(universidad);
                    return true;
                }
            }
        }
        return false;
    }
}
