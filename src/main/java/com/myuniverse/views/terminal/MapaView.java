package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.models.*;
import com.myuniverse.services.GestionEspacioService;

import java.io.IOException;
import java.util.*;

public class MapaView {
    private final TerminalScreen screen;
    private final boolean isAdmin;
    private final EspacioController espacioCtrl;
    private final AuthController authCtrl;
    private final GestionEspacioService gestionService;
    private final GridMapaRenderer renderer;
    private RecorridoController recorridoCtrl;

    private Universidad universidad;
    private Edificio edificioActual;
    private int plantaIndex;
    private GridCursor cursor;
    private Espacio selectedEspacio;
    private Mode mode;
    private String statusMessage;
    private int resizeAncho;
    private int resizeAlto;
    private int movePreviewX;
    private int movePreviewY;
    private boolean running;

    private Recorrido activeRecorrido;
    private Map<String, Integer> recorridoOverlay;
    private int recorridoStepIndex;
    private boolean editandoRecorrido;
    private List<String> recorridoEditIds;

    private int mapOffsetX;
    private int mapOffsetY;
    private int vpW;
    private int vpH;

    private enum Mode {
        NAVIGATE, RESIZE, MOVE, CONFIRM_DELETE, RECORRIDO_EDIT
    }

    private static final int DETAIL_PANEL_WIDTH = 38;
    private static final int MIN_GRID_COLS = 30;

    public MapaView(TerminalScreen screen, boolean isAdmin, EspacioController espacioCtrl,
                    AuthController authCtrl, GestionEspacioService gestionService) {
        this.screen = screen;
        this.isAdmin = isAdmin;
        this.espacioCtrl = espacioCtrl;
        this.authCtrl = authCtrl;
        this.gestionService = gestionService;
        this.renderer = new GridMapaRenderer();
        this.mode = Mode.NAVIGATE;
        this.statusMessage = "";
        this.running = true;
        this.recorridoOverlay = null;
        this.recorridoStepIndex = -1;
        this.editandoRecorrido = false;
        this.recorridoEditIds = new ArrayList<>();
    }

    public void setRecorridoController(RecorridoController ctrl) {
        this.recorridoCtrl = ctrl;
    }

    public void run() throws IOException {
        loadData();
        TerminalSize size = screen.getTerminalSize();
        cursor = new GridCursor(0, 0);
        updateGridBounds();

        while (running) {
            size = screen.doResizeIfNecessary() != null ? screen.getTerminalSize() : size;
            render(size);
            screen.refresh();
            KeyStroke key = screen.readInput();
            if (key != null) {
                size = screen.getTerminalSize();
                handleInput(key, size);
            }
        }
    }

    private void loadData() {
        universidad = gestionService.obtenerUniversidad();
        if (universidad == null) universidad = new Universidad();
        if (universidad.getEdificios().isEmpty()) return;
        edificioActual = universidad.getEdificios().get(0);
        if (plantaIndex < 0 || plantaIndex >= edificioActual.getPlantas().size()) plantaIndex = 0;
    }

    private Planta getPlantaActual() {
        if (edificioActual == null || plantaIndex >= edificioActual.getPlantas().size()) return null;
        return edificioActual.getPlantas().get(plantaIndex);
    }

    private List<Espacio> getEspaciosActuales() {
        Planta p = getPlantaActual();
        return (p != null) ? p.getEspacios() : Collections.emptyList();
    }

    private void updateGridBounds() {
        int[] size = renderer.computeGridSize(getEspaciosActuales());
        cursor.setGridBounds(size[0], size[1]);
        cursor.clampToGrid();
    }

    private Espacio refreshSelectedEspacio() {
        if (selectedEspacio == null) return null;
        for (Espacio e : getEspaciosActuales()) {
            if (e.getId().equals(selectedEspacio.getId())) return e;
        }
        return null;
    }

