package com.myuniverse.views.terminal;

import com.myuniverse.models.Planta;
import com.myuniverse.models.Espacio;
import com.googlecode.lanterna.TextColor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IMapRenderer {
    int[] computeGridSize(List<Espacio> espacios);

    Espacio spaceAt(List<Espacio> espacios, int x, int y);

    LinkedHashMap<String, Espacio> spacesAdyacentes(List<Espacio> espacios, Espacio actual);

    boolean tieneSolapamiento(List<Espacio> espacios, int x, int y, int ancho, int alto, String excludeId);

    void renderizar(TextGraphicsAdapter g, Planta planta, List<Espacio> espacios,
                GridCursor cursor, Espacio selectedEspacio,
                int offsetX, int offsetY, int viewportWidth, int viewportHeight,
                Map<String, Integer> routeOverlay, Integer routeStep);

    void renderNameOverlay(TextGraphicsAdapter g, List<Espacio> espacios,
                           GridCursor cursor, Espacio selectedEspacio,
                           int offsetX, int offsetY, int viewportWidth, int viewportHeight,
                           Map<String, Integer> routeOverlay);

    interface TextGraphicsAdapter {
        void drawChar(int x, int y, char ch, TextColor fg, TextColor bg);
    }
}