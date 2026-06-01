package com.myuniverse.models;

public enum TipoEspacio {
    CLASSROOM("Classroom", "AULA", 'A'),
    LABORATORY("Laboratory", "LABORATORIO", 'L'),
    LIBRARY("Library", "BIBLIOTECA", 'B'),
    CAFETERIA("Cafeteria", "CAFETERÍA", 'C'),
    AUDITORIUM("Auditorium", "AUDITORIO", 'D'),
    OFFICE("Office", "OFICINA", 'O'),
    RESTROOM("Restroom", "BAÑO", 'N'),
    OTHER("Other", "OTRO", '?');

    private final String displayName;
    private final String persistedName;
    private final char symbol;

    TipoEspacio(String displayName, String persistedName, char symbol) {
        this.displayName = displayName;
        this.persistedName = persistedName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPersistedName() {
        return persistedName;
    }

    public char getSymbol() {
        return symbol;
    }

    public static TipoEspacio fromPersistedName(String nombre) {
        if (nombre == null) return OTHER;
        for (TipoEspacio tipo : values()) {
            if (tipo.persistedName.equalsIgnoreCase(nombre)) {
                return tipo;
            }
        }
        try {
            return valueOf(nombre.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}