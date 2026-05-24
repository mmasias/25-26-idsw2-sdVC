package com.myuniverse.models;

import java.util.List;

public class Universidad {
    private String id;
    private String nombre;
    private List<Edificio> edificios;

    public Universidad() {}

    public Universidad(String id, String nombre, List<Edificio> edificios) {
        this.id = id;
        this.nombre = nombre;
        this.edificios = edificios;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Edificio> getEdificios() { return edificios; }
    public void setEdificios(List<Edificio> edificios) { this.edificios = edificios; }
}
