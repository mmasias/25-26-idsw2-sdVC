package com.myuniverse.views.terminal;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.models.Sesion;

import java.io.IOException;

public class LoginDialog {
    private final TerminalScreen screen;
    private final AuthController authCtrl;
    private String user;
    private String pass;

    public LoginDialog(TerminalScreen screen, AuthController authCtrl) {
        this.screen = screen;
        this.authCtrl = authCtrl;
    }

    public boolean show() throws IOException {
        user = "";
        pass = "";
        int field = 0;
        boolean running = true;
        boolean authenticated = false;

        while (running) {
            TerminalSize size = screen.getTerminalSize();
            TextGraphics g = screen.newTextGraphics();
            g.fillRectangle(new TerminalPosition(0, 0), size, ' ');

            int centerX = size.getColumns() / 2;
            int centerY = size.getRows() / 2;
            int boxW = 40;
            int boxH = 8;
            int boxX = centerX - boxW / 2;
            int boxY = centerY - boxH / 2;

            drawBox(g, boxX, boxY, boxW, boxH, "Login");

            g.setForegroundColor(ColorScheme.FG);
            g.setBackgroundColor(ColorScheme.BG);
            g.putString(boxX + 2, boxY + 2, "User:");
            g.setForegroundColor(ColorScheme.SELECTED_FG);
            String userDisplay = user;
            if (field == 0) userDisplay += "█";
            g.putString(boxX + 2, boxY + 3, String.format("%-36s", userDisplay));

            g.setForegroundColor(ColorScheme.FG);
            g.putString(boxX + 2, boxY + 4, "Password:");
            g.setForegroundColor(ColorScheme.SELECTED_FG);
            String passDisplay = repeat('*', pass.length());
            if (field == 1) passDisplay += "█";
            g.putString(boxX + 2, boxY + 5, String.format("%-36s", passDisplay));

            if (field == 0) {
                screen.setCursorPosition(new TerminalPosition(boxX + 2 + user.length(), boxY + 3));
            } else {
                screen.setCursorPosition(new TerminalPosition(boxX + 2 + pass.length(), boxY + 5));
            }

            screen.refresh();

            KeyStroke key = screen.readInput();
            if (key == null) continue;

            switch (key.getKeyType()) {
                case Tab:
                    field = (field + 1) % 2;
                    break;
                case Enter:
                    if (field == 0) {
                        field = 1;
                    } else {
                        Sesion sesion = authCtrl.autenticar(user, pass);
                        if (sesion != null) {
                            authenticated = true;
                            running = false;
                        } else {
                            pass = "";
                        }
                    }
                    break;
                case Escape:
                    running = false;
                    break;
                case Backspace:
                    if (field == 0 && user.length() > 0) user = user.substring(0, user.length() - 1);
                    if (field == 1 && pass.length() > 0) pass = pass.substring(0, pass.length() - 1);
                    break;
                case Character:
                    if (key.getCharacter() != null) {
                        if (field == 0) user += key.getCharacter();
                        else pass += key.getCharacter();
                    }
                    break;
                default:
                    break;
            }
        }
        screen.setCursorPosition(null);
        return authenticated;
    }

    private void drawBox(TextGraphics g, int x, int y, int w, int h, String title) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String top = "┌" + repeat('─', w - 2) + "┐";
        String bot = "└" + repeat('─', w - 2) + "┘";
        g.putString(x, y, top);
        for (int i = 1; i < h - 1; i++) {
            g.putString(x, y + i, "│" + String.format("%" + (w - 2) + "s", "") + "│");
        }
        g.putString(x, y + h - 1, bot);
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.putString(x + 2, y, " " + title + " ");
    }

    private String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }
}