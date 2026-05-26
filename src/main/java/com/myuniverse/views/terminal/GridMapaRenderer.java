package com.myuniverse.views.terminal;

import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GridMapaRenderer implements IMapRenderer {

    private final int minWidth;
    private final int minHeight;

    public GridMapaRenderer() {
        this(20, 10);
    }

    public GridMapaRenderer(int minWidth, int minHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    @Override
    public int[] computeGridSize(List<Espacio> espacios) {
        int maxX = minWidth;
        int maxY = minHeight;
        for (Espacio espacio : espacios) {
            int right = espacio.getCoordenadaX() + espacio.getAncho();
            int bottom = espacio.getCoordenadaY() + espacio.getAlto();
            if (right > maxX) maxX = right;
            if (bottom > maxY) maxY = bottom;
        }
        return new int[]{maxX, maxY};
    }

    @Override
    public Espacio spaceAt(List<Espacio> espacios, int x, int y) {
        for (Espacio espacio : espacios) {
            if (espacio.contiene(x, y)) return espacio;
        }
        return null;
    }

    @Override
    public LinkedHashMap<String, Espacio> spacesAdyacentes(List<Espacio> espacios, Espacio actual) {
        LinkedHashMap<String, Espacio> adyacentes = new LinkedHashMap<>();
        if (actual == null) return adyacentes;
        String excludeId = actual.getId();
        int x = actual.getCoordenadaX();
        int y = actual.getCoordenadaY();
        int w = actual.getAncho();
        int h = actual.getAlto();

        addAdyacenteDireccion(espacios, adyacentes, excludeId,
                x, y - 1, x + w - 1, y - 1);
        addAdyacenteDireccion(espacios, adyacentes, excludeId,
                x, y + h, x + w - 1, y + h);
        addAdyacenteDireccion(espacios, adyacentes, excludeId,
                x - 1, y, x - 1, y + h - 1);
        addAdyacenteDireccion(espacios, adyacentes, excludeId,
                x + w, y, x + w, y + h - 1);

        return adyacentes;
    }

    private void addAdyacenteDireccion(List<Espacio> espacios, LinkedHashMap<String, Espacio> adyacentes,
                                       String excludeId, int x1, int y1, int x2, int y2) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        for (int cy = minY; cy <= maxY; cy++) {
            for (int cx = minX; cx <= maxX; cx++) {
                Espacio found = spaceAt(espacios, cx, cy);
                if (found != null && !found.getId().equals(excludeId)
                        && !adyacentes.containsKey(found.getId())) {
                    adyacentes.put(found.getId(), found);
                }
            }
        }
    }

    @Override
    public boolean tieneSolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excludeId) {
        for (Espacio espacio : espacios) {
            if (espacio.seSolapaCon(x, y, ancho, alto, excludeId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void renderizar(TextGraphicsAdapter g, Planta planta, List<Espacio> espacios,
                       GridCursor cursor, Espacio selectedEspacio,
                       int offsetX, int offsetY, int viewportWidth, int viewportHeight,
                       Map<String, Integer> routeOverlay, Integer routeStep) {
        int[] gridSize = computeGridSize(espacios);
        int gridWidth = gridSize[0];
        int gridHeight = gridSize[1];
        cursor.setGridBounds(gridWidth, gridHeight);

        for (int row = 0; row < viewportHeight; row++) {
            int gridY = cursor.getCameraY() + row;
            for (int col = 0; col < viewportWidth; col++) {
                int gridX = cursor.getCameraX() + col;
                if (gridX < 0 || gridY < 0 || gridX >= gridWidth || gridY >= gridHeight) {
                    int screenX = offsetX + col;
                    int screenY = offsetY + row;
                    g.drawChar(screenX, screenY, ' ', ColorScheme.FG_DIM, ColorScheme.BG);
                    continue;
                }

                int screenX = offsetX + col;
                int screenY = offsetY + row;
                boolean isCursor = (gridX == cursor.getX() && gridY == cursor.getY());
                Espacio espacio = spaceAt(espacios, gridX, gridY);

                if (espacio != null) {
                    boolean isSelected = selectedEspacio != null && selectedEspacio.getId().equals(espacio.getId());
                    boolean isInRecorrido = routeOverlay != null && routeOverlay.containsKey(espacio.getId());
                    renderEspacioCell(g, screenX, screenY, gridX, gridY, espacio,
                            isCursor, isSelected, isInRecorrido, routeOverlay, routeStep);
                } else {
                    drawCell(g, screenX, screenY, '·', ColorScheme.FG_DIM, ColorScheme.BG, isCursor, false);
                }
            }
        }
    }

    private void renderEspacioCell(TextGraphicsAdapter g, int screenX, int screenY, int gridX, int gridY,
                                 Espacio espacio, boolean isCursor, boolean isSelected,
                                 boolean isInRecorrido, Map<String, Integer> routeOverlay,
                                 Integer routeStep) {
        int spaceX = espacio.getCoordenadaX();
        int spaceY = espacio.getCoordenadaY();
        int spaceWidth = espacio.getAncho();
        int spaceHeight = espacio.getAlto();
        boolean topLeft = (gridX == spaceX && gridY == spaceY);
        boolean topRight = (gridX == spaceX + spaceWidth - 1 && gridY == spaceY);
        boolean bottomLeft = (gridX == spaceX && gridY == spaceY + spaceHeight - 1);
        boolean bottomRight = (gridX == spaceX + spaceWidth - 1 && gridY == spaceY + spaceHeight - 1);
        boolean topEdge = (gridY == spaceY);
        boolean bottomEdge = (gridY == spaceY + spaceHeight - 1);
        boolean leftEdge = (gridX == spaceX);
        boolean rightEdge = (gridX == spaceX + spaceWidth - 1);

        TextColor typeFg = isSelected ? ColorScheme.SELECTED_FG : TipoEspacioStyle.foregroundFor(espacio.getTipo());
        TextColor cellBg = getCellBackground(espacio, isSelected, isInRecorrido);

        if (spaceWidth == 1 && spaceHeight == 1) {
            Integer step = routeOverlay != null ? routeOverlay.get(espacio.getId()) : null;
            char ch = (step != null) ? UIUtils.digitChar(step) : TipoEspacioStyle.symbolFor(espacio.getTipo());
            if (isInRecorrido) {
                typeFg = ColorScheme.ROUTE_FG;
            }
            drawCell(g, screenX, screenY, ch, typeFg, cellBg, isCursor, isSelected);
            return;
        }

        if (topLeft || topRight || bottomLeft || bottomRight) {
            char corner;
            if (topLeft) corner = '┌';
            else if (topRight) corner = '┐';
            else if (bottomLeft) corner = '└';
            else corner = '┘';
            drawCell(g, screenX, screenY, corner, typeFg, cellBg, isCursor, isSelected);
        } else if (topEdge || bottomEdge) {
            Integer step = routeOverlay != null ? routeOverlay.get(espacio.getId()) : null;
            if (step != null && gridY == spaceY) {
                drawCell(g, screenX, screenY, UIUtils.digitChar(step), ColorScheme.ROUTE_FG, cellBg, isCursor, isSelected);
            } else {
                drawCell(g, screenX, screenY, '─', typeFg, cellBg, isCursor, isSelected);
            }
        } else if (leftEdge || rightEdge) {
            drawCell(g, screenX, screenY, '│', typeFg, cellBg, isCursor, isSelected);
        } else {
            Integer step = routeOverlay != null ? routeOverlay.get(espacio.getId()) : null;
            if (step != null && gridX == spaceX + 1 && gridY == spaceY + 1 && spaceHeight > 2) {
                drawCell(g, screenX, screenY, UIUtils.digitChar(step), ColorScheme.ROUTE_FG, cellBg, isCursor, isSelected);
            } else {
                drawCell(g, screenX, screenY, ' ', typeFg, cellBg, isCursor, isSelected);
            }
        }
    }

    private TextColor getCellBackground(Espacio espacio, boolean isSelected, boolean isInRecorrido) {
        TextColor bg = TipoEspacioStyle.backgroundFor(espacio.getTipo());
        if (isSelected) bg = ColorScheme.SELECTED_BG;
        if (isInRecorrido) bg = ColorScheme.ROUTE_BG;
        return bg;
    }

    private void drawCell(TextGraphicsAdapter g, int x, int y, char ch,
                          TextColor fg, TextColor bg, boolean isCursor, boolean isSelected) {
        if (isCursor) {
            g.drawChar(x, y, ch, ColorScheme.CURSOR_FG, ColorScheme.CURSOR_BG);
        } else {
            g.drawChar(x, y, ch, fg, bg);
        }
    }

    @Override
    public void renderNameOverlay(TextGraphicsAdapter g, List<Espacio> espacios,
                                  GridCursor cursor, Espacio selectedEspacio,
                                  int offsetX, int offsetY, int viewportWidth, int viewportHeight,
                                  Map<String, Integer> routeOverlay) {
        for (Espacio espacio : espacios) {
            int spaceWidth = espacio.getAncho();
            int spaceHeight = espacio.getAlto();
            if (spaceWidth < 3 || spaceHeight < 2) continue;

            boolean isSelected = selectedEspacio != null && selectedEspacio.getId().equals(espacio.getId());
            boolean isInRecorrido = routeOverlay != null && routeOverlay.containsKey(espacio.getId());
            TextColor fg = isSelected ? ColorScheme.SELECTED_FG : TipoEspacioStyle.foregroundFor(espacio.getTipo());
            TextColor bg = getCellBackground(espacio, isSelected, isInRecorrido);

            int nameStartCol = espacio.getCoordenadaX() + 1;
            int maxLen = spaceWidth - 2;
            if (maxLen <= 0) continue;

            String nombre = espacio.getNombre();
            List<String> lines = wrapText(nombre, maxLen);

            int availableRows = spaceHeight - 2;
            int startRow;
            if (lines.size() >= availableRows) {
                startRow = 1;
            } else {
                startRow = 1 + (availableRows - lines.size()) / 2;
            }

            for (int lineIndex = 0; lineIndex < lines.size() && lineIndex < availableRows; lineIndex++) {
                String line = lines.get(lineIndex);
                if (line.length() > maxLen) {
                    line = line.substring(0, maxLen - 1) + "…";
                }
                int lineLen = line.length();
                int padLeft = (maxLen - lineLen) / 2;
                int gridY = espacio.getCoordenadaY() + startRow + lineIndex;
                if (gridY >= espacio.getCoordenadaY() + espacio.getAlto() - 1) break;

                for (int charIndex = 0; charIndex < lineLen; charIndex++) {
                    int gridX = nameStartCol + padLeft + charIndex;
                    if (gridX < cursor.getCameraX() || gridX >= cursor.getCameraX() + viewportWidth ||
                            gridY < cursor.getCameraY() || gridY >= cursor.getCameraY() + viewportHeight)
                        continue;
                    int sx = offsetX + (gridX - cursor.getCameraX());
                    int sy = offsetY + (gridY - cursor.getCameraY());
                    boolean isCursorCell = (gridX == cursor.getX() && gridY == cursor.getY());
                    if (isCursorCell) {
                        g.drawChar(sx, sy, line.charAt(charIndex), ColorScheme.CURSOR_FG, ColorScheme.CURSOR_BG);
                    } else {
                        g.drawChar(sx, sy, line.charAt(charIndex), fg, bg);
                    }
                }
            }
        }
    }

    private List<String> wrapText(String text, int maxLen) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) return lines;
        while (!text.isEmpty()) {
            if (text.length() <= maxLen) {
                lines.add(text);
                break;
            }
            int breakIdx = text.lastIndexOf(' ', maxLen);
            if (breakIdx <= 0) breakIdx = maxLen;
            lines.add(text.substring(0, breakIdx).trim());
            text = text.substring(breakIdx).trim();
        }
        return lines;
    }
}