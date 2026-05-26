package com.myuniverse.views.terminal;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;

import java.util.Map;

public class PanelDetallesComponente {

    private static final int DETAIL_PANEL_WIDTH = 38;

    public int getPanelWidth() {
        return DETAIL_PANEL_WIDTH;
    }

    public void renderizar(TextGraphics g, int rows, MapContext context) {
        if (context.getCurrentMode() == null) return;

        int cols = g.getSize().getColumns();
        boolean showDetail = cols > 30 + DETAIL_PANEL_WIDTH;
        if (!showDetail) return;

        int detailX = cols - DETAIL_PANEL_WIDTH;
        int panelHeight = rows - 3;

        drawPanel(g, detailX, 1, DETAIL_PANEL_WIDTH, panelHeight, context);
    }

    private void drawPanel(TextGraphics g, int x, int y, int w, int h, MapContext context) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String border = "┌" + UIUtils.repeat('─', w - 2) + "┐";
        g.putString(x, y, border);
        for (int row = 1; row < h - 1; row++) {
            if (y + row < context.getScreen().getTerminalSize().getRows() - 2) {
                g.putString(x, y + row, "│" + String.format("%-" + (w - 2) + "s", "") + "│");
            }
        }
        g.putString(x, Math.min(y + h - 1, context.getScreen().getTerminalSize().getRows() - 3),
                "└" + UIUtils.repeat('─', w - 2) + "┘");

        int innerX = x + 2;
        int innerY = y + 1;
        int maxLen = w - 4;

        Espacio espacio = context.getSelectedEspacio();

        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.setBackgroundColor(ColorScheme.BG);
        String header = (espacio != null) ? "Espacio Details" : "Cell Info";
        g.putString(innerX, innerY, UIUtils.truncate(header, maxLen));
        innerY += 2;

        if (espacio != null) {
            drawEspacioDetails(g, innerX, innerY, maxLen, w, h, x, y, context, espacio);
        } else {
            drawEmptyCellInfo(g, innerX, innerY, maxLen, context);
        }
    }

    private void drawEspacioDetails(TextGraphics g, int innerX, int innerY, int maxLen, int w, int h,
                                  int panelX, int panelY, MapContext context, Espacio espacio) {
        g.setForegroundColor(ColorScheme.FG);
        g.putString(innerX, innerY, UIUtils.truncate("Name:", maxLen));
        innerY++;
        g.setForegroundColor(ColorScheme.SELECTED_FG);
        g.putString(innerX + 2, innerY, UIUtils.truncate(espacio.getNombre(), maxLen - 2));
        innerY++;

        g.setForegroundColor(ColorScheme.FG);
        g.putString(innerX, innerY, UIUtils.truncate("Type:", maxLen));
        innerY++;
        g.setForegroundColor(TipoEspacioStyle.foregroundFor(espacio.getTipo()));
        g.putString(innerX + 2, innerY, UIUtils.truncate(TipoEspacioStyle.labelFor(espacio.getTipo()), maxLen - 2));
        innerY++;

        g.setForegroundColor(ColorScheme.FG);
        g.putString(innerX, innerY, UIUtils.truncate("Planta:", maxLen));
        innerY++;
        Planta planta = context.getCurrentPlanta();
        g.putString(innerX + 2, innerY, UIUtils.truncate(planta != null ? planta.getNombre() : "—", maxLen - 2));
        innerY++;

        g.setForegroundColor(ColorScheme.FG);
        g.putString(innerX, innerY, UIUtils.truncate("Position:", maxLen));
        innerY++;
        g.putString(innerX + 2, innerY, UIUtils.truncate(
                String.format("(%d, %d)", espacio.getCoordenadaX(), espacio.getCoordenadaY()), maxLen - 2));
        innerY++;

        g.setForegroundColor(ColorScheme.FG);
        g.putString(innerX, innerY, UIUtils.truncate("Size:", maxLen));
        innerY++;
        g.putString(innerX + 2, innerY, UIUtils.truncate(
                String.format("%d × %d", espacio.getAncho(), espacio.getAlto()), maxLen - 2));
        innerY++;

        if (espacio.getDescripcion() != null && !espacio.getDescripcion().isEmpty()) {
            innerY++;
            g.setForegroundColor(ColorScheme.FG);
            g.putString(innerX, innerY, UIUtils.truncate("Description:", maxLen));

            String desc = espacio.getDescripcion();
            int lineNum = 0;
            while (!desc.isEmpty() && innerY + 1 + lineNum < panelY + h - 3) {
                int end = Math.min(maxLen, desc.length());
                g.putString(innerX, innerY + 1 + lineNum, UIUtils.truncate(desc.substring(0, end), maxLen));
                desc = desc.substring(end);
                lineNum++;
            }
        }

        Map<String, Integer> routeOverlay = context.getRecorridoOverlay();
        if (routeOverlay != null && routeOverlay.containsKey(espacio.getId())) {
            innerY = panelY + h - 8;
            if (innerY > panelY + 4) {
                g.setForegroundColor(ColorScheme.ROUTE_FG);
                g.putString(innerX, innerY, UIUtils.truncate(
                        "Recorrido step: " + routeOverlay.get(espacio.getId()), maxLen));
                innerY++;
            }
        }

        if (context.isAdmin() && context.getCurrentMode() instanceof ModoNavegarEstado) {
            innerY = panelY + h - 6;
            if (innerY < panelY + h - 2) {
                g.setForegroundColor(ColorScheme.STATUS_FG);
                g.putString(innerX, innerY, "[E]dit  [D]elete");
                innerY++;
                g.putString(innerX, innerY, "[R]esize [M]ove");
                innerY++;
            }
        }
    }

    private void drawEmptyCellInfo(TextGraphics g, int innerX, int innerY, int maxLen, MapContext context) {
        g.setForegroundColor(ColorScheme.FG_DIM);
        g.putString(innerX, innerY, "Empty cell");
        innerY++;
        g.putString(innerX, innerY + 1, UIUtils.truncate(
                String.format("(%d, %d)", context.getCursor().getX(), context.getCursor().getY()), maxLen));
        if (context.isAdmin() && context.getCurrentMode() instanceof ModoNavegarEstado) {
            innerY += 3;
            g.setForegroundColor(ColorScheme.STATUS_FG);
            g.putString(innerX, innerY, "[N]ew espacio");
        }
    }
}