package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.myuniverse.models.Espacio;

public class ModoNavegarEstado implements MapaModoEstado {

    @Override
    public String obtenerEtiquetaModo() {
        return "";
    }

    @Override
    public String construirBarraEstado() {
        return null;
    }

    @Override
    public void manejarEntrada(KeyStroke key, TerminalSize size) {
    }

    public String construirBarraEstado(MapContext context) {
        if (context == null)
            return "";
        StringBuilder sb = new StringBuilder(" ↑↓←→ Navigate | Tab:Planta | [B]uscar [C]ercanos");
        if (context.getActiveRecorrido() != null) {
            sb.append(" | A/D prev/next");
        }
        if (context.isAdmin()) {
            sb.append(" | [E]dit [D]el [R]esize [M]ove [N]ew [F]loors [G]Recorridos [O]Config");
        } else {
            sb.append(" | [T]ours");
        }
        return sb.toString();
    }

    public void handleNavigation(KeyStroke key, TerminalSize size, MapContext context, MapaView mapView) {
        context.setStatusMessage("");
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
            case Tab:
                if (context.getCurrentRegion() != null && !context.getCurrentRegion().obtenerPlantas().isEmpty()) {
                    int newIndex = (context.getPlantaIndex() + 1) % context.getCurrentRegion().obtenerPlantas().size();
                    context.setPlantaIndex(newIndex);
                    context.setSelectedEspacio(null);
                    cursor.setX(0);
                    cursor.setY(0);
                    context.setActiveRecorrido(null);
                    context.setRecorridoOverlay(null);
                    context.setRecorridoStepIndex(-1);
                    context.cargarDatos();
                    context.actualizarLimitesGrilla();
                }
                break;
            case Escape:
                if (context.getActiveRecorrido() != null) {
                    context.setActiveRecorrido(null);
                    context.setRecorridoOverlay(null);
                    context.setRecorridoStepIndex(-1);
                    context.setStatusMessage("Vista de recorrido finalizada");
                } else {
                    context.setRunning(false);
                }
                break;
            default:
                if (key.getCharacter() != null) {
                    char ch = Character.toLowerCase(key.getCharacter());
                    if (context.isAdmin()) {
                        switch (ch) {
                            case 'n':
                                mapView.mostrarDialogoCrear(size);
                                break;
                            case 'e':
                                mapView.mostrarDialogoEditar(size);
                                break;
                            case 'd':
                                if (context.getSelectedEspacio() != null) {
                                    context.cambiarModo(new ModoConfirmarEliminarEstado());
                                    context.setStatusMessage(
                                            "Delete '" + context.getSelectedEspacio().getNombre() + "'? Enter=Yes Esc=No");
                                }
                                break;
                            case 'r':
                                if (context.getSelectedEspacio() != null) {
                                    ModoRedimensionarEstado resize = new ModoRedimensionarEstado();
                                    resize.initFrom(context.getSelectedEspacio());
                                    context.cambiarModo(resize);
                                    context.setStatusMessage("Resizing...");
                                }
                                break;
                            case 'm':
                                if (context.getSelectedEspacio() != null) {
                                    ModoMoverEstado mover = new ModoMoverEstado();
                                    mover.initFrom(context.getSelectedEspacio());
                                    context.cambiarModo(mover);
                                    context.setStatusMessage("Moving...");
                                }
                                break;
                            case 'f':
                                mapView.mostrarDialogoGestionPlantas(size);
                                break;
                            case 'g':
                                mapView.mostrarGestionRecorridos(size);
                                break;
                            case 'o':
                                mapView.mostrarDialogoConfig(size);
                                break;
                            case 'q':
                                context.setRunning(false);
                                break;
                        }
                    }
                    switch (ch) {
                        case 't':
                            mapView.mostrarListaRecorridos(size);
                            break;
                        case 'b':
                            mapView.mostrarBuscarEspacio();
                            break;
                        case 'c':
                            mapView.verEspaciosCercanos();
                            break;
                        case 'a':
                            context.navegarRecorrido(-1);
                            break;
                        case 'd':
                            if (!context.isAdmin()) {
                                context.navegarRecorrido(1);
                            }
                            break;
                        case 'q':
                            if (!context.isAdmin())
                                context.setRunning(false);
                            break;
                    }
                }
                break;
        }

        Espacio spaceAtCursor = context.getRenderer().spaceAt(context.getCurrentEspacios(), cursor.getX(), cursor.getY());
        context.setSelectedEspacio(spaceAtCursor);
        context.actualizarLimitesGrilla();
    }
}