package com.myuniverse.models;

public class Espacio {
    private String id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private int coordenadaX;
    private int coordenadaY;
    private int ancho = 1;
    private int alto = 1;

    public Espacio() {}

    public Espacio(String id, String nombre, String tipo, String descripcion, int coordenadaX, int coordenadaY) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
    }

    public Espacio(String id, String nombre, String tipo, String descripcion, int coordenadaX, int coordenadaY, int ancho, int alto) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.ancho = ancho;
        this.alto = alto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCoordenadaX() { return coordenadaX; }
    public void setCoordenadaX(int coordenadaX) { this.coordenadaX = coordenadaX; }

    public int getCoordenadaY() { return coordenadaY; }
    public void setCoordenadaY(int coordenadaY) { this.coordenadaY = coordenadaY; }

    public int getAncho() { return ancho; }
    public void setAncho(int ancho) { this.ancho = ancho; }

    public int getAlto() { return alto; }
    public void setAlto(int alto) { this.alto = alto; }

    public boolean contiene(int x, int y) {
        return x >= coordenadaX && x < coordenadaX + ancho
            && y >= coordenadaY && y < coordenadaY + alto;
    }
}
