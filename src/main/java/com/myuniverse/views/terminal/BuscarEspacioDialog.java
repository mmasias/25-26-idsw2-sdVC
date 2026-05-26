package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.models.Espacio;

import java.io.IOException;
import java.util.List;

public class BuscarEspacioDialog {
    private final TerminalScreen screen;
    private final EspacioController controller;
    private int selected;

    public BuscarEspacioDialog(TerminalScreen screen, EspacioController controller) {
        this.screen = screen;
        this.controller = controller;
        this.selected = 0;
    }

    public Espacio mostrar() {
        String termino = pedirTermino();
        if (termino == null || termino.isBlank()) {
            return null;
        }

        List<Espacio> resultados = controller.filtrar(termino);
        if (resultados.isEmpty()) {
            mostrarSinResultados(termino);
            return null;
        }

        return seleccionarResultado(resultados);
    }

    private String pedirTermino() {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Search"};
        String[] defaults = {""};
        form.showForm("Search Espacio", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null) {
            return null;
        }
        return result[0].trim();
    }

    private Espacio seleccionarResultado(List<Espacio> resultados) {
        selected = 0;
        boolean running = true;

        while (running) {
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 60;
            int boxH = Math.max(8, resultados.size() * 2 + 5);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Search Results");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "Found " + resultados.size() + " espacio(s):");

            for (int i = 0; i < resultados.size(); i++) {
                int row = boxY + 4 + i;
                Espacio e = resultados.get(i);
                String line = String.format("%d. %-30s %s", i,
                        truncate(e.getNombre(), 28),
                        e.getTipo().getDisplayName());
                if (i == selected) {
                    g.setForegroundColor(ColorScheme.CURSOR_FG);
                    g.setBackgroundColor(ColorScheme.CURSOR_BG);
                } else {
                    g.setForegroundColor(ColorScheme.FG);
                    g.setBackgroundColor(ColorScheme.BG);
                }
                g.putString(boxX + 2, row, String.format("%-56s", line));
            }

            int hintY = boxY + boxH - 2;
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, hintY, "Up/Dn:Select  Enter:Go  Esc:Cancel");

            screen.setCursorPosition(null);
            try { screen.refresh(); } catch (IOException e) { return null; }

            try {
                KeyStroke key = screen.readInput();
                if (key == null) continue;

                switch (key.getKeyType()) {
                    case ArrowUp:
                        if (selected > 0) selected--;
                        break;
                    case ArrowDown:
                        if (selected < resultados.size() - 1) selected++;
                        break;
                    case Enter:
                        if (!resultados.isEmpty() && selected < resultados.size()) {
                            return resultados.get(selected);
                        }
                        break;
                    case Escape:
                        return null;
                    default:
                        break;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    private void mostrarSinResultados(String termino) {
        TerminalSize size = screen.getTerminalSize();
        TextGraphics g = screen.newTextGraphics();
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        String message = "No spaces found for: " + termino;
        int boxW = Math.max(message.length() + 6, 30);
        int boxH = 5;
        int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
        int boxY = Math.max(0, (size.getRows() - boxH) / 2);

        UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Search");
        g.setForegroundColor(ColorScheme.SELECTED_FG);
        g.setBackgroundColor(ColorScheme.BG);
        g.putString(boxX + 3, boxY + 2, message);

        try { screen.refresh(); } catch (IOException e) { return; }
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
    }
}