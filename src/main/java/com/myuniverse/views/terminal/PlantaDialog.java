package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.models.Region;
import com.myuniverse.models.Planta;
import com.myuniverse.models.Universidad;
import com.myuniverse.controllers.EspacioController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlantaDialog {
    private final TerminalScreen screen;
    private final EspacioController controladorEspacio;
    private int selected;

    public PlantaDialog(TerminalScreen screen, EspacioController controladorEspacio) {
        this.screen = screen;
        this.controladorEspacio = controladorEspacio;
        this.selected = 0;
    }

    public void show(Region region, int currentPlantaIndex) {
        if (region == null) return;
        boolean running = true;

        while (running) {
            List<Planta> plantas = region.obtenerPlantas();

            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 50;
            int boxH = Math.max(8, plantas.size() * 2 + 6);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Planta Manager");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "Plantas:");

            for (int i = 0; i < plantas.size(); i++) {
                int row = boxY + 3 + i;
                Planta planta = plantas.get(i);
                String line = String.format("%d. %s (Planta %d) - %d espacios",
                        i, planta.getNombre(), planta.getNumero(), planta.getEspacios().size());
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
                                case 'a': agregarPlanta(region); break;
                                case 'd': deletePlanta(region); break;
                                case 'r': renamePlanta(region); break;
                                case 'q': running = false; break;
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

    private void agregarPlanta(Region region) {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {"New Planta"};
        form.showForm("Add Planta", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;

        int maxNum = -1;
        for (Planta f : region.obtenerPlantas()) {
            if (f.getNumero() > maxNum) maxNum = f.getNumero();
        }

        Planta newPlanta = new Planta(
                "planta-" + UUID.randomUUID().toString().substring(0, 8),
                result[0].trim(),
                maxNum + 1,
                new ArrayList<>());
        region.agregarPlanta(newPlanta);
        saveRegion(region);
    }

    private void deletePlanta(Region region) {
        List<Planta> plantas = region.obtenerPlantas();
        if (plantas.isEmpty() || selected >= plantas.size()) return;
        Planta target = plantas.get(selected);
        if (!target.getEspacios().isEmpty()) {
            showMessage("Cannot eliminar: planta has espacios");
            return;
        }
        region.eliminarPlanta(target.getId());
        saveRegion(region);
        if (selected > 0) selected--;
    }

    private void renamePlanta(Region region) {
        List<Planta> plantas = region.obtenerPlantas();
        if (plantas.isEmpty() || selected >= plantas.size()) return;
        Planta target = plantas.get(selected);

        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {target.getNombre()};
        form.showForm("Rename Planta", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;

        target.setNombre(result[0].trim());
        saveRegion(region);
    }

    private void saveRegion(Region region) {
        Universidad universidad = controladorEspacio.obtenerUniversidad();
        List<Region> regiones = new ArrayList<>(universidad.getRegiones());
        for (int i = 0; i < regiones.size(); i++) {
            if (regiones.get(i).getId().equals(region.getId())) {
                regiones.set(i, region);
                break;
            }
        }
        universidad.setRegiones(regiones);
        controladorEspacio.guardarUniversidad(universidad);    }

    private void showMessage(String message) {
        TerminalSize size = screen.getTerminalSize();
        TextGraphics g = screen.newTextGraphics();
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

        int boxW = Math.max(message.length() + 6, 20);
        int boxH = 5;
        int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
        int boxY = Math.max(0, (size.getRows() - boxH) / 2);

        UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Attention");
        g.setForegroundColor(ColorScheme.SELECTED_FG);
        g.setBackgroundColor(ColorScheme.BG);
        g.putString(boxX + 3, boxY + 2, message);

        try { screen.refresh(); } catch (IOException e) { return; }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}