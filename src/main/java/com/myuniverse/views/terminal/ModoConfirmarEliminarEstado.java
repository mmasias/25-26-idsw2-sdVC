package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.myuniverse.exceptions.ExcepcionReglaNegocio;

public class ModoConfirmarEliminarEstado implements MapaModoEstado {

    @Override
    public String obtenerEtiquetaModo() {
        return "DELETE?";
    }

    @Override
    public String construirBarraEstado() {
        return " Enter=confirm eliminar | Esc=cancel";
    }

    @Override
    public void manejarEntrada(KeyStroke key, TerminalSize size) {
    }

    public void handleConfirmDelete(KeyStroke key, TerminalSize size, MapContext context) {
        switch (key.getKeyType()) {
            case Enter:
                if (context.getSelectedEspacio() != null) {
                    try {
                        boolean ok = context.getEspacioController().eliminar(context.getSelectedEspacio().getId());
                        if (ok) {
                            context.setStatusMessage("Espacio deleted");
                            context.setSelectedEspacio(null);
                            context.cargarDatos();
                        } else {
                            context.setStatusMessage("Error deleting espacio");
                        }
                    } catch (ExcepcionReglaNegocio e) {
                        context.setStatusMessage(e.getMessage());
                    }
                }
                context.cambiarModo(new ModoNavegarEstado());
                break;
            case Escape:
                context.cambiarModo(new ModoNavegarEstado());
                context.setStatusMessage("Delete cancelled");
                break;
            default: break;
        }
    }
}