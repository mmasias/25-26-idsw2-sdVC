package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.models.Region;
import com.myuniverse.models.Universidad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigDialog {
    private final TerminalScreen screen;
    private final EspacioController controladorEspacio;
    private int selected;

    public ConfigDialog(TerminalScreen screen, EspacioController controladorEspacio) {
        this.screen = screen;
        this.controladorEspacio = controladorEspacio;
        this.selected = 0;
    }

    public void show() {
        boolean running = true;

        while (running) {
            Universidad universidad = controladorEspacio.obtenerUniversidad();
            Region currentRegion = null;
            List<Region> regiones = new ArrayList<>(universidad.getRegiones());
            if (!regiones.isEmpty() && selected < regiones.size()) {
                currentRegion = regiones.get(selected);
            }

            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 60;
            int boxH = Math.max(10, regiones.size() * 2 + 10);
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Config");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            String uniName = universidad.getNombre() != null ? universidad.getNombre() : "(sin nombre)";
            g.putString(boxX + 2, boxY + 2, "Universidad:");
            g.setForegroundColor(ColorScheme.SELECTED_FG);
            g.putString(boxX + 2, boxY + 3, String.format("%-56s", "  " + uniName));

            g.setForegroundColor(ColorScheme.FG);
            g.putString(boxX + 2, boxY + 5, "Regiones:");

            for (int i = 0; i < regiones.size(); i++) {
                int row = boxY + 6 + i * 2;
                if (i == selected) {
                    g.setForegroundColor(ColorScheme.CURSOR_FG);
                    g.setBackgroundColor(ColorScheme.CURSOR_BG);
                } else {
                    g.setForegroundColor(ColorScheme.FG);
                    g.setBackgroundColor(ColorScheme.BG);
                }
                String line = String.format("%d. %s", i, regiones.get(i).getNombre());
                g.putString(boxX + 2, row, String.format("%-56s", line));
                g.setForegroundColor(ColorScheme.FG);
                g.setBackgroundColor(ColorScheme.BG);
                g.putString(boxX + 2, row + 1, String.format("%-56s", "   Plantas: " + regiones.get(i).obtenerPlantas().size()));
            }

            int hintY = boxY + boxH - 3;
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, hintY, "Up/Dn:Select  U:Rename Uni  R:Rename Reg");
            g.putString(boxX + 2, hintY + 1, "A:Add Region  D:Delete Region  Q/Enter:Close");

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
                        if (selected < regiones.size() - 1) selected++;
                        break;
                    case Character:
                        if (key.getCharacter() != null) {
                            char ch = Character.toLowerCase(key.getCharacter());
                            switch (ch) {
                                case 'u':
                                    renameUniversidad(universidad);
                                    break;
                                case 'r':
                                    if (currentRegion != null) renameRegion(currentRegion);
                                    break;
                                case 'a':
                                    agregarRegion();
                                    break;
                                case 'd':
                                    if (currentRegion != null) deleteRegion(currentRegion);
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

    private void renameUniversidad(Universidad universidad) {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {universidad.getNombre() != null ? universidad.getNombre() : ""};
        form.showForm("Rename Universidad", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;
        controladorEspacio.actualizarNombreUniversidad(result[0].trim());
    }

    private void renameRegion(Region region) {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {region.getNombre()};
        form.showForm("Rename Region", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;
        controladorEspacio.actualizarNombreRegion(region.getId(), result[0].trim());
    }

    private void agregarRegion() {
        DialogForm form = new DialogForm(screen);
        String[] labels = {"Name"};
        String[] defaults = {""};
        form.showForm("Add Region", labels, defaults, null, -1);
        String[] result = form.getValues();
        if (result == null || result[0].trim().isEmpty()) return;
        controladorEspacio.agregarRegion(result[0].trim());
    }

    private void deleteRegion(Region region) {
        Universidad universidad = controladorEspacio.obtenerUniversidad();
        if (universidad.getRegiones().size() <= 1) {
            showErrorMessage("Cannot delete: must have at least one region");
            return;
        }
        universidad.removeRegion(region.getId());
        controladorEspacio.guardarUniversidad(universidad);
        if (selected > 0) selected--;
    }

    private void showErrorMessage(String message) {
        TerminalSize size = screen.getTerminalSize();
        TextGraphics g = screen.newTextGraphics();
        g.fillRectangle(new TerminalPosition(0, 0), size, ' ');
        int boxW = Math.max(message.length() + 6, 20);
        int boxH = 5;
        int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
        int boxY = Math.max(0, (size.getRows() - boxH) / 2);
        UIUtils.drawBox(g, boxX, boxY, boxW, boxH, "Error");
        g.setForegroundColor(ColorScheme.SELECTED_FG);
        g.setBackgroundColor(ColorScheme.BG);
        g.putString(boxX + 3, boxY + 2, message);
        try { screen.refresh(); } catch (IOException e) { return; }
        try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}