package com.myuniverse.models;

import java.util.List;

public class Planta {
    private String id;
    private String nombre;
    private int numero;
    private List<Espacio> espacios;

    public Planta() {}

    public Planta(String id, String nombre, int numero, List<Espacio> espacios) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.espacios = espacios;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public List<Espacio> getEspacios() { return espacios; }
    public void setEspacios(List<Espacio> espacios) { this.espacios = espacios; }
}
