package com.myuniverse.views.terminal;

import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Region;
import com.myuniverse.models.Recorrido;
import com.myuniverse.models.Espacio;
import com.myuniverse.models.Universidad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapContext {
    private final TerminalScreen screen;
    private final boolean admin;
    private final EspacioController controladorEspacio;
    private final AuthController controladorAutenticacion;
    private final IMapRenderer renderer;
    private RecorridoController controladorRecorrido;

    private Universidad universidad;
    private Region currentRegion;
    private int indicePlanta;
    private GridCursor cursor;
    private Espacio selectedEspacio;
    private Recorrido activeRecorrido;
    private Map<String, Integer> routeOverlay;
    private int routeStepIndex;
    private boolean editingRecorrido;
    private List<String> routeEditEspacioIds;
    private String statusMessage;
    private boolean running;

    private int resizeWidth;
    private int resizeHeight;
    private int movePreviewX;
    private int movePreviewY;

    private MapaModoEstado currentMode;

    private int mapOffsetX;
    private int mapOffsetY;
    private int viewportWidth;
    private int viewportHeight;

    public MapContext(TerminalScreen screen, boolean admin, EspacioController controladorEspacio,
            AuthController controladorAutenticacion, IMapRenderer renderer) {
        this.screen = screen;
        this.admin = admin;
        this.controladorEspacio = controladorEspacio;
        this.controladorAutenticacion = controladorAutenticacion;
        this.renderer = renderer;
        this.statusMessage = "";
        this.running = true;
        this.routeOverlay = null;
        this.routeStepIndex = -1;
        this.editingRecorrido = false;
        this.routeEditEspacioIds = new ArrayList<>();
    }

    public void setRecorridoController(RecorridoController controladorRecorrido) {
        this.controladorRecorrido = controladorRecorrido;
    }

    public TerminalScreen getScreen() {
        return screen;
    }

    public boolean isAdmin() {
        return admin;
    }

    public EspacioController getEspacioController() {
        return controladorEspacio;
    }

    public AuthController getAuthController() {
        return controladorAutenticacion;
    }

    public IMapRenderer getRenderer() {
        return renderer;
    }

    public RecorridoController getRecorridoController() {
        return controladorRecorrido;
    }

    public Universidad obtenerUniversidad() {
        return universidad;
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public int getPlantaIndex() {
        return indicePlanta;
    }

    public GridCursor getCursor() {
        return cursor;
    }

    public Espacio getSelectedEspacio() {
        return selectedEspacio;
    }

    public Recorrido getActiveRecorrido() {
        return activeRecorrido;
    }

    public Map<String, Integer> getRecorridoOverlay() {
        return routeOverlay;
    }

    public int getRecorridoStepIndex() {
        return routeStepIndex;
    }

    public boolean isEditingRecorrido() {
        return editingRecorrido;
    }

    public List<String> getRecorridoEditEspacioIds() {
        return routeEditEspacioIds;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isRunning() {
        return running;
    }

    public int getResizeWidth() {
        return resizeWidth;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    public int getMovePreviewX() {
        return movePreviewX;
    }

    public int getMovePreviewY() {
        return movePreviewY;
    }

    public MapaModoEstado getCurrentMode() {
        return currentMode;
    }

    public int getMapOffsetX() {
        return mapOffsetX;
    }

    public int getMapOffsetY() {
        return mapOffsetY;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }

    public void setCurrentRegion(Region currentRegion) {
        this.currentRegion = currentRegion;
    }

    public void setPlantaIndex(int indicePlanta) {
        this.indicePlanta = indicePlanta;
    }

    public void setCursor(GridCursor cursor) {
        this.cursor = cursor;
    }

    public void setSelectedEspacio(Espacio selectedEspacio) {
        this.selectedEspacio = selectedEspacio;
    }

    public void setActiveRecorrido(Recorrido activeRecorrido) {
        this.activeRecorrido = activeRecorrido;
    }

    public void setRecorridoOverlay(Map<String, Integer> routeOverlay) {
        this.routeOverlay = routeOverlay;
    }

    public void setRecorridoStepIndex(int routeStepIndex) {
        this.routeStepIndex = routeStepIndex;
    }

    public void setEditingRecorrido(boolean editingRecorrido) {
        this.editingRecorrido = editingRecorrido;
    }

    public void setRecorridoEditEspacioIds(List<String> routeEditEspacioIds) {
        this.routeEditEspacioIds = routeEditEspacioIds;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setResizeWidth(int resizeWidth) {
        this.resizeWidth = resizeWidth;
    }

    public void setResizeHeight(int resizeHeight) {
        this.resizeHeight = resizeHeight;
    }

    public void setMovePreviewX(int movePreviewX) {
        this.movePreviewX = movePreviewX;
    }

    public void setMovePreviewY(int movePreviewY) {
        this.movePreviewY = movePreviewY;
    }

    public void setCurrentMode(MapaModoEstado currentMode) {
        this.currentMode = currentMode;
    }

    public void setMapOffsetX(int mapOffsetX) {
        this.mapOffsetX = mapOffsetX;
    }

    public void setMapOffsetY(int mapOffsetY) {
        this.mapOffsetY = mapOffsetY;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public Planta getCurrentPlanta() {
        if (currentRegion == null || indicePlanta >= currentRegion.obtenerPlantas().size()) return null;
        return currentRegion.obtenerPlantas().get(indicePlanta);
    }

    public List<Espacio> getCurrentEspacios() {
        Planta planta = getCurrentPlanta();
        return planta != null ? planta.getEspacios() : Collections.emptyList();
    }

    public Espacio refrescarEspacioSeleccionado() {
        if (selectedEspacio == null)
            return null;
        for (Espacio espacio : getCurrentEspacios()) {
            if (espacio.getId().equals(selectedEspacio.getId()))
                return espacio;
        }
        return null;
    }

    public void cargarDatos() {
        universidad = controladorEspacio.obtenerUniversidad();
        if (universidad == null)
            universidad = new Universidad();
        if (universidad.getRegiones().isEmpty())
            return;
        currentRegion = universidad.getRegiones().get(0);
        if (indicePlanta < 0 || indicePlanta >= currentRegion.obtenerPlantas().size())
            indicePlanta = 0;
    }

    public void actualizarLimitesGrilla() {
        int[] size = renderer.computeGridSize(getCurrentEspacios());
        cursor.setGridBounds(size[0], size[1]);
        cursor.clampToGrid();
    }

    public void construirCapaRecorrido() {
        routeOverlay = null;
        if (activeRecorrido == null)
            return;
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> ids = editingRecorrido ? routeEditEspacioIds : activeRecorrido.getEspacioIds();
        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), i + 1);
        }
        routeOverlay = map;
    }

    public void navegarRecorrido(int direccion) {
        if (activeRecorrido == null)
            return;
        List<String> ids = editingRecorrido ? routeEditEspacioIds : activeRecorrido.getEspacioIds();
        if (ids.isEmpty())
            return;
        if (routeStepIndex < 0) {
            routeStepIndex = 0;
        } else {
            routeStepIndex = (routeStepIndex + direccion + ids.size()) % ids.size();
        }
        String idEspacio = ids.get(routeStepIndex);
        Espacio espacio = null;
        for (Espacio s : getCurrentEspacios()) {
            if (s.getId().equals(idEspacio)) {
                espacio = s;
                break;
            }
        }
        if (espacio != null) {
            cursor.setX(espacio.getCoordenadaX());
            cursor.setY(espacio.getCoordenadaY());
            selectedEspacio = espacio;
        }
        statusMessage = "Step " + (routeStepIndex + 1) + "/" + ids.size()
                + (espacio != null ? " - " + espacio.getNombre() : "");
    }

    public void cambiarModo(MapaModoEstado newMode) {
        this.currentMode = newMode;
    }
}