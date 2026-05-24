package com.myuniverse.views.terminal;

import com.myuniverse.models.Espacio;
import com.myuniverse.models.Planta;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GridMapaRenderer {

    private final int minWidth;
    private final int minHeight;

    public GridMapaRenderer() {
        this(20, 10);
    }

    public GridMapaRenderer(int minWidth, int minHeight) {
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public int[] computeGridSize(List<Espacio> espacios) {
        int maxX = minWidth;
        int maxY = minHeight;
        for (Espacio e : espacios) {
            int right = e.getCoordenadaX() + e.getAncho();
            int bottom = e.getCoordenadaY() + e.getAlto();
            if (right > maxX) maxX = right;
            if (bottom > maxY) maxY = bottom;
        }
        return new int[]{maxX, maxY};
    }

    public Espacio espacioEn(List<Espacio> espacios, int x, int y) {
        for (Espacio e : espacios) {
            if (e.contiene(x, y)) return e;
        }
        return null;
    }

    public boolean haySolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excluirId) {
        for (Espacio e : espacios) {
            if (e.getId().equals(excluirId)) continue;
            if (seSolapan(x, y, ancho, alto, e.getCoordenadaX(), e.getCoordenadaY(), e.getAncho(), e.getAlto())) {
                return true;
            }
        }
        return false;
    }

    private boolean seSolapan(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    public void render(TextGraphicsAdapter g, Planta planta, List<Espacio> espacios,
                       GridCursor cursor, Espacio selectedEspacio,
                       int offsetX, int offsetY, int viewportW, int viewportH,
                       Map<String, Integer> recorridoOverlay, Integer recorridoStep) {
        int[] gridSize = computeGridSize(espacios);
        int gridW = gridSize[0];
        int gridH = gridSize[1];
        cursor.setGridBounds(gridW, gridH);

        for (int row = 0; row < viewportH; row++) {
            int gy = cursor.getCameraY() + row;
            for (int col = 0; col < viewportW; col++) {
                int gx = cursor.getCameraX() + col;
                if (gx < 0 || gy < 0 || gx >= gridW || gy >= gridH) {
                    int screenX = offsetX + col;
                    int screenY = offsetY + row;
                    g.drawChar(screenX, screenY, ' ', ColorScheme.FG_DIM, ColorScheme.BG);
                    continue;
                }

                int screenX = offsetX + col;
                int screenY = offsetY + row;

                boolean isCursor = (gx == cursor.getX() && gy == cursor.getY());
                Espacio espacio = espacioEn(espacios, gx, gy);

                if (espacio != null) {
                    boolean isSelected = selectedEspacio != null && selectedEspacio.getId().equals(espacio.getId());
                    boolean isInRecorrido = recorridoOverlay != null && recorridoOverlay.containsKey(espacio.getId());
                    renderSpaceCell(g, screenX, screenY, gx, gy, espacio, isCursor, isSelected, isInRecorrido, recorridoOverlay, recorridoStep);
                } else {
                    drawCell(g, screenX, screenY, '·', ColorScheme.FG_DIM, ColorScheme.BG, isCursor, false);
                }
            }
        }
    }

    private void renderSpaceCell(TextGraphicsAdapter g, int screenX, int screenY, int gx, int gy,
                                 Espacio espacio, boolean isCursor, boolean isSelected,
                                 boolean isInRecorrido, Map<String, Integer> recorridoOverlay,
                                 Integer recorridoStep) {
        int ex = espacio.getCoordenadaX();
        int ey = espacio.getCoordenadaY();
        int ew = espacio.getAncho();
        int eh = espacio.getAlto();
        boolean topLeft = (gx == ex && gy == ey);
        boolean topRight = (gx == ex + ew - 1 && gy == ey);
        boolean bottomLeft = (gx == ex && gy == ey + eh - 1);
        boolean bottomRight = (gx == ex + ew - 1 && gy == ey + eh - 1);
        boolean topEdge = (gy == ey);
        boolean bottomEdge = (gy == ey + eh - 1);
        boolean leftEdge = (gx == ex);
        boolean rightEdge = (gx == ex + ew - 1);

        TextColor typeFg = isSelected ? ColorScheme.SELECTED_FG : ColorScheme.fgForType(espacio.getTipo());
        TextColor cellBg = getCellBg(espacio, isSelected, isInRecorrido);

        if (ew == 1 && eh == 1) {
            Integer step = recorridoOverlay != null ? recorridoOverlay.get(espacio.getId()) : null;
            char ch = (step != null) ? digitChar(step) : ColorScheme.charForType(espacio.getTipo());
            if (isInRecorrido) {
                typeFg = ColorScheme.RECORRIDO_FG;
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
            Integer step = recorridoOverlay != null ? recorridoOverlay.get(espacio.getId()) : null;
            if (step != null && gy == ey) {
                drawCell(g, screenX, screenY, digitChar(step), ColorScheme.RECORRIDO_FG, cellBg, isCursor, isSelected);
            } else {
                drawCell(g, screenX, screenY, '─', typeFg, cellBg, isCursor, isSelected);
            }
        } else if (leftEdge || rightEdge) {
            drawCell(g, screenX, screenY, '│', typeFg, cellBg, isCursor, isSelected);
        } else {
            Integer step = recorridoOverlay != null ? recorridoOverlay.get(espacio.getId()) : null;
            if (step != null && gx == ex + 1 && gy == ey + 1 && eh > 2) {
                drawCell(g, screenX, screenY, digitChar(step), ColorScheme.RECORRIDO_FG, cellBg, isCursor, isSelected);
            } else {
                drawCell(g, screenX, screenY, ' ', typeFg, cellBg, isCursor, isSelected);
            }
        }
    }

    private TextColor getCellBg(Espacio espacio, boolean isSelected, boolean isInRecorrido) {
        TextColor bg = ColorScheme.bgForType(espacio.getTipo());
        if (isSelected) bg = ColorScheme.SELECTED_BG;
        if (isInRecorrido) bg = ColorScheme.RECORRIDO_BG;
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

    public void renderNameOverlay(TextGraphicsAdapter g, List<Espacio> espacios,
                                   GridCursor cursor, Espacio selectedEspacio,
                                   int offsetX, int offsetY, int viewportW, int viewportH,
                                   Map<String, Integer> recorridoOverlay) {
        for (Espacio espacio : espacios) {
            int ew = espacio.getAncho();
            int eh = espacio.getAlto();
            if (ew < 3 || eh < 2) continue;

            boolean isSelected = selectedEspacio != null && selectedEspacio.getId().equals(espacio.getId());
            boolean isInRecorrido = recorridoOverlay != null && recorridoOverlay.containsKey(espacio.getId());
            TextColor fg = isSelected ? ColorScheme.SELECTED_FG : ColorScheme.fgForType(espacio.getTipo());
            TextColor bg = getCellBg(espacio, isSelected, isInRecorrido);

            int nameStartCol = espacio.getCoordenadaX() + 1;
            int maxLen = ew - 2;
            if (maxLen <= 0) continue;

            String name = espacio.getNombre();
            List<String> lines = wrapText(name, maxLen);

            int availableRows = eh - 2;
            int startRow;
            if (lines.size() >= availableRows) {
                startRow = 1;
            } else {
                startRow = 1 + (availableRows - lines.size()) / 2;
            }

            for (int li = 0; li < lines.size() && li < availableRows; li++) {
                String line = lines.get(li);
                if (line.length() > maxLen) {
                    line = line.substring(0, maxLen - 1) + "…";
                }
                int lineLen = line.length();
                int padLeft = (maxLen - lineLen) / 2;
                int gy = espacio.getCoordenadaY() + startRow + li;
                if (gy >= espacio.getCoordenadaY() + espacio.getAlto() - 1) break;

                for (int ci = 0; ci < lineLen; ci++) {
                    int gx = nameStartCol + padLeft + ci;
                    if (gx < cursor.getCameraX() || gx >= cursor.getCameraX() + viewportW ||
                        gy < cursor.getCameraY() || gy >= cursor.getCameraY() + viewportH) continue;
                    int sx = offsetX + (gx - cursor.getCameraX());
                    int sy = offsetY + (gy - cursor.getCameraY());
                    boolean isCursorCell = (gx == cursor.getX() && gy == cursor.getY());
                    if (isCursorCell) {
                        g.drawChar(sx, sy, line.charAt(ci), ColorScheme.CURSOR_FG, ColorScheme.CURSOR_BG);
                    } else {
                        g.drawChar(sx, sy, line.charAt(ci), fg, bg);
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

    private char digitChar(int step) {
        if (step >= 1 && step <= 9) return (char) ('0' + step);
        if (step > 9) return '#';
        return '•';
    }

    public interface TextGraphicsAdapter {
        void drawChar(int x, int y, char ch, TextColor fg, TextColor bg);
    }
}