package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Region {
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("plantas")
    private List<Planta> plantas = new ArrayList<>();

    public Region() {
    }

    public Region(String id, String nombre, List<Planta> plantas) {
        this.id = id;
        this.nombre = nombre;
        this.plantas = plantas != null ? plantas : new ArrayList<>();
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

    public List<Planta> obtenerPlantas() {
        return Collections.unmodifiableList(plantas);
    }

    public void agregarPlanta(Planta planta) {
        plantas.add(planta);
    }

    public boolean eliminarPlanta(String idPlanta) {
        return plantas.removeIf(f -> f.getId().equals(idPlanta));
    }

    public void setPlantas(List<Planta> plantas) {
        this.plantas = plantas != null ? plantas : new ArrayList<>();
    }

    public Planta buscarPlantaPorId(String idPlanta) {
        return plantas.stream()
                .filter(f -> f.getId().equals(idPlanta))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Region region = (Region) o;
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}