package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.models.Recorrido;

import java.io.IOException;
import java.util.List;

public class RecorridoDialog {
    private final TerminalScreen screen;
    private final RecorridoController controller;
    private int selected;

    public RecorridoDialog(TerminalScreen screen, RecorridoController controller) {
        this.screen = screen;
        this.controller = controller;
        this.selected = 0;
    }

    public static class Action {
        public enum ActionType {
            SELECT, CREATE
        }

        public ActionType tipo;
        public Recorrido recorrido;

        public Action(ActionType tipo, Recorrido recorrido) {
            this.tipo = tipo;
            this.recorrido = recorrido;
        }
    }

    public Action mostrarListaAdmin() {
        boolean running = true;
        while (running) {
            List<Recorrido> routes = controller.obtenerTodos();
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 60;
            int boxH = Math.max(10, routes.size() * 2 + 8);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Recorrido Manager [ADMIN]");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "Recorridos:");

            for (int i = 0; i < routes.size(); i++) {
                int row = boxY + 4 + i;
                Recorrido r = routes.get(i);
                String line = String.format("%d. %s (%d espacios)", i, r.getNombre(), r.getEspacioIds().size());
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
            g.putString(boxX + 2, hintY, "Up/Down:Select  Enter:Edit  C:Create  D:Delete");
            g.putString(boxX + 2, hintY + 1, "Esc:Cancel");

            screen.setCursorPosition(null);
            try {
                screen.refresh();
            } catch (IOException e) {
                return null;
            }

            try {
                KeyStroke key = screen.readInput();
                if (key == null)
                    continue;

                switch (key.getKeyType()) {
                    case ArrowUp:
                        if (selected > 0)
                            selected--;
                        break;
                    case ArrowDown:
                        if (selected < routes.size() - 1)
                            selected++;
                        break;
                    case Enter:
                        if (!routes.isEmpty() && selected < routes.size()) {
                            return new Action(Action.ActionType.SELECT, routes.get(selected));
                        }
                        break;
                    case Escape:
                        return null;
                    default:
                        if (key.getCharacter() != null) {
                            char ch = Character.toLowerCase(key.getCharacter());
                            if (ch == 'c') {
                                Recorrido nuevo = showCreateForm();
                                if (nuevo != null)
                                    return new Action(Action.ActionType.CREATE, nuevo);
                            } else if (ch == 'd' && !routes.isEmpty() && selected < routes.size()) {
                                controller.eliminar(routes.get(selected).getId());
                                if (selected > 0)
                                    selected--;
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

    public Recorrido mostrarListaVisitante() {
        boolean running = true;
        while (running) {
            List<Recorrido> routes = controller.obtenerTodos();
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 50;
            int boxH = Math.max(8, routes.size() * 2 + 6);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Recorridos");

            for (int i = 0; i < routes.size(); i++) {
                int row = boxY + 2 + i;
                Recorrido r = routes.get(i);
                String line = String.format("%d. %s (%d espacios)", i, r.getNombre(), r.getEspacioIds().size());
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
            g.putString(boxX + 2, hintY, "Up/Down:Select  Enter:View  Esc:Cancel");

            screen.setCursorPosition(null);
            try {
                screen.refresh();
            } catch (IOException e) {
                return null;
            }

            try {
                KeyStroke key = screen.readInput();
                if (key == null)
                    continue;

                switch (key.getKeyType()) {
                    case ArrowUp:
                        if (selected > 0)
                            selected--;
                        break;
                    case ArrowDown:
                        if (selected < routes.size() - 1)
                            selected++;
                        break;
                    case Enter:
                        if (!routes.isEmpty() && selected < routes.size()) {
                            return routes.get(selected);
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
        String[] labels = { "Name", "Description" };
        String[] defaults = { "", "" };
        form.showForm("Create Recorrido", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null)
            return null;
        if (result[0].trim().isEmpty())
            return null;
        return controller.crear(result[0].trim(), result[1].trim());
    }
}