package com.myuniverse.views.terminal;

import com.googlecode.lanterna.input.KeyStroke;

public interface MapaModoEstado {
    String obtenerEtiquetaModo();
    String construirBarraEstado();
    void manejarEntrada(KeyStroke key, com.googlecode.lanterna.TerminalSize size);
}