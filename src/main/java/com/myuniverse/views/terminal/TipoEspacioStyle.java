package com.myuniverse.views.terminal;

import com.googlecode.lanterna.TextColor;
import com.myuniverse.models.TipoEspacio;

import java.util.EnumMap;

public final class TipoEspacioStyle {

    private static final EnumMap<TipoEspacio, TextColor> FOREGROUND_MAP = new EnumMap<>(TipoEspacio.class);
    private static final EnumMap<TipoEspacio, TextColor> BACKGROUND_MAP = new EnumMap<>(TipoEspacio.class);

    static {
        FOREGROUND_MAP.put(TipoEspacio.CLASSROOM, TextColor.ANSI.BLUE_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.LABORATORY, TextColor.ANSI.CYAN_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.LIBRARY, TextColor.ANSI.GREEN_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.CAFETERIA, TextColor.ANSI.YELLOW_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.AUDITORIUM, TextColor.ANSI.MAGENTA_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.OFFICE, TextColor.ANSI.WHITE_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.RESTROOM, TextColor.ANSI.RED_BRIGHT);
        FOREGROUND_MAP.put(TipoEspacio.OTHER, ColorScheme.FG_DIM);

        BACKGROUND_MAP.put(TipoEspacio.CLASSROOM, TextColor.Factory.fromString("#000033"));
        BACKGROUND_MAP.put(TipoEspacio.LABORATORY, TextColor.Factory.fromString("#003333"));
        BACKGROUND_MAP.put(TipoEspacio.LIBRARY, TextColor.Factory.fromString("#003300"));
        BACKGROUND_MAP.put(TipoEspacio.CAFETERIA, TextColor.Factory.fromString("#333300"));
        BACKGROUND_MAP.put(TipoEspacio.AUDITORIUM, TextColor.Factory.fromString("#330033"));
        BACKGROUND_MAP.put(TipoEspacio.OFFICE, TextColor.Factory.fromString("#222222"));
        BACKGROUND_MAP.put(TipoEspacio.RESTROOM, TextColor.Factory.fromString("#330000"));
        BACKGROUND_MAP.put(TipoEspacio.OTHER, TextColor.Factory.fromString("#1a1a1a"));
    }

    private TipoEspacioStyle() {
    }

    public static TextColor foregroundFor(TipoEspacio tipo) {
        if (tipo == null) return ColorScheme.FG;
        return FOREGROUND_MAP.getOrDefault(tipo, ColorScheme.FG_DIM);
    }

    public static TextColor backgroundFor(TipoEspacio tipo) {
        if (tipo == null) return ColorScheme.BG;
        return BACKGROUND_MAP.getOrDefault(tipo, TextColor.Factory.fromString("#1a1a1a"));
    }

    public static char symbolFor(TipoEspacio tipo) {
        if (tipo == null) return '?';
        return tipo.getSymbol();
    }

    public static String labelFor(TipoEspacio tipo) {
        if (tipo == null) return "Unknown";
        return tipo.getDisplayName();
    }

    public static TipoEspacio[] allTypes() {
        return TipoEspacio.values();
    }
}