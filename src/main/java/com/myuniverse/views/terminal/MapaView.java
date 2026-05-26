package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Region;
import com.myuniverse.models.TipoEspacio;
import com.myuniverse.models.Recorrido;
import com.myuniverse.models.Universidad;
import com.myuniverse.exceptions.ExcepcionReglaNegocio;

import java.io.IOException;
import java.util.*;

public class MapaView {
    private final MapContext context;
    private final PanelDetallesComponente detailsPanel;

    private static final int MIN_GRID_COLS = 30;

    public MapaView(TerminalScreen screen, boolean isAdmin, EspacioController controladorEspacio,
            AuthController controladorAutenticacion) {
        IMapRenderer renderer = new GridMapaRenderer();
        this.context = new MapContext(screen, isAdmin, controladorEspacio, controladorAutenticacion, renderer);
        this.detailsPanel = new PanelDetallesComponente();
        this.context.setCursor(new GridCursor(0, 0));
        this.context.setCurrentMode(new ModoNavegarEstado());
    }

    public void setRecorridoController(RecorridoController controladorRecorrido) {
        context.setRecorridoController(controladorRecorrido);
    }

    public void ejecutar() throws IOException {
        context.cargarDatos();
        TerminalSize size = context.getScreen().getTerminalSize();
        context.actualizarLimitesGrilla();

        while (context.isRunning()) {
            size = context.getScreen().doResizeIfNecessary() != null ? context.getScreen().getTerminalSize() : size;
            renderizar(size);
            context.getScreen().refresh();
            KeyStroke key = context.getScreen().readInput();
            if (key != null) {
                size = context.getScreen().getTerminalSize();
                manejarEntrada(key, size);
            }
        }
    }

    private void manejarEntrada(KeyStroke key, TerminalSize size) {
        context.setStatusMessage("");
        MapaModoEstado mode = context.getCurrentMode();

        if (mode instanceof ModoNavegarEstado) {
            ((ModoNavegarEstado) mode).handleNavigation(key, size, context, this);
        } else if (mode instanceof ModoRedimensionarEstado) {
            ((ModoRedimensionarEstado) mode).handleResize(key, size, context);
        } else if (mode instanceof ModoMoverEstado) {
            ((ModoMoverEstado) mode).handleMove(key, size, context);
        } else if (mode instanceof ModoConfirmarEliminarEstado) {
            ((ModoConfirmarEliminarEstado) mode).handleConfirmDelete(key, size, context);
        } else if (mode instanceof ModoEditarRecorridoEstado) {
            ((ModoEditarRecorridoEstado) mode).handleRecorridoEdit(key, size, context);
        }
    }

