package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TextColor;

public final class ColorScheme {

    public static final TextColor BG = TextColor.ANSI.BLACK;
    public static final TextColor FG = TextColor.ANSI.WHITE;
    public static final TextColor FG_DIM = TextColor.Factory.fromString("#555555");
    public static final TextColor TITLE_BG = TextColor.ANSI.BLUE;
    public static final TextColor TITLE_FG = TextColor.ANSI.WHITE_BRIGHT;
    public static final TextColor CURSOR_FG = TextColor.ANSI.BLACK;
    public static final TextColor CURSOR_BG = TextColor.ANSI.WHITE_BRIGHT;
    public static final TextColor SELECTED_FG = TextColor.ANSI.YELLOW_BRIGHT;
    public static final TextColor SELECTED_BG = TextColor.Factory.fromString("#444400");
    public static final TextColor BORDER = TextColor.ANSI.WHITE_BRIGHT;
    public static final TextColor STATUS_BG = TextColor.ANSI.BLUE;
    public static final TextColor STATUS_FG = TextColor.ANSI.WHITE_BRIGHT;
    public static final TextColor RECORRIDO_FG = TextColor.ANSI.CYAN_BRIGHT;
    public static final TextColor RECORRIDO_BG = TextColor.Factory.fromString("#003333");

    private ColorScheme() {}

    public static TextColor fgForType(String tipo) {
        if (tipo == null) return FG;
        switch (tipo.toUpperCase()) {
            case "AULA":        return TextColor.ANSI.BLUE_BRIGHT;
            case "LABORATORIO": return TextColor.ANSI.CYAN_BRIGHT;
            case "BIBLIOTECA":  return TextColor.ANSI.GREEN_BRIGHT;
            case "CAFETERÍA":   return TextColor.ANSI.YELLOW_BRIGHT;
            case "CAFETERIA":   return TextColor.ANSI.YELLOW_BRIGHT;
            case "AUDITORIO":   return TextColor.ANSI.MAGENTA_BRIGHT;
            case "OFICINA":     return TextColor.ANSI.WHITE_BRIGHT;
            case "BAÑO":        return TextColor.ANSI.RED_BRIGHT;
            case "BANO":        return TextColor.ANSI.RED_BRIGHT;
            default:            return FG_DIM;
        }
    }

    public static TextColor bgForType(String tipo) {
        if (tipo == null) return BG;
        switch (tipo.toUpperCase()) {
            case "AULA":        return TextColor.Factory.fromString("#000033");
            case "LABORATORIO": return TextColor.Factory.fromString("#003333");
            case "BIBLIOTECA":  return TextColor.Factory.fromString("#003300");
            case "CAFETERÍA":   return TextColor.Factory.fromString("#333300");
            case "CAFETERIA":   return TextColor.Factory.fromString("#333300");
            case "AUDITORIO":   return TextColor.Factory.fromString("#330033");
            case "OFICINA":     return TextColor.Factory.fromString("#222222");
            case "BAÑO":        return TextColor.Factory.fromString("#330000");
            case "BANO":        return TextColor.Factory.fromString("#330000");
            default:            return TextColor.Factory.fromString("#1a1a1a");
        }
    }

    public static char charForType(String tipo) {
        if (tipo == null) return '?';
        switch (tipo.toUpperCase()) {
            case "AULA":        return 'A';
            case "LABORATORIO": return 'L';
            case "BIBLIOTECA":  return 'B';
            case "CAFETERÍA":   return 'C';
            case "CAFETERIA":   return 'C';
            case "AUDITORIO":   return 'D';
            case "OFICINA":     return 'O';
            case "BAÑO":        return 'N';
            case "BANO":        return 'N';
            default:            return '?';
        }
    }

    public static String labelForType(String tipo) {
        if (tipo == null) return "Desconocido";
        switch (tipo.toUpperCase()) {
            case "AULA":        return "Aula";
            case "LABORATORIO": return "Laboratorio";
            case "BIBLIOTECA":  return "Biblioteca";
            case "CAFETERÍA":   return "Cafetería";
            case "CAFETERIA":   return "Cafetería";
            case "AUDITORIO":   return "Auditorio";
            case "OFICINA":     return "Oficina";
            case "BAÑO":        return "Baño";
            case "BANO":        return "Baño";
            default:            return "Otro";
        }
    }
}