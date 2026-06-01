package com.myuniverse.exceptions;

public class ExcepcionReglaNegocio extends RuntimeException {
    private final String ruleId;

    public ExcepcionReglaNegocio(String ruleId, String message) {
        super(message);
        this.ruleId = ruleId;
    }

    public ExcepcionReglaNegocio(String ruleId, String message, Throwable cause) {
        super(message, cause);
        this.ruleId = ruleId;
    }

    public String getRuleId() {
        return ruleId;
    }
}