    private void renderizar(TerminalSize termSize) {
        TextGraphics g = context.getScreen().newTextGraphics();
        int cols = termSize.getColumns();
        int rows = termSize.getRows();
        if (cols < 40 || rows < 10)
            return;

        g.fillRectangle(new TerminalPosition(0, 0), termSize, ' ');

        renderTitleBar(g, cols);

        boolean showDetail = cols > MIN_GRID_COLS + detailsPanel.getPanelWidth();
        int mapW = showDetail ? cols - detailsPanel.getPanelWidth() : cols;
        int mapH = rows - 3;
        if (mapW < 10) {
            mapW = cols;
            showDetail = false;
        }
        if (mapH < 5)
            mapH = 5;

        int vpW = Math.max(1, mapW - 2);
        int vpH = Math.max(1, mapH - 2);
        context.setViewportWidth(vpW);
        context.setViewportHeight(vpH);

        context.getCursor().ensureVisible(vpW, vpH);
        context.getCursor().clampToGrid();

        List<Espacio> spacesToRender = context.getCurrentEspacios();
        Espacio detailEspacio = context.getSelectedEspacio();

        if (context.getCurrentMode() instanceof ModoMoverEstado) {
            ModoMoverEstado moveMode = (ModoMoverEstado) context.getCurrentMode();
            if (detailEspacio != null) {
                spacesToRender = new ArrayList<>(spacesToRender);
                for (int i = 0; i < spacesToRender.size(); i++) {
                    if (spacesToRender.get(i).getId().equals(detailEspacio.getId())) {
                        Espacio preview = new Espacio(
                                detailEspacio.getId(), detailEspacio.getNombre(),
                                detailEspacio.getTipo(), detailEspacio.getDescripcion(),
                                moveMode.getMovePreviewX(), moveMode.getMovePreviewY(),
                                detailEspacio.getAncho(), detailEspacio.getAlto());
                        spacesToRender.set(i, preview);
                        detailEspacio = preview;
                        break;
                    }
                }
            }
        }
        if (context.getCurrentMode() instanceof ModoRedimensionarEstado) {
            ModoRedimensionarEstado resizeMode = (ModoRedimensionarEstado) context.getCurrentMode();
            if (detailEspacio != null) {
                spacesToRender = new ArrayList<>(spacesToRender);
                for (int i = 0; i < spacesToRender.size(); i++) {
                    if (spacesToRender.get(i).getId().equals(detailEspacio.getId())) {
                        Espacio preview = new Espacio(
                                detailEspacio.getId(), detailEspacio.getNombre(),
                                detailEspacio.getTipo(), detailEspacio.getDescripcion(),
                                detailEspacio.getCoordenadaX(), detailEspacio.getCoordenadaY(),
                                resizeMode.getResizeWidth(), resizeMode.getResizeHeight());
                        spacesToRender.set(i, preview);
                        detailEspacio = preview;
                        break;
                    }
                }
            }
        }

        GridMapaRenderer.TextGraphicsAdapter adapter = (x, y, ch, fg, bg) -> {
            if (x < 0 || y < 0 || x >= cols || y >= rows)
                return;
            g.setForegroundColor(fg);
            g.setBackgroundColor(bg);
            g.putString(x, y, String.valueOf(ch));
        };

        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String coordLabel = String.format(" %d,%d ", context.getCursor().getX(), context.getCursor().getY());
        g.putString(1, 1, coordLabel);

        int mapOffsetX = showDetail ? 1 : Math.max(0, (cols - vpW) / 2);
        int mapOffsetY = 2;
        context.setMapOffsetX(mapOffsetX);
        context.setMapOffsetY(mapOffsetY);

        context.getRenderer().renderizar(adapter, context.getCurrentPlanta(), spacesToRender,
                context.getCursor(), detailEspacio,
                mapOffsetX, mapOffsetY, vpW, vpH,
                context.getRecorridoOverlay(),
                context.getRecorridoStepIndex() >= 0 ? context.getRecorridoStepIndex() : null);

        context.getRenderer().renderNameOverlay(adapter, spacesToRender,
                context.getCursor(), detailEspacio,
                mapOffsetX, mapOffsetY, vpW, vpH,
                context.getRecorridoOverlay());

        if (showDetail) {
            detailsPanel.renderizar(g, rows, context);
        }

        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String sepStr = UIUtils.repeat('─', cols);
        g.putString(0, rows - 2, sepStr.substring(0, Math.min(cols, sepStr.length())));

        String status = construirBarraEstado();
        g.setForegroundColor(ColorScheme.STATUS_FG);
        g.setBackgroundColor(ColorScheme.STATUS_BG);
        String padded = String.format("%-" + cols + "s", status);
        if (padded.length() > cols)
            padded = padded.substring(0, cols);
        g.putString(0, rows - 1, padded);
    }

