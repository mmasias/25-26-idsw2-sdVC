package com.myuniverse.views.terminal;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.myuniverse.controllers.AuthController;
import com.myuniverse.controllers.EspacioController;
import com.myuniverse.controllers.RecorridoController;
import com.myuniverse.controllers.VisitaController;
import com.myuniverse.repositories.AdministradorRepository;
import com.myuniverse.repositories.EspacioRepository;
import com.myuniverse.repositories.RecorridoRepository;
import com.myuniverse.services.AuthService;
import com.myuniverse.services.GestionEspacioService;
import com.myuniverse.services.VisitaService;

import java.io.IOException;

public class TerminalApp {
    private final boolean adminMode;

    public TerminalApp(boolean adminMode) {
        this.adminMode = adminMode;
    }

    public void start() {
        DefaultTerminalFactory factory = new DefaultTerminalFactory()
                .setTerminalEmulatorTitle("myUniverse");
        Terminal terminal = null;
        TerminalScreen screen = null;

        try {
            terminal = factory.createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();

            EspacioRepository espacioRepo = new EspacioRepository();
            RecorridoRepository recorridoRepo = new RecorridoRepository();
            AdministradorRepository adminRepo = new AdministradorRepository();

            AuthService authService = new AuthService(adminRepo);
            GestionEspacioService gestionService = new GestionEspacioService(espacioRepo, recorridoRepo);
            VisitaService visitaService = new VisitaService(espacioRepo, recorridoRepo);

            AuthController authCtrl = new AuthController(authService);
            EspacioController espacioCtrl = new EspacioController(gestionService, visitaService);
            RecorridoController recorridoCtrl = new RecorridoController(gestionService, visitaService);
            VisitaController visitaCtrl = new VisitaController(visitaService);

            MapaView mapaView = new MapaView(screen, adminMode, espacioCtrl, authCtrl, gestionService);
            mapaView.setRecorridoController(recorridoCtrl);

            if (adminMode) {
                LoginDialog loginDialog = new LoginDialog(screen, authCtrl);
                boolean authenticated = loginDialog.show();
                if (!authenticated) {
                    screen.stopScreen();
                    return;
                }
            }

            mapaView.run();

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