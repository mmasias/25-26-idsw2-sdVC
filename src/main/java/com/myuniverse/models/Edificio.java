package com.myuniverse.models;

import java.util.List;

public class Edificio {
    private String id;
    private String nombre;
    private List<Planta> plantas;

    public Edificio() {}

    public Edificio(String id, String nombre, List<Planta> plantas) {
        this.id = id;
        this.nombre = nombre;
        this.plantas = plantas;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Planta> getPlantas() { return plantas; }
    public void setPlantas(List<Planta> plantas) { this.plantas = plantas; }
}