    private void renderTitleBar(TextGraphics g, int cols) {
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.setBackgroundColor(ColorScheme.TITLE_BG);
        Planta planta = context.getCurrentPlanta();
        String floorName = planta != null ? planta.getNombre() : "—";
        String modeLabel = context.getCurrentMode() != null ? context.getCurrentMode().obtenerEtiquetaModo() : "";
        String uniName = context.obtenerUniversidad() != null && context.obtenerUniversidad().getNombre() != null
                ? context.obtenerUniversidad().getNombre() : "myUniverse";
        String title = " " + uniName + " " + (context.isAdmin() ? "[ADMIN]" : "")
                + " ─ " + (context.getCurrentRegion() != null ? context.getCurrentRegion().getNombre() : "")
                + " ─ " + floorName + " ";
        if (!modeLabel.isEmpty())
            title += " ─ " + modeLabel + " ";
        if (context.getActiveRecorrido() != null)
            title += " ─ ⟫ " + context.getActiveRecorrido().getNombre() + " ";
        if (title.length() < cols) {
            title = title + String.format("%" + (cols - title.length()) + "s", "");
        } else {
            title = title.substring(0, cols);
        }
        g.putString(0, 0, title, SGR.BOLD);
    }

    private String construirBarraEstado() {
        if (!context.getStatusMessage().isEmpty()) {
            return " " + context.getStatusMessage();
        }
        if (context.getCurrentMode() instanceof ModoNavegarEstado) {
            return ((ModoNavegarEstado) context.getCurrentMode()).construirBarraEstado(context);
        }
        return context.getCurrentMode().construirBarraEstado();
    }

