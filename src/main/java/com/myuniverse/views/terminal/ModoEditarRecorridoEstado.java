package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.myuniverse.models.Espacio;

import java.util.ArrayList;

public class ModoEditarRecorridoEstado implements MapaModoEstado {

    @Override
    public String obtenerEtiquetaModo() {
        return "ROUTE EDIT";
    }

    @Override
    public String construirBarraEstado() {
        return " [S]Add espacio | [X]Remove last | Enter=guardar | Esc=cancel";
    }

    @Override
    public void manejarEntrada(KeyStroke key, TerminalSize size) {
    }

    public void handleRecorridoEdit(KeyStroke key, TerminalSize size, MapContext context) {
        GridCursor cursor = context.getCursor();

        switch (key.getKeyType()) {
            case ArrowUp:
                cursor.moveUp();
                break;
            case ArrowDown:
                cursor.moveDown();
                break;
            case ArrowLeft:
                cursor.moveLeft();
                break;
            case ArrowRight:
                cursor.moveRight();
                break;
            case Escape:
                context.setEditingRecorrido(false);
                context.setRecorridoEditEspacioIds(null);
                context.setActiveRecorrido(null);
                context.setRecorridoOverlay(null);
                context.cambiarModo(new ModoNavegarEstado());
                context.setStatusMessage("Recorrido edit cancelled");
                break;
            case Enter:
                if (context.getActiveRecorrido() != null && context.getRecorridoEditEspacioIds() != null) {
                    boolean ok = context.getRecorridoController().actualizar(
                            context.getActiveRecorrido().getId(),
                            context.getActiveRecorrido().getNombre(),
                            context.getActiveRecorrido().getDescripcion(),
                            context.getRecorridoEditEspacioIds() != null
                                    ? new ArrayList<>(context.getRecorridoEditEspacioIds())
                                    : new ArrayList<>());
                    if (ok) {
                        context.cargarDatos();
                        context.setStatusMessage(
                                "Recorrido saved (" + context.getRecorridoEditEspacioIds().size() + " espacios)");
                    } else {
                        context.setStatusMessage("Error saving recorrido");
                    }
                }
                context.setEditingRecorrido(false);
                context.setRecorridoEditEspacioIds(null);
                context.setActiveRecorrido(null);
                context.setRecorridoOverlay(null);
                context.cambiarModo(new ModoNavegarEstado());
                break;
            default:
                if (key.getCharacter() != null) {
                    char ch = Character.toLowerCase(key.getCharacter());
                    if (ch == 's' && context.getSelectedEspacio() != null) {
                        if (!context.getRecorridoEditEspacioIds().contains(context.getSelectedEspacio().getId())) {
                            context.getRecorridoEditEspacioIds().add(context.getSelectedEspacio().getId());
                            context.construirCapaRecorrido();
                            context.setStatusMessage("Added: " + context.getSelectedEspacio().getNombre()
                                    + " (step " + context.getRecorridoEditEspacioIds().size() + ")");
                        } else {
                            context.setStatusMessage("Already in recorrido");
                        }
                    } else if (ch == 'x' && !context.getRecorridoEditEspacioIds().isEmpty()) {
                        context.getRecorridoEditEspacioIds().remove(context.getRecorridoEditEspacioIds().size() - 1);
                        context.construirCapaRecorrido();
                        context.setStatusMessage("Removed last step");
                    }
                }
                break;
        }

        Espacio spaceAtCursor = context.getRenderer().spaceAt(context.getCurrentEspacios(), cursor.getX(),
                cursor.getY());
        context.setSelectedEspacio(spaceAtCursor);
        context.actualizarLimitesGrilla();
    }
}