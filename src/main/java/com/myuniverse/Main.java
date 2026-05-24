package com.myuniverse;

import com.myuniverse.repositories.AdministradorRepository;
import com.myuniverse.repositories.EspacioRepository;
import com.myuniverse.repositories.RecorridoRepository;
import com.myuniverse.views.terminal.TerminalApp;

public class Main {
    public static void main(String[] args) {
        boolean modoAdmin = false;
        for (String arg : args) {
            if ("--admin".equals(arg)) modoAdmin = true;
        }
        TerminalApp app = new TerminalApp(modoAdmin);
        app.start();
    }
}