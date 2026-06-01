package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;

public class ModoRedimensionarEstado implements MapaModoEstado {
    private int resizeWidth = 1;
    private int resizeHeight = 1;

    public void initFrom(com.myuniverse.models.Espacio espacio) {
        this.resizeWidth = espacio.getAncho();
        this.resizeHeight = espacio.getAlto();
    }

    public int getResizeWidth() {
        return resizeWidth;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    @Override
    public String obtenerEtiquetaModo() {
        return "RESIZE";
    }

    @Override
    public String construirBarraEstado() {
        return " ←→ ancho | ↑↓ alto | Enter=confirm | Esc=cancel";
    }

    @Override
    public void manejarEntrada(KeyStroke key, TerminalSize size) {
    }

    public void handleResize(KeyStroke key, TerminalSize size, MapContext context) {
        switch (key.getKeyType()) {
            case ArrowRight:
                resizeWidth++;
                break;
            case ArrowLeft:
                if (resizeWidth > 1)
                    resizeWidth--;
                break;
            case ArrowDown:
                resizeHeight++;
                break;
            case ArrowUp:
                if (resizeHeight > 1)
                    resizeHeight--;
                break;
            case Enter:
                if (context.getSelectedEspacio() != null) {
                    boolean overlap = context.getEspacioController().comprobarSolapamiento(
                            context.getCurrentEspacios(),
                            context.getSelectedEspacio().getCoordenadaX(),
                            context.getSelectedEspacio().getCoordenadaY(),
                            resizeWidth, resizeHeight,
                            context.getSelectedEspacio().getId());
                    if (overlap) {
                        context.setStatusMessage("Cannot resize: overlaps another espacio");
                    } else {
                        try {
                            boolean ok = context.getEspacioController().actualizarCompleto(
                                    context.getSelectedEspacio().getId(),
                                    context.getSelectedEspacio().getNombre(),
                                    context.getSelectedEspacio().getTipo(),
                                    context.getSelectedEspacio().getDescripcion(),
                                    context.getSelectedEspacio().getCoordenadaX(),
                                    context.getSelectedEspacio().getCoordenadaY(),
                                    resizeWidth, resizeHeight);
                            if (ok) {
                                context.cargarDatos();
                                context.setStatusMessage("Espacio resized");
                            } else {
                                context.setStatusMessage("Error resizing espacio");
                            }
                        } catch (com.myuniverse.exceptions.ExcepcionReglaNegocio e) {
                            context.setStatusMessage(e.getMessage());
                        }
                    }
                }
                context.cambiarModo(new ModoNavegarEstado());
                context.setSelectedEspacio(context.refrescarEspacioSeleccionado());
                break;
            case Escape:
                context.cambiarModo(new ModoNavegarEstado());
                context.setStatusMessage("Resize cancelled");
                context.setSelectedEspacio(context.refrescarEspacioSeleccionado());
                break;
            default:
                break;
        }
    }
}