package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;

public class DialogForm {
    private final TerminalScreen screen;
    private String[] values;
    private boolean confirmed;

    public DialogForm(TerminalScreen screen) {
        this.screen = screen;
        this.confirmed = false;
    }

    public String[] getValues() {
        return confirmed ? values : null;
    }

    public void showForm(String title, String[] labels, String[] defaults, String[] comboBoxOptions, int comboBoxFieldIndex) {
        values = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            values[i] = defaults[i] != null ? defaults[i] : "";
        }
        confirmed = false;
        int field = 0;
        boolean running = true;

        while (running) {
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int boxW = 50;
            int boxH = labels.length * 2 + 4;
            int boxX = Math.max(0, (size.getColumns() - boxW) / 2);
            int boxY = Math.max(0, (size.getRows() - boxH) / 2);

            drawBox(g, boxX, boxY, boxW, boxH, title);

            for (int i = 0; i < labels.length; i++) {
                int labelY = boxY + 2 + i * 2;
                g.setForegroundColor(ColorScheme.FG);
                g.setBackgroundColor(ColorScheme.BG);
                g.putString(boxX + 2, labelY, labels[i] + ":");

                if (i == comboBoxFieldIndex) {
                    String current = values[i];
                    int optIdx = findOptionIndex(comboBoxOptions, current);
                    String display = "[" + ColorScheme.labelForType(current) + "] ←→";
                    if (field == i) {
                        g.setForegroundColor(ColorScheme.CURSOR_FG);
                        g.setBackgroundColor(ColorScheme.CURSOR_BG);
                    } else {
                        g.setForegroundColor(ColorScheme.SELECTED_FG);
                        g.setBackgroundColor(ColorScheme.BG);
                    }
                    g.putString(boxX + 2, labelY + 1, String.format("%-46s", display));
                } else {
                    String display = values[i];
                    if (field == i) display += "█";
                    if (field == i) {
                        g.setForegroundColor(ColorScheme.CURSOR_FG);
                        g.setBackgroundColor(ColorScheme.CURSOR_BG);
                    } else {
                        g.setForegroundColor(ColorScheme.FG);
                        g.setBackgroundColor(ColorScheme.BG);
                    }
                    g.putString(boxX + 2, labelY + 1, String.format("%-46s", display));
                }
            }

            int hintY = boxY + boxH - 2;
            g.setForegroundColor(ColorScheme.FG_DIM);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, hintY, "Tab:Next  Enter:OK  Esc:Cancel");

            if (field < labels.length) {
                int cursorY = boxY + 2 + field * 2 + 1;
                int cursorX = boxX + 2 + values[field].length();
                if (field != comboBoxFieldIndex && cursorX < boxX + boxW - 2) {
                    screen.setCursorPosition(new TerminalPosition(cursorX, cursorY));
                } else {
                    screen.setCursorPosition(null);
                }
            } else {
                screen.setCursorPosition(null);
            }

            try { screen.refresh(); } catch (IOException e) { return; }

            try {
                KeyStroke key = screen.readInput();
                if (key == null) continue;

                if (field == comboBoxFieldIndex) {
                    switch (key.getKeyType()) {
                        case ArrowRight:
                            int idx = findOptionIndex(comboBoxOptions, values[field]);
                            values[field] = comboBoxOptions[(idx + 1) % comboBoxOptions.length];
                            break;
                        case ArrowLeft:
                            int idx2 = findOptionIndex(comboBoxOptions, values[field]);
                            values[field] = comboBoxOptions[(idx2 - 1 + comboBoxOptions.length) % comboBoxOptions.length];
                            break;
                        case Tab:
                            field = (field + 1) % labels.length;
                            break;
                        case Enter:
                            confirmed = true;
                            running = false;
                            break;
                        case Escape:
                            running = false;
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (key.getKeyType()) {
                        case Tab:
                            field = (field + 1) % labels.length;
                            break;
                        case Enter:
                            confirmed = true;
                            running = false;
                            break;
                        case Escape:
                            running = false;
                            break;
                        case Backspace:
                            if (values[field].length() > 0) {
                                values[field] = values[field].substring(0, values[field].length() - 1);
                            }
                            break;
                        case Character:
                            if (key.getCharacter() != null && values[field].length() < 40) {
                                values[field] += key.getCharacter();
                            }
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                running = false;
            }
        }
        screen.setCursorPosition(null);
    }

    private int findOptionIndex(String[] options, String value) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equalsIgnoreCase(value)) return i;
        }
        return 0;
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