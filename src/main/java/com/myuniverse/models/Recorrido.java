package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Recorrido {
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("espacioIds")
    private List<String> idsEspacios = new ArrayList<>();

    public Recorrido() {
    }

    public Recorrido(String id, String nombre, String descripcion, List<String> idsEspacios) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idsEspacios = idsEspacios != null ? idsEspacios : new ArrayList<>();
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<String> getEspacioIds() {
        return Collections.unmodifiableList(idsEspacios);
    }

    public void setEspacioIds(List<String> idsEspacios) {
        this.idsEspacios = idsEspacios != null ? idsEspacios : new ArrayList<>();
    }

    public void agregarEspacio(String idEspacio) {
        if (idEspacio != null && !idsEspacios.contains(idEspacio)) {
            idsEspacios.add(idEspacio);
        }
    }

    public void eliminarEspacio(String idEspacio) {
        idsEspacios.remove(idEspacio);
    }

    public boolean esValido() {
        return idsEspacios != null && !idsEspacios.isEmpty();
    }

    public boolean tieneNombreValido() {
        return nombre != null && !nombre.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recorrido recorrido = (Recorrido) o;
        return Objects.equals(id, recorrido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}