package com.myuniverse.models;

import java.util.List;

public class Recorrido {
    private String id;
    private String nombre;
    private String descripcion;
    private List<String> espacioIds;

    public Recorrido() {}

    public Recorrido(String id, String nombre, String descripcion, List<String> espacioIds) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.espacioIds = espacioIds;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<String> getEspacioIds() { return espacioIds; }
    public void setEspacioIds(List<String> espacioIds) { this.espacioIds = espacioIds; }
}
