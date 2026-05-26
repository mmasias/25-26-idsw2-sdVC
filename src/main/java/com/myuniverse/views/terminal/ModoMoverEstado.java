package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.myuniverse.models.Espacio;

public class ModoMoverEstado implements MapaModoEstado {
    private int movePreviewX;
    private int movePreviewY;

    public void initFrom(Espacio espacio) {
        this.movePreviewX = espacio.getCoordenadaX();
        this.movePreviewY = espacio.getCoordenadaY();
    }

    public int getMovePreviewX() { return movePreviewX; }
    public int getMovePreviewY() { return movePreviewY; }

    @Override
    public String obtenerEtiquetaModo() {
        return "MOVE";
    }

    @Override
    public String construirBarraEstado() {
        return " ←↑↓→ mover | Enter=confirm | Esc=cancel";
    }

    @Override
    public void manejarEntrada(KeyStroke key, TerminalSize size) {
    }

    public void handleMove(KeyStroke key, TerminalSize size, MapContext context) {
        if (context.getSelectedEspacio() == null) {
            context.cambiarModo(new ModoNavegarEstado());
            return;
        }
        int newX = movePreviewX;
        int newY = movePreviewY;
        GridCursor cursor = context.getCursor();

        switch (key.getKeyType()) {
            case ArrowRight: newX++; break;
            case ArrowLeft:  newX = Math.max(0, newX - 1); break;
            case ArrowDown:  newY++; break;
            case ArrowUp:    newY = Math.max(0, newY - 1); break;
            case Enter:
                boolean overlap = context.getEspacioController().comprobarSolapamiento(
                        context.getCurrentEspacios(), newX, newY,
                        context.getSelectedEspacio().getAncho(),
                        context.getSelectedEspacio().getAlto(),
                        context.getSelectedEspacio().getId());
                if (overlap) {
                    context.setStatusMessage("Cannot mover: overlaps another espacio");
                } else {
                    boolean ok = context.getEspacioController().mover(
                            context.getSelectedEspacio().getId(), newX, newY);
                    if (ok) {
                        context.cargarDatos();
                        context.setStatusMessage("Espacio moved");
                    } else {
                        context.setStatusMessage("Error moving espacio");
                    }
                }
                context.cambiarModo(new ModoNavegarEstado());
                context.setSelectedEspacio(context.refrescarEspacioSeleccionado());
                break;
            case Escape:
                context.cambiarModo(new ModoNavegarEstado());
                context.setStatusMessage("Move cancelled");
                context.setSelectedEspacio(context.refrescarEspacioSeleccionado());
                break;
            default: break;
        }

        if (key.getKeyType() != KeyType.Enter && key.getKeyType() != KeyType.Escape) {
            movePreviewX = newX;
            movePreviewY = newY;
            boolean overl = context.getEspacioController().comprobarSolapamiento(
                    context.getCurrentEspacios(), newX, newY,
                    context.getSelectedEspacio().getAncho(),
                    context.getSelectedEspacio().getAlto(),
                    context.getSelectedEspacio().getId());
            if (overl) {
                context.setStatusMessage("OVERLAP! Press Esc to cancel");
            } else {
                context.setStatusMessage("New pos: (" + newX + "," + newY + ") Enter=confirm Esc=cancel");
            }
            cursor.setX(newX);
            cursor.setY(newY);
        }
    }
}
