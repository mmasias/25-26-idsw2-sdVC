package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.models.Edificio;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Universidad;
import com.myuniverse.services.GestionEspacioService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FloorDialog {
    private final TerminalScreen screen;
    private final GestionEspacioService service;
    private int selected;

    public FloorDialog(TerminalScreen screen, GestionEspacioService service) {
        this.screen = screen;
        this.service = service;
        this.selected = 0;
    }

    public void show(Edificio edificio, int currentFloorIndex) {
        if (edificio == null) return;
        boolean running = true;

        while (running) {
            List<Planta> plantas = edificio.getPlantas();

            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 50;
            int boxH = Math.max(8, plantas.size() * 2 + 6);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            drawBox(g, boxX, boxY, boxW, boxH, "Floor Manager");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "Floors:");

            for (int i = 0; i < plantas.size(); i++) {
                int row = boxY + 3 + i;
                Planta p = plantas.get(i);
                String line = String.format("%d. %s (Planta %d) - %d spaces",
                        i, p.getNombre(), p.getNumero(), p.getEspacios().size());
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
            g.putString(boxX + 2, hintY, "Up/Down:Select  A:Add  D:Delete");
            g.putString(boxX + 2, hintY + 1, "R:Rename  Q/Enter:Close");

            screen.setCursorPosition(null);
            try { screen.refresh(); } catch (IOException e) { return; }

            try {
                KeyStroke key = screen.readInput();
                if (key == null) continue;

                switch (key.getKeyType()) {
                    case ArrowUp:
                        if (selected > 0) selected--;
                        break;
                    case ArrowDown:
                        if (selected < plantas.size() - 1) selected++;
                        break;
                    case Character:
                        if (key.getCharacter() != null) {
                            char ch = Character.toLowerCase(key.getCharacter());
                            switch (ch) {
                                case 'a':
                                    addFloor(edificio);
                                    break;
                                case 'd':
                                    deleteFloor(edificio);
                                    break;
                                case 'r':
                                    renameFloor(edificio);
                                    break;
                                case 'q':
                                    running = false;
                                    break;
                            }
                        }
                        break;
                    case Enter:
                    case Escape:
                        running = false;
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                running = false;
            }
        }
        screen.setCursorPosition(null);
    }

    private void addFloor(Edificio edificio) {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {"Nueva Planta"};
        form.showForm("Add Floor", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;

        int maxNum = -1;
        for (Planta p : edificio.getPlantas()) {
            if (p.getNumero() > maxNum) maxNum = p.getNumero();
        }

        Planta nueva = new Planta(
                "planta-" + UUID.randomUUID().toString().substring(0, 8),
                result[0].trim(),
                maxNum + 1,
                new java.util.ArrayList<>()
        );
        edificio.getPlantas().add(nueva);
        saveEdificio(edificio);
    }

    private void deleteFloor(Edificio edificio) {
        List<Planta> plantas = edificio.getPlantas();
        if (plantas.isEmpty() || selected >= plantas.size()) return;
        Planta target = plantas.get(selected);
        if (!target.getEspacios().isEmpty()) {
            showMessage("Cannot delete: floor has spaces");
            return;
        }
        plantas.remove(selected);
        saveEdificio(edificio);
        if (selected > 0) selected--;
    }

    private void renameFloor(Edificio edificio) {
        List<Planta> plantas = edificio.getPlantas();
        if (plantas.isEmpty() || selected >= plantas.size()) return;
        Planta target = plantas.get(selected);

        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {target.getNombre()};
        form.showForm("Rename Floor", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;

        target.setNombre(result[0].trim());
        saveEdificio(edificio);
    }

    private void saveEdificio(Edificio edificio) {
        Universidad uni = service.obtenerUniversidad();
        for (int i = 0; i < uni.getEdificios().size(); i++) {
            if (uni.getEdificios().get(i).getId().equals(edificio.getId())) {
                uni.getEdificios().set(i, edificio);
                break;
            }
        }
        service.guardarUniversidad(uni);
    }

    private void showMessage(String msg) {
        TerminalSize size = screen.getTerminalSize();
        TextGraphics g = screen.newTextGraphics();
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        int boxW = Math.max(msg.length() + 6, 20);
        int boxH = 5;
        int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
        int boxY = Math.max(0, (size.getRows() - boxH) / 2);

        drawBox(g, boxX, boxY, boxW, boxH, "Attention");
        g.setForegroundColor(ColorScheme.SELECTED_FG);
        g.setBackgroundColor(ColorScheme.BG);
        g.putString(boxX + 3, boxY + 2, msg);

try { screen.refresh(); } catch (IOException e) { return; }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void drawBox(TextGraphics g, int x, int y, int w, int h, String title) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        StringBuilder topSb = new StringBuilder("┌");
        for (int i = 0; i < w - 2; i++) topSb.append('─');
        topSb.append('┐');
        g.putString(x, y, topSb.toString());
        for (int i = 1; i < h - 1; i++) {
            g.putString(x, y + i, "│" + String.format("%" + (w - 2) + "s", "") + "│");
        }
        StringBuilder botSb = new StringBuilder("└");
        for (int i = 0; i < w - 2; i++) botSb.append('─');
        botSb.append('┘');
        g.putString(x, y + h - 1, botSb.toString());
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.putString(x + 2, y, " " + title + " ");
    }
}