    private void buildRecorridoOverlay() {
        recorridoOverlay = null;
        if (activeRecorrido == null) return;
        Map<String, Integer> map = new LinkedHashMap<>();
        List<String> ids = editandoRecorrido ? recorridoEditIds : activeRecorrido.getEspacioIds();
        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), i + 1);
        }
        recorridoOverlay = map;
    }

    private void render(TerminalSize termSize) {
        TextGraphics g = screen.newTextGraphics();
        int cols = termSize.getColumns();
        int rows = termSize.getRows();
        if (cols < 40 || rows < 10) return;

        g.fillRectangle(new TerminalPosition(0, 0), termSize, ' ');

        // Title bar
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.setBackgroundColor(ColorScheme.TITLE_BG);
        Planta planta = getPlantaActual();
        String floorName = planta != null ? planta.getNombre() : "—";
        String modeLabel = getModeLabel();
        String title = " myUniverse " + (isAdmin ? "[ADMIN]" : "") + " ─ " + (edificioActual != null ? edificioActual.getNombre() : "") + " ─ " + floorName + " ";
        if (!modeLabel.isEmpty()) title += " ─ " + modeLabel + " ";
        if (activeRecorrido != null) title += " ─ ⟫ " + activeRecorrido.getNombre() + " ";
        if (title.length() < cols) {
            title = title + String.format("%" + (cols - title.length()) + "s", "");
        } else {
            title = title.substring(0, cols);
        }
        g.putString(0, 0, title, SGR.BOLD);

        boolean showDetail = cols > MIN_GRID_COLS + DETAIL_PANEL_WIDTH;
        int mapW = showDetail ? cols - DETAIL_PANEL_WIDTH : cols;
        int mapH = rows - 3;
        if (mapW < 10) { mapW = cols; showDetail = false; }
        if (mapH < 5) mapH = 5;

        vpW = Math.max(1, mapW - 2);
        vpH = Math.max(1, mapH - 2);

        cursor.ensureVisible(vpW, vpH);
        cursor.clampToGrid();

        List<Espacio> espaciosParaRender = getEspaciosActuales();
        Espacio detailEspacio = selectedEspacio;
        if (mode == Mode.MOVE && selectedEspacio != null) {
            espaciosParaRender = new ArrayList<>(espaciosParaRender);
            for (int i = 0; i < espaciosParaRender.size(); i++) {
                if (espaciosParaRender.get(i).getId().equals(selectedEspacio.getId())) {
                    Espacio preview = new Espacio(
                            selectedEspacio.getId(), selectedEspacio.getNombre(),
                            selectedEspacio.getTipo(), selectedEspacio.getDescripcion(),
                            movePreviewX, movePreviewY, selectedEspacio.getAncho(), selectedEspacio.getAlto());
                    espaciosParaRender.set(i, preview);
                    detailEspacio = preview;
                    break;
                }
            }
        }
        if (mode == Mode.RESIZE && selectedEspacio != null) {
            espaciosParaRender = new ArrayList<>(espaciosParaRender);
            for (int i = 0; i < espaciosParaRender.size(); i++) {
                if (espaciosParaRender.get(i).getId().equals(selectedEspacio.getId())) {
                    Espacio preview = new Espacio(
                            selectedEspacio.getId(), selectedEspacio.getNombre(),
                            selectedEspacio.getTipo(), selectedEspacio.getDescripcion(),
                            selectedEspacio.getCoordenadaX(), selectedEspacio.getCoordenadaY(),
                            resizeAncho, resizeAlto);
                    espaciosParaRender.set(i, preview);
                    detailEspacio = preview;
                    break;
                }
            }
        }

        GridMapaRenderer.TextGraphicsAdapter adapter = (x, y, ch, fg, bg) -> {
            if (x < 0 || y < 0 || x >= cols || y >= rows) return;
            g.setForegroundColor(fg);
            g.setBackgroundColor(bg);
            g.putString(x, y, String.valueOf(ch));
        };

        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String coordLabel = String.format(" %d,%d ", cursor.getX(), cursor.getY());
        g.putString(1, 1, coordLabel);

        mapOffsetX = showDetail ? 1 : Math.max(0, (cols - vpW) / 2);
        mapOffsetY = 2;

        renderer.render(adapter, planta, espaciosParaRender, cursor, detailEspacio,
                mapOffsetX, mapOffsetY, vpW, vpH, recorridoOverlay, recorridoStepIndex >= 0 ? recorridoStepIndex : null);

        renderer.renderNameOverlay(adapter, espaciosParaRender, cursor, detailEspacio,
                mapOffsetX, mapOffsetY, vpW, vpH, recorridoOverlay);

        if (showDetail) {
            int detailX = cols - DETAIL_PANEL_WIDTH;
            renderDetailPanel(g, detailX, 1, DETAIL_PANEL_WIDTH, rows - 3, detailEspacio);
        }

        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String sepStr = repeat('─', cols);
        g.putString(0, rows - 2, sepStr.substring(0, Math.min(cols, sepStr.length())));

        String status = buildStatusBar();
        g.setForegroundColor(ColorScheme.STATUS_FG);
        g.setBackgroundColor(ColorScheme.STATUS_BG);
        String padded = String.format("%-" + cols + "s", status);
        if (padded.length() > cols) padded = padded.substring(0, cols);
        g.putString(0, rows - 1, padded);
    }

    private String getModeLabel() {
        switch (mode) {
            case RESIZE: return "RESIZE";
            case MOVE: return "MOVE";
            case CONFIRM_DELETE: return "DELETE?";
                       case RECORRIDO_EDIT: return "ROUTE EDIT";
            default: return "";
        }
    }

    private String buildStatusBar() {
        if (!statusMessage.isEmpty()) return " " + statusMessage;

        switch (mode) {
            case RESIZE:
                return " ←→ width | ↑↓ height | Enter=confirm | Esc=cancel";
            case MOVE:
                return " ←↑↓→ move | Enter=confirm | Esc=cancel";
            case CONFIRM_DELETE:
                return " Enter=confirm delete | Esc=cancel";
            case RECORRIDO_EDIT:
                return " [S]Add space | [X]Remove last | Enter=save | Esc=cancel";
            default:
                StringBuilder sb = new StringBuilder(" ↑↓←→ Navigate | Tab:Floor");
                if (activeRecorrido != null) {
                    sb.append(" | A/D prev/next");
                }
                if (isAdmin) {
                    sb.append(" | [E]dit [D]el [R]esize [M]ove [N]ew [F]loors [G]Routes");
                } else {
                    sb.append(" | [T]ours");
                }
                return sb.toString();
        }
    }

    private void renderDetailPanel(TextGraphics g, int x, int y, int w, int h, Espacio esp) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String border = "┌" + repeat('─', w - 2) + "┐";
        g.putString(x, y, border);
        for (int row = 1; row < h - 1; row++) {
            if (y + row < screen.getTerminalSize().getRows() - 2) {
                g.putString(x, y + row, "│" + String.format("%-" + (w - 2) + "s", "") + "│");
            }
        }
        g.putString(x, Math.min(y + h - 1, screen.getTerminalSize().getRows() - 3),
                "└" + repeat('─', w - 2) + "┘");

        int innerX = x + 2;
        int innerY = y + 1;
        int maxLen = w - 4;

        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.setBackgroundColor(ColorScheme.BG);
        String header = (esp != null) ? "Space Details" : "Cell Info";
        g.putString(innerX, innerY, truncate(header, maxLen));
        innerY += 2;

        if (esp != null) {
            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, truncate("Name:", maxLen)); innerY++;
            g.setForegroundColor(ColorScheme.SELECTED_FG);
            g.putString(innerX + 2, innerY, truncate(esp.getNombre(), maxLen - 2)); innerY++;

            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, truncate("Type:", maxLen)); innerY++;
            g.setForegroundColor(ColorScheme.fgForType(esp.getTipo()));
            g.putString(innerX + 2, innerY, truncate(ColorScheme.labelForType(esp.getTipo()), maxLen - 2)); innerY++;

            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, truncate("Floor:", maxLen)); innerY++;
            Planta p = getPlantaActual();
            g.putString(innerX + 2, innerY, truncate(p != null ? p.getNombre() : "—", maxLen - 2)); innerY++;

            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, truncate("Position:", maxLen)); innerY++;
            g.putString(innerX + 2, innerY, truncate(String.format("(%d, %d)", esp.getCoordenadaX(), esp.getCoordenadaY()), maxLen - 2)); innerY++;

            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, truncate("Size:", maxLen)); innerY++;
            g.putString(innerX + 2, innerY, truncate(String.format("%d × %d", esp.getAncho(), esp.getAlto()), maxLen - 2)); innerY++;

            if (esp.getDescripcion() != null && !esp.getDescripcion().isEmpty()) {
                innerY++;
                g.setForegroundColor(ColorScheme.FG);
                g.putString(innerX, innerY, truncate("Description:", maxLen));
                String desc = esp.getDescripcion();
                int lineNum = 0;
                while (!desc.isEmpty() && innerY + 1 + lineNum < y + h - 3) {
                    int end = Math.min(maxLen, desc.length());
                    g.putString(innerX, innerY + 1 + lineNum, truncate(desc.substring(0, end), maxLen));
                    desc = desc.substring(end);
                    lineNum++;
                }
            }

            if (recorridoOverlay != null && recorridoOverlay.containsKey(esp.getId())) {
                innerY = y + h - 8;
                if (innerY > y + 4) {
                    g.setForegroundColor(ColorScheme.RECORRIDO_FG);
                    g.putString(innerX, innerY, truncate("Route step: " + recorridoOverlay.get(esp.getId()), maxLen));
                    innerY++;
                }
            }

            if (isAdmin && mode == Mode.NAVIGATE) {
                innerY = y + h - 6;
                if (innerY < y + h - 2) {
                    g.setForegroundColor(ColorScheme.STATUS_FG);
                    g.putString(innerX, innerY, "[E]dit  [D]elete"); innerY++;
                    g.putString(innerX, innerY, "[R]esize [M]ove");  innerY++;
                }
            }
        } else {
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.putString(innerX, innerY, "Empty cell");
            innerY++;
            g.putString(innerX, innerY + 1, truncate(String.format("(%d, %d)", cursor.getX(), cursor.getY()), maxLen));
            if (isAdmin && mode == Mode.NAVIGATE) {
                innerY += 3;
                g.setForegroundColor(ColorScheme.STATUS_FG);
                g.putString(innerX, innerY, "[N]ew space");
            }
        }
    }

    private void handleInput(KeyStroke key, TerminalSize size) {
        statusMessage = "";
        switch (mode) {
            case NAVIGATE:
                handleNavigate(key, size);
                break;
            case RESIZE:
                handleResize(key, size);
                break;
            case MOVE:
                handleMove(key, size);
                break;
            case CONFIRM_DELETE:
                handleConfirmDelete(key, size);
                break;
            case RECORRIDO_EDIT:
                handleRecorridoEdit(key, size);
                break;
        }
        if (mode == Mode.NAVIGATE || mode == Mode.RECORRIDO_EDIT) {
            Espacio atCursor = renderer.espacioEn(getEspaciosActuales(), cursor.getX(), cursor.getY());
            selectedEspacio = atCursor;
        }
        updateGridBounds();
    }

    private void handleNavigate(KeyStroke key, TerminalSize size) {
        switch (key.getKeyType()) {
            case ArrowUp:    cursor.moveUp(); break;
            case ArrowDown:  cursor.moveDown(); break;
            case ArrowLeft:  cursor.moveLeft(); break;
            case ArrowRight: cursor.moveRight(); break;
            case Tab:
                if (edificioActual != null && !edificioActual.getPlantas().isEmpty()) {
                    plantaIndex = (plantaIndex + 1) % edificioActual.getPlantas().size();
                    selectedEspacio = null;
                    cursor.setX(0); cursor.setY(0);
                    activeRecorrido = null;
                    recorridoOverlay = null;
                    recorridoStepIndex = -1;
                    loadData();
                    updateGridBounds();
                }
                break;
            case Escape:
                if (activeRecorrido != null) {
                    activeRecorrido = null;
                    recorridoOverlay = null;
                    recorridoStepIndex = -1;
                    statusMessage = "Route view ended";
                } else {
                    running = false;
                }
                break;
            default:
                if (key.getCharacter() != null) {
                    char ch = Character.toLowerCase(key.getCharacter());
                    if (isAdmin) {
                        switch (ch) {
                            case 'n': showCreateDialog(size); break;
                            case 'e': showEditDialog(size); break;
                            case 'd':
                                if (selectedEspacio != null) {
                                    mode = Mode.CONFIRM_DELETE;
                                    statusMessage = "Delete '" + selectedEspacio.getNombre() + "'? Enter=Yes Esc=No";
                                }
                                break;
                            case 'r':
                                if (selectedEspacio != null) {
                                    mode = Mode.RESIZE;
                                    resizeAncho = selectedEspacio.getAncho();
                                    resizeAlto = selectedEspacio.getAlto();
                                    statusMessage = "Resizing...";
                                }
                                break;
                            case 'm':
                                if (selectedEspacio != null) {
                                    mode = Mode.MOVE;
                                    movePreviewX = selectedEspacio.getCoordenadaX();
                                    movePreviewY = selectedEspacio.getCoordenadaY();
                                    statusMessage = "Moving...";
                                }
                                break;
                            case 'f': showFloorManagerDialog(size); break;
                            case 'g': showRecorridoManager(size); break;
                            case 'q': running = false; break;
                        }
                    }
                    switch (ch) {
                        case 't': showRecorridoList(size); break;
                        case 'a': navigateRecorrido(-1); break;
                        case 'd': navigateRecorrido(1); break;
                        case 'q':
                            if (!isAdmin) running = false;
                            break;
                    }
                }
                break;
        }
    }

    private void handleResize(KeyStroke key, TerminalSize size) {
        switch (key.getKeyType()) {
            case ArrowRight: resizeAncho++; break;
            case ArrowLeft:  if (resizeAncho > 1) resizeAncho--; break;
            case ArrowDown:  resizeAlto++; break;
            case ArrowUp:    if (resizeAlto > 1) resizeAlto--; break;
            case Enter:
                if (selectedEspacio != null) {
                    boolean overlap = renderer.haySolapamiento(
                            getEspaciosActuales(),
                            selectedEspacio.getCoordenadaX(), selectedEspacio.getCoordenadaY(),
                            resizeAncho, resizeAlto, selectedEspacio.getId());
                    if (overlap) {
                        statusMessage = "Cannot resize: overlaps another space";
                    } else {
                        boolean ok = gestionService.actualizarEspacioCompleto(
                                selectedEspacio.getId(), selectedEspacio.getNombre(),
                                selectedEspacio.getTipo(), selectedEspacio.getDescripcion(),
                                selectedEspacio.getCoordenadaX(), selectedEspacio.getCoordenadaY(),
                                resizeAncho, resizeAlto);
                        if (ok) {
                            loadData();
                            statusMessage = "Space resized";
                        } else {
                            statusMessage = "Error resizing space";
                        }
                    }
                }
                mode = Mode.NAVIGATE;
                selectedEspacio = refreshSelectedEspacio();
                break;
            case Escape:
                mode = Mode.NAVIGATE;
                statusMessage = "Resize cancelled";
                selectedEspacio = refreshSelectedEspacio();
                break;
            default: break;
        }
    }

    private void handleMove(KeyStroke key, TerminalSize size) {
        if (selectedEspacio == null) { mode = Mode.NAVIGATE; return; }
        int newX = movePreviewX;
        int newY = movePreviewY;

        switch (key.getKeyType()) {
            case ArrowRight: newX++; break;
            case ArrowLeft:  newX = Math.max(0, newX - 1); break;
            case ArrowDown:  newY++; break;
            case ArrowUp:    newY = Math.max(0, newY - 1); break;
            case Enter:
                boolean overlap = renderer.haySolapamiento(
                        getEspaciosActuales(), newX, newY,
                        selectedEspacio.getAncho(), selectedEspacio.getAlto(), selectedEspacio.getId());
                if (overlap) {
                    statusMessage = "Cannot move: overlaps another space";
                } else {
                    boolean ok = gestionService.actualizarEspacio(
                            selectedEspacio.getId(), selectedEspacio.getNombre(),
                            selectedEspacio.getTipo(), selectedEspacio.getDescripcion(),
                            newX, newY);
                    if (ok) {
                        loadData();
                        statusMessage = "Space moved";
                    } else {
                        statusMessage = "Error moving space";
                    }
                }
                mode = Mode.NAVIGATE;
                selectedEspacio = refreshSelectedEspacio();
                break;
            case Escape:
                mode = Mode.NAVIGATE;
                statusMessage = "Move cancelled";
                selectedEspacio = refreshSelectedEspacio();
                break;
            default: break;
        }

        if (key.getKeyType() != KeyType.Enter && key.getKeyType() != KeyType.Escape) {
            movePreviewX = newX;
            movePreviewY = newY;
            boolean overl = renderer.haySolapamiento(
                    getEspaciosActuales(), newX, newY,
                    selectedEspacio.getAncho(), selectedEspacio.getAlto(), selectedEspacio.getId());
            if (overl) {
                statusMessage = "OVERLAP! Press Esc to cancel";
            } else {
                statusMessage = "New pos: (" + newX + "," + newY + ") Enter=confirm Esc=cancel";
            }
            cursor.setX(newX);
            cursor.setY(newY);
        }
    }

    private void handleConfirmDelete(KeyStroke key, TerminalSize size) {
        switch (key.getKeyType()) {
            case Enter:
                if (selectedEspacio != null) {
                    boolean ok = gestionService.eliminarEspacio(selectedEspacio.getId());
                    if (ok) {
                        statusMessage = "Space deleted";
                        selectedEspacio = null;
                        loadData();
                    } else {
                        statusMessage = "Error deleting space";
                    }
                }
                mode = Mode.NAVIGATE;
                break;
            case Escape:
                mode = Mode.NAVIGATE;
                statusMessage = "Delete cancelled";
                break;
            default: break;
        }
    }

    private void handleRecorridoEdit(KeyStroke key, TerminalSize size) {
        switch (key.getKeyType()) {
            case ArrowUp:    cursor.moveUp(); break;
            case ArrowDown:  cursor.moveDown(); break;
            case ArrowLeft:  cursor.moveLeft(); break;
            case ArrowRight: cursor.moveRight(); break;
            case Escape:
                editandoRecorrido = false;
                recorridoEditIds = null;
                activeRecorrido = null;
                recorridoOverlay = null;
                mode = Mode.NAVIGATE;
                statusMessage = "Route edit cancelled";
                break;
            case Enter:
                if (activeRecorrido != null && recorridoEditIds != null) {
                    boolean ok = gestionService.actualizarRecorrido(
                            activeRecorrido.getId(), activeRecorrido.getNombre(),
                            activeRecorrido.getDescripcion(), new ArrayList<>(recorridoEditIds));
                    if (ok) {
                        loadData();
                        statusMessage = "Route saved (" + recorridoEditIds.size() + " spaces)";
                    } else {
                        statusMessage = "Error saving route";
                    }
                }
                editandoRecorrido = false;
                recorridoEditIds = null;
                activeRecorrido = null;
                recorridoOverlay = null;
                mode = Mode.NAVIGATE;
                break;
            default:
                if (key.getCharacter() != null) {
                    char ch = Character.toLowerCase(key.getCharacter());
                    if (ch == 's' && selectedEspacio != null) {
                        if (!recorridoEditIds.contains(selectedEspacio.getId())) {
                            recorridoEditIds.add(selectedEspacio.getId());
                            buildRecorridoOverlay();
                            statusMessage = "Added: " + selectedEspacio.getNombre() + " (step " + recorridoEditIds.size() + ")";
                        } else {
                            statusMessage = "Already in route";
                        }
                    } else if (ch == 'x' && !recorridoEditIds.isEmpty()) {
                        String removed = recorridoEditIds.remove(recorridoEditIds.size() - 1);
                        buildRecorridoOverlay();
                        statusMessage = "Removed last step";
                    }
                }
                break;
        }
    }

    private void navigateRecorrido(int direction) {
        if (activeRecorrido == null) return;
        List<String> ids = editandoRecorrido ? recorridoEditIds : activeRecorrido.getEspacioIds();
        if (ids.isEmpty()) return;
        if (recorridoStepIndex < 0) {
            recorridoStepIndex = 0;
        } else {
            recorridoStepIndex = (recorridoStepIndex + direction + ids.size()) % ids.size();
        }
        String espacioId = ids.get(recorridoStepIndex);
        Espacio esp = null;
        for (Espacio e : getEspaciosActuales()) {
            if (e.getId().equals(espacioId)) { esp = e; break; }
        }
        if (esp != null) {
            cursor.setX(esp.getCoordenadaX());
            cursor.setY(esp.getCoordenadaY());
            selectedEspacio = esp;
        }
        statusMessage = "Step " + (recorridoStepIndex + 1) + "/" + ids.size() + (esp != null ? " - " + esp.getNombre() : "");
    }

    private void showCreateDialog(TerminalSize size) {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name", "Type", "Description", "Width", "Height"};
        String[] defaults = {"", "AULA", "", "1", "1"};
        String[] types = {"AULA", "LABORATORIO", "BIBLIOTECA", "CAFETERÍA", "AUDITORIO", "OFICINA", "BAÑO", "OTRO"};
        int cursorX = cursor.getX();
        int cursorY = cursor.getY();
        form.showForm("Create Space (" + cursorX + "," + cursorY + ")", labels, defaults, types, 1);
        String[] result = form.getValues();
        if (result == null) return;
        try {
            int ancho = Integer.parseInt(result[3].trim());
            int alto = Integer.parseInt(result[4].trim());
            if (ancho < 1) ancho = 1;
            if (alto < 1) alto = 1;
            boolean overlap = renderer.haySolapamiento(getEspaciosActuales(), cursorX, cursorY, ancho, alto, null);
            if (overlap) { showStatusMessage("Cannot create: overlaps existing space", size); return; }
            Planta planta = getPlantaActual();
            if (planta == null) return;
            Espacio nuevo = gestionService.crearEspacio(
                    result[0].trim(), result[1].trim(), result[2].trim(),
                    cursorX, cursorY, ancho, alto, planta.getId());
            if (nuevo != null) {
                loadData();
                statusMessage = "Space created";
            } else {
                showStatusMessage("Error creating space", size);
            }
        } catch (NumberFormatException e) {
            showStatusMessage("Invalid number for width/height", size);
        }
    }

    private void showEditDialog(TerminalSize size) {
        if (selectedEspacio == null) return;
        Espacio esp = selectedEspacio;
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name", "Type", "Description"};
        String[] types = {"AULA", "LABORATORIO", "BIBLIOTECA", "CAFETERÍA", "AUDITORIO", "OFICINA", "BAÑO", "OTRO"};
        String[] defaults = {esp.getNombre(), esp.getTipo(), esp.getDescripcion()};
        form.showForm("Edit Space", labels, defaults, types, 1);
        String[] result = form.getValues();
        if (result == null) return;
        boolean ok = gestionService.actualizarEspacioCompleto(esp.getId(), result[0].trim(), result[1].trim(),
                result[2].trim(), esp.getCoordenadaX(), esp.getCoordenadaY(),
                esp.getAncho(), esp.getAlto());
        if (ok) { loadData(); statusMessage = "Space updated"; }
        else { showStatusMessage("Error updating space", size); }
    }

    private void showFloorManagerDialog(TerminalSize size) {
        FloorDialog dialog = new FloorDialog(screen, gestionService);
        dialog.show(edificioActual, plantaIndex);
        loadData();
        plantaIndex = Math.min(plantaIndex, edificioActual.getPlantas().size() - 1);
        if (plantaIndex < 0) plantaIndex = 0;
        cursor.setX(0); cursor.setY(0);
        updateGridBounds();
    }

    private void showRecorridoManager(TerminalSize size) {
        if (recorridoCtrl == null) return;
        RecorridoDialog dialog = new RecorridoDialog(screen, recorridoCtrl, gestionService);
        RecorridoDialog.Action action = dialog.showAdminList();
        if (action == null) return;
        if (action.type == RecorridoDialog.Action.ActionType.SELECT) {
            activeRecorrido = action.recorrido;
            editandoRecorrido = true;
            recorridoEditIds = new ArrayList<>(activeRecorrido.getEspacioIds());
            recorridoStepIndex = -1;
            buildRecorridoOverlay();
            mode = Mode.RECORRIDO_EDIT;
            statusMessage = "Editing route: " + activeRecorrido.getNombre();
        } else if (action.type == RecorridoDialog.Action.ActionType.CREATE) {
            activeRecorrido = action.recorrido;
            editandoRecorrido = true;
            recorridoEditIds = new ArrayList<>();
            recorridoStepIndex = -1;
            buildRecorridoOverlay();
            mode = Mode.RECORRIDO_EDIT;
            statusMessage = "New route: add spaces with [S]";
        }
        loadData();
    }

    private void showRecorridoList(TerminalSize size) {
        if (recorridoCtrl == null) return;
        if (activeRecorrido != null) {
            activeRecorrido = null;
            recorridoOverlay = null;
            recorridoStepIndex = -1;
            statusMessage = "Route view ended";
            return;
        }
        RecorridoDialog dialog = new RecorridoDialog(screen, recorridoCtrl, gestionService);
        Recorrido recorrido = dialog.showVisitorList();
        if (recorrido != null) {
            activeRecorrido = recorrido;
            recorridoStepIndex = 0;
            buildRecorridoOverlay();
            navigateRecorrido(0);
            statusMessage = "Viewing route: " + recorrido.getNombre();
        }
    }

    private void showStatusMessage(String msg, TerminalSize size) {
        statusMessage = msg;
        try { render(size); screen.refresh(); Thread.sleep(1500); } catch (Exception e) { /* ignore */ }
        statusMessage = "";
    }

    private String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }
}