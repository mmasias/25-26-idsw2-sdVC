package com.myuniverse.views.terminal;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.models.Universidad;
import com.myuniverse.repositories.AdministradorRepository;
import com.myuniverse.repositories.EspacioRepository;
import com.myuniverse.repositories.RecorridoRepository;
import com.myuniverse.services.AuthService;
import com.myuniverse.services.GestionEspacioService;
import com.myuniverse.services.RecorridoService;

import java.io.IOException;

public class TerminalApp {
    private final boolean adminMode;

    public TerminalApp(boolean adminMode) {
        this.adminMode = adminMode;
    }

    public void iniciar() {
        EspacioRepository spaceRepository = new EspacioRepository();
        RecorridoRepository routeRepository = new RecorridoRepository();
        AdministradorRepository repositorioAdministrador = new AdministradorRepository();

        GestionEspacioService servicioEspacio = new GestionEspacioService(spaceRepository, routeRepository);
        RecorridoService servicioRecorrido = new RecorridoService(routeRepository);
        AuthService servicioAutenticacion = new AuthService(repositorioAdministrador);

        AuthController controladorAutenticacion = new AuthController(servicioAutenticacion);
        EspacioController controladorEspacio = new EspacioController(servicioEspacio);
        RecorridoController controladorRecorrido = new RecorridoController(servicioRecorrido);

        String uniName = "myUniverse";
        Universidad uni = controladorEspacio.obtenerUniversidad();
        if (uni != null && uni.getNombre() != null && !uni.getNombre().isBlank()) {
            uniName = uni.getNombre();
        }

        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setTerminalEmulatorTitle(uniName);
        Terminal terminal = null;
        TerminalScreen screen = null;

        try {
            terminal = factory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            MapaView mapView = new MapaView(screen, adminMode, controladorEspacio, controladorAutenticacion);
            mapView.setRecorridoController(controladorRecorrido);

            if (adminMode) {
                LoginDialog loginDialog = new LoginDialog(screen, controladorAutenticacion);
                boolean authenticated = loginDialog.show();
                if (!authenticated) {
                    screen.stopScreen();
                    return;
                }
            }

            mapView.ejecutar();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (screen != null) {
                try {
                    screen.stopScreen();
                } catch (IOException ex) {
                    // ignore
                }
            }
        }
    }
}