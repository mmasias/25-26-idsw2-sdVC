package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.models.Recorrido;
import com.myuniverse.services.GestionEspacioService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class RecorridoDialog {
    private final TerminalScreen screen;
    private final RecorridoController ctrl;
    private final GestionEspacioService service;
    private int selected;

    public RecorridoDialog(TerminalScreen screen, RecorridoController ctrl, GestionEspacioService service) {
        this.screen = screen;
        this.ctrl = ctrl;
        this.service = service;
        this.selected = 0;
    }

    public static class Action {
        public enum ActionType { SELECT, CREATE }
        public ActionType type;
        public Recorrido recorrido;
        public Action(ActionType type, Recorrido recorrido) {
            this.type = type;
            this.recorrido = recorrido;
        }
    }

    public Action showAdminList() {
        boolean running = true;
        while (running) {
            List<Recorrido> recorridos = ctrl.obtenerTodos();
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 60;
            int boxH = Math.max(10, recorridos.size() * 2 + 8);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            drawBox(g, boxX, boxY, boxW, boxH, "Route Manager [ADMIN]");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "Routes:");

            for (int i = 0; i < recorridos.size(); i++) {
                int row = boxY + 4 + i;
                Recorrido r = recorridos.get(i);
                String line = String.format("%d. %s (%d spaces)", i, r.getNombre(), r.getEspacioIds().size());
                if (i == selected) {
                    g.setForegroundColor(ColorScheme.CURSOR_FG);
                    g.setBackgroundColor(ColorScheme.CURSOR_BG);
                } else {
                    g.setForegroundColor(ColorScheme.FG);
                    g.setBackgroundColor(ColorScheme.BG);
                }
                g.putString(boxX + 2, row, String.format("%-56s", line));
            }

            int hintY = boxY + boxH - 4;
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, hintY, "↑/↓: Select  Enter:Edit  C:Create  D:Delete");
            g.putString(boxX + 2, hintY + 1, "Esc:Cancel");

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
                        if (selected < recorridos.size() - 1) selected++;
                        break;
                    case Enter:
                        if (!recorridos.isEmpty() && selected < recorridos.size()) {
                            return new Action(Action.ActionType.SELECT, recorridos.get(selected));
                        }
                        break;
                    case Escape:
                        return null;
                    default:
                        if (key.getCharacter() != null) {
                            char ch = Character.toLowerCase(key.getCharacter());
                            if (ch == 'c') {
                                Recorrido nuevo = showCreateForm();
                                if (nuevo != null) return new Action(Action.ActionType.CREATE, nuevo);
                            } else if (ch == 'd' && !recorridos.isEmpty() && selected < recorridos.size()) {
                                ctrl.eliminar(recorridos.get(selected).getId());
                                if (selected > 0) selected--;
                            }
                        }
                        break;
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    public Recorrido showVisitorList() {
        boolean running = true;
        while (running) {
            List<Recorrido> recorridos = ctrl.obtenerTodos();
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 50;
            int boxH = Math.max(8, recorridos.size() * 2 + 6);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            drawBox(g, boxX, boxY, boxW, boxH, "Routes");

            for (int i = 0; i < recorridos.size(); i++) {
                int row = boxY + 2 + i;
                Recorrido r = recorridos.get(i);
                String line = String.format("%d. %s (%d spaces)", i, r.getNombre(), r.getEspacioIds().size());
                if (i == selected) {
                    g.setForegroundColor(ColorScheme.CURSOR_FG);
                    g.setBackgroundColor(ColorScheme.CURSOR_BG);
                } else {
                    g.setForegroundColor(ColorScheme.FG);
                    g.setBackgroundColor(ColorScheme.BG);
                }
                g.putString(boxX + 2, row, String.format("%-46s", line));
            }

            int hintY = boxY + boxH - 3;
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, hintY, "↑/↓:Select  Enter:View  Esc:Cancel");

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
                        if (selected < recorridos.size() - 1) selected++;
                        break;
                    case Enter:
                        if (!recorridos.isEmpty() && selected < recorridos.size()) {
                            return recorridos.get(selected);
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

    private Recorrido showCreateForm() {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name", "Description"};
        String[] defaults = {"", ""};
        form.showForm("Create Route", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null) return null;
        if (result[0].trim().isEmpty()) return null;
        return ctrl.crear(result[0].trim(), result[1].trim());
    }

    private void drawBox(TextGraphics g, int x, int y, int w, int h, String title) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String top = "┌" + "─".repeat(Math.max(0, w - 2)) + "┐";
        String bot = "└" + "─".repeat(Math.max(0, w - 2)) + "┘";
        g.putString(x, y, top);
        for (int i = 1; i < h - 1; i++) {
            g.putString(x, y + i, "│" + String.format("%" + (w - 2) + "s", "") + "│");
        }
        g.putString(x, y + h - 1, bot);
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.putString(x + 2, y, " " + title + " ");
    }
}