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
    public static final TextColor ROUTE_FG = TextColor.ANSI.CYAN_BRIGHT;
    public static final TextColor ROUTE_BG = TextColor.Factory.fromString("#003333");

    private ColorScheme() {
    }
}