    public void mostrarDialogoCrear(TerminalSize size) {
        DialogForm form = new DialogForm(context.getScreen());
        String[] labels = { "Name", "Type", "Description", "Width", "Height" };
        String[] defaults = { "", "CLASSROOM", "", "1", "1" };
        TipoEspacio[] types = TipoEspacio.values();
        String[] typeNames = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            typeNames[i] = types[i].getDisplayName();
        }
        int cursorX = context.getCursor().getX();
        int cursorY = context.getCursor().getY();
        form.showForm("Create Espacio (" + cursorX + "," + cursorY + ")", labels, defaults, typeNames, 1);
        String[] result = form.getValues();
        if (result == null)
            return;
        try {
            int ancho = Integer.parseInt(result[3].trim());
            int alto = Integer.parseInt(result[4].trim());
            if (ancho < 1)
                ancho = 1;
            if (alto < 1)
                alto = 1;
            boolean overlap = context.getEspacioController().comprobarSolapamiento(
                    context.getCurrentEspacios(), cursorX, cursorY, ancho, alto, null);
            if (overlap) {
                mostrarMensajeEstado("No se puede crear: se solapa con un espacio", size);
                return;
            }
            Planta planta = context.getCurrentPlanta();
            if (planta == null)
                return;

            TipoEspacio tipo = TipoEspacio.fromPersistedName(result[1].trim());
            Espacio nuevo = context.getEspacioController().crear(
                    result[0].trim(), tipo, result[2].trim(),
                    cursorX, cursorY, ancho, alto, planta.getId());
            if (nuevo != null) {
                context.cargarDatos();
                context.setStatusMessage("Espacio creado");
            } else {
                mostrarMensajeEstado("Error al crear espacio", size);
            }
        } catch (NumberFormatException e) {
            mostrarMensajeEstado("Número inválido para ancho/alto", size);
        } catch (ExcepcionReglaNegocio e) {
            mostrarMensajeEstado(e.getMessage(), size);
        }
    }

    public void mostrarDialogoEditar(TerminalSize size) {
        if (context.getSelectedEspacio() == null)
            return;
        Espacio espacio = context.getSelectedEspacio();
        DialogForm form = new DialogForm(context.getScreen());
        String[] labels = { "Name", "Type", "Description" };
        TipoEspacio[] types = TipoEspacio.values();
        String[] typeNames = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            typeNames[i] = types[i].getDisplayName();
        }
        String[] defaults = { espacio.getNombre(), espacio.getTipo().getDisplayName(), espacio.getDescripcion() };
        form.showForm("Edit Espacio", labels, defaults, typeNames, 1);
        String[] result = form.getValues();
        if (result == null)
            return;
        TipoEspacio tipo = TipoEspacio.fromPersistedName(result[1].trim());
        boolean ok = context.getEspacioController().actualizarCompleto(
                espacio.getId(), result[0].trim(), tipo, result[2].trim(),
                espacio.getCoordenadaX(), espacio.getCoordenadaY(),
                espacio.getAncho(), espacio.getAlto());
        if (ok) {
            context.cargarDatos();
            context.setStatusMessage("Espacio actualizado");
        } else {
            mostrarMensajeEstado("Error al actualizar espacio", size);
        }
    }

    public void mostrarDialogoGestionPlantas(TerminalSize size) {
        PlantaDialog dialog = new PlantaDialog(context.getScreen(), context.getEspacioController());
        dialog.show(context.getCurrentRegion(), context.getPlantaIndex());
        context.cargarDatos();
        int maxIndex = context.getCurrentRegion().obtenerPlantas().size() - 1;
        if (context.getPlantaIndex() > maxIndex)
            context.setPlantaIndex(Math.max(0, maxIndex));
        context.getCursor().setX(0);
        context.getCursor().setY(0);
        context.actualizarLimitesGrilla();
    }

    public void mostrarGestionRecorridos(TerminalSize size) {
        if (context.getRecorridoController() == null)
            return;
        RecorridoDialog dialog = new RecorridoDialog(context.getScreen(), context.getRecorridoController());
        RecorridoDialog.Action action = dialog.mostrarListaAdmin();
        if (action == null)
            return;
        if (action.tipo == RecorridoDialog.Action.ActionType.SELECT) {
            context.setActiveRecorrido(action.recorrido);
            context.setEditingRecorrido(true);
            context.setRecorridoEditEspacioIds(new ArrayList<>(action.recorrido.getEspacioIds()));
            context.setRecorridoStepIndex(-1);
            context.construirCapaRecorrido();
            context.cambiarModo(new ModoEditarRecorridoEstado());
            context.setStatusMessage("Editando recorrido: " + action.recorrido.getNombre());
        } else if (action.tipo == RecorridoDialog.Action.ActionType.CREATE) {
            context.setActiveRecorrido(action.recorrido);
            context.setEditingRecorrido(true);
            context.setRecorridoEditEspacioIds(new ArrayList<>());
            context.setRecorridoStepIndex(-1);
            context.construirCapaRecorrido();
            context.cambiarModo(new ModoEditarRecorridoEstado());
            context.setStatusMessage("Nuevo recorrido: agrega espacios con [S]");
        }
        context.cargarDatos();
    }

    public void mostrarListaRecorridos(TerminalSize size) {
        if (context.getRecorridoController() == null)
            return;
        if (context.getActiveRecorrido() != null) {
            context.setActiveRecorrido(null);
            context.setRecorridoOverlay(null);
            context.setRecorridoStepIndex(-1);
            context.setStatusMessage("Vista de recorrido finalizada");
            return;
        }
        RecorridoDialog dialog = new RecorridoDialog(context.getScreen(), context.getRecorridoController());
        Recorrido recorrido = dialog.mostrarListaVisitante();
        if (recorrido != null) {
            context.setActiveRecorrido(recorrido);
            context.setRecorridoStepIndex(0);
            context.construirCapaRecorrido();
            context.navegarRecorrido(0);
            context.setStatusMessage("Viendo recorrido: " + recorrido.getNombre());
        }
    }

    public void mostrarBuscarEspacio() {
        BuscarEspacioDialog dialog = new BuscarEspacioDialog(context.getScreen(), context.getEspacioController());
        Espacio seleccionado = dialog.mostrar();
        if (seleccionado == null)
            return;

        Universidad uni = context.obtenerUniversidad();
        if (uni == null)
            return;

        Planta plantaDestino = uni.findPlantaContainingEspacio(seleccionado.getId());
        if (plantaDestino != null) {
            boolean changedPlanta = false;
            Region regionDestino = null;
            for (Region r : uni.getRegiones()) {
                for (Planta p : r.obtenerPlantas()) {
                    if (p.getId().equals(plantaDestino.getId())) {
                        regionDestino = r;
                        break;
                    }
                }
                if (regionDestino != null) break;
            }

            if (regionDestino != null) {
                context.setCurrentRegion(regionDestino);
            }

            List<Planta> plantas = context.getCurrentRegion().obtenerPlantas();
            for (int i = 0; i < plantas.size(); i++) {
                if (plantas.get(i).getId().equals(plantaDestino.getId())) {
                    if (i != context.getPlantaIndex()) {
                        context.setPlantaIndex(i);
                        changedPlanta = true;
                    }
                    break;
                }
            }

            if (changedPlanta) {
                context.cargarDatos();
            }
        }

        context.cargarDatos();
        context.getCursor().setX(seleccionado.getCoordenadaX());
        context.getCursor().setY(seleccionado.getCoordenadaY());

        Espacio refreshed = null;
        for (Espacio e : context.getCurrentEspacios()) {
            if (e.getId().equals(seleccionado.getId())) {
                refreshed = e;
                break;
            }
        }
        context.setSelectedEspacio(refreshed);
        context.actualizarLimitesGrilla();
        context.setStatusMessage(refreshed != null ? "Found: " + refreshed.getNombre() : "Espacio not on this floor");
    }

    public void verEspaciosCercanos() {
        Espacio actual = context.getSelectedEspacio();
        if (actual == null) {
            context.setStatusMessage("Cerca: (nada)");
            return;
        }
        LinkedHashMap<String, Espacio> adyacentes = context.getRenderer().spacesAdyacentes(
                context.getCurrentEspacios(), actual);
        if (adyacentes.isEmpty()) {
            context.setStatusMessage("Cerca: (nada)");
        } else {
            List<String> etiquetas = new ArrayList<>();
            for (Map.Entry<String, Espacio> entry : adyacentes.entrySet()) {
                Espacio vecino = entry.getValue();
                etiquetas.add(vecino.getNombre() + " (" + direccionRelativa(actual, vecino) + ")");
            }
            context.setStatusMessage("Cerca: " + String.join(", ", etiquetas));
        }
    }

    private String direccionRelativa(Espacio centro, Espacio vecino) {
        int cx = centro.getCoordenadaX() + centro.getAncho() / 2;
        int cy = centro.getCoordenadaY() + centro.getAlto() / 2;
        int vx = vecino.getCoordenadaX() + vecino.getAncho() / 2;
        int vy = vecino.getCoordenadaY() + vecino.getAlto() / 2;
        StringBuilder sb = new StringBuilder();
        if (vy < cy) sb.append("N");
        else if (vy > cy) sb.append("S");
        if (vx < cx) sb.append("O");
        else if (vx > cx) sb.append("E");
        return sb.length() > 0 ? sb.toString() : "?";
    }

    private void mostrarMensajeEstado(String message, TerminalSize size) {
        context.setStatusMessage(message);
        try {
            renderizar(size);
            context.getScreen().refresh();
            Thread.sleep(1500);
        } catch (Exception e) {
            // ignore
        }
        context.setStatusMessage("");
    }

    public void mostrarDialogoConfig(TerminalSize size) {
        ConfigDialog dialog = new ConfigDialog(context.getScreen(), context.getEspacioController());
        dialog.show();
        context.cargarDatos();
        Universidad uni = context.obtenerUniversidad();
        if (uni != null && !uni.getRegiones().isEmpty()) {
            context.setCurrentRegion(uni.getRegiones().get(0));
            if (context.getPlantaIndex() >= context.getCurrentRegion().obtenerPlantas().size()) {
                context.setPlantaIndex(0);
            }
        }
        context.actualizarLimitesGrilla();
    }
}
