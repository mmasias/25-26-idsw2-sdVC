package com.myuniverse.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Espacio {
    @SerializedName("id")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("tipo")
    private String typePersisted;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("coordenadaX")
    private int coordenadaX;

    @SerializedName("coordenadaY")
    private int coordenadaY;

    @SerializedName("ancho")
    private int ancho = 1;

    @SerializedName("alto")
    private int alto = 1;

    public Espacio() {
    }

    public Espacio(String id, String nombre, TipoEspacio tipo, String descripcion,
                 int coordenadaX, int coordenadaY, int ancho, int alto) {
        this.id = id;
        this.nombre = nombre;
        this.typePersisted = tipo.getPersistedName();
        this.descripcion = descripcion;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.ancho = Math.max(1, ancho);
        this.alto = Math.max(1, alto);
    }

    public Espacio(String id, String nombre, TipoEspacio tipo, String descripcion,
                 int coordenadaX, int coordenadaY) {
        this(id, nombre, tipo, descripcion, coordenadaX, coordenadaY, 1, 1);
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

    public TipoEspacio getTipo() {
        return TipoEspacio.fromPersistedName(typePersisted);
    }

    public void setTipo(TipoEspacio tipo) {
        this.typePersisted = tipo.getPersistedName();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = Math.max(1, ancho);
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = Math.max(1, alto);
    }

    public boolean contiene(int x, int y) {
        return x >= coordenadaX && x < coordenadaX + ancho
                && y >= coordenadaY && y < coordenadaY + alto;
    }

    public boolean seSolapaCon(int x, int y, int w, int h, String excludeId) {
        if (this.id != null && this.id.equals(excludeId)) {
            return false;
        }
        return coordenadaX < x + w && coordenadaX + ancho > x
                && coordenadaY < y + h && coordenadaY + alto > y;
    }

    public boolean tieneNombreValido() {
        return nombre != null && !nombre.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Espacio espacio = (Espacio) o;
        return Objects.equals(id, espacio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}