package com.myuniverse;

import com.myuniverse.views.terminal.TerminalApp;

public class Main {
    public static void main(String[] args) {
        boolean adminMode = false;
        for (String arg : args) {
            if ("--admin".equals(arg))
                adminMode = true;
        }
        TerminalApp app = new TerminalApp(adminMode);
        app.iniciar();
    }
}