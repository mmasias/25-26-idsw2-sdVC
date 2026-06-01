package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Universidad {
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName(value = "regiones", alternate = {"edificios"})
    private List<Region> regiones = new ArrayList<>();

    public Universidad() {
    }

    public Universidad(String id, String nombre, List<Region> regiones) {
        this.id = id;
        this.nombre = nombre;
        this.regiones = regiones != null ? regiones : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Region> getRegiones() {
        return Collections.unmodifiableList(regiones);
    }

    public void addRegion(Region region) {
        regiones.add(region);
    }

    public boolean removeRegion(String idRegion) {
        return regiones.removeIf(r -> r.getId().equals(idRegion));
    }

    public void setRegiones(List<Region> regiones) {
        this.regiones = regiones != null ? regiones : new ArrayList<>();
    }

    public Region findRegionById(String idRegion) {
        return regiones.stream()
                .filter(r -> r.getId().equals(idRegion))
                .findFirst()
                .orElse(null);
    }

    public Planta buscarPlantaPorId(String idPlanta) {
        for (Region region : regiones) {
            Planta planta = region.buscarPlantaPorId(idPlanta);
            if (planta != null)
                return planta;
        }
        return null;
    }

    public Espacio buscarEspacioPorId(String idEspacio) {
        for (Region region : regiones) {
            for (Planta planta : region.obtenerPlantas()) {
                Espacio espacio = planta.buscarEspacioPorId(idEspacio);
                if (espacio != null)
                    return espacio;
            }
        }
        return null;
    }

    public Planta findPlantaContainingEspacio(String idEspacio) {
        for (Region region : regiones) {
            for (Planta planta : region.obtenerPlantas()) {
                if (planta.contieneEspacio(idEspacio)) {
                    return planta;
                }
            }
        }
        return null;
    }

    public List<Espacio> obtenerTodosLosEspacios() {
        List<Espacio> espacios = new ArrayList<>();
        for (Region region : regiones) {
            for (Planta planta : region.obtenerPlantas()) {
                espacios.addAll(planta.getEspacios());
            }
        }
        return espacios;
    }

    public List<Planta> obtenerTodasLasPlantas() {
        List<Planta> plantas = new ArrayList<>();
        for (Region region : regiones) {
            plantas.addAll(region.obtenerPlantas());
        }
        return plantas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Universidad that = (Universidad) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}