package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.myuniverse.exceptions.ExcepcionReglaNegocio;

public class Planta {
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("numero")
    private int numero;

    @SerializedName("espacios")
    private List<Espacio> espacios = new ArrayList<>();

    public Planta() {
    }

    public Planta(String id, String nombre, int numero, List<Espacio> espacios) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.espacios = espacios != null ? espacios : new ArrayList<>();
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public List<Espacio> getEspacios() {
        return Collections.unmodifiableList(espacios);
    }

    public void agregarEspacio(Espacio espacio) {
        if (espacio == null) {
            throw new ExcepcionReglaNegocio("BR-02", "Espacio cannot be null.");
        }
        if (!esNombreEspacioUnico(espacio.getNombre())) {
            throw new ExcepcionReglaNegocio("BR-02",
                    "Duplicate espacio nombre '" + espacio.getNombre() + "' on planta '" + nombre + "'.");
        }
        espacios.add(espacio);
    }

    public boolean eliminarEspacio(String idEspacio) {
        return espacios.removeIf(s -> s.getId().equals(idEspacio));
    }

    public boolean esNombreEspacioUnico(String spaceName) {
        if (spaceName == null || spaceName.isBlank()) {
            return false;
        }
        return espacios.stream()
                .noneMatch(s -> s.getNombre().equalsIgnoreCase(spaceName));
    }

    public Espacio buscarEspacioPorId(String idEspacio) {
        return espacios.stream()
                .filter(s -> s.getId().equals(idEspacio))
                .findFirst()
                .orElse(null);
    }

    public boolean contieneEspacio(String idEspacio) {
        return espacios.stream().anyMatch(s -> s.getId().equals(idEspacio));
    }

    void setEspaciosInternal(List<Espacio> espacios) {
        this.espacios = espacios != null ? espacios : new ArrayList<>();
    }

    public void setEspacios(List<Espacio> espacios) {
        this.espacios = espacios != null ? espacios : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Planta planta = (Planta) o;
        return Objects.equals(id, planta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}