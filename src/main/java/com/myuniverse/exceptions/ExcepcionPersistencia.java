package com.myuniverse.exceptions;

public class ExcepcionPersistencia extends RuntimeException {
    public ExcepcionPersistencia(String message, Throwable cause) {
        super(message, cause);
    }
}
