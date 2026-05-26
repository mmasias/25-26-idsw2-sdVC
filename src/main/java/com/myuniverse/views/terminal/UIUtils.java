package com.myuniverse.views.terminal;

import com.googlecode.lanterna.graphics.TextGraphics;

public final class UIUtils {

    private UIUtils() {
    }

    public static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen);
    }

    public static void drawBox(TextGraphics g, int x, int y, int w, int h, String title) {
        g.setForegroundColor(ColorScheme.BORDER);
        g.setBackgroundColor(ColorScheme.BG);
        String top = "┌" + repeat('─', Math.max(0, w - 2)) + "┐";
        String bot = "└" + repeat('─', Math.max(0, w - 2)) + "┘";
        g.putString(x, y, top);
        for (int i = 1; i < h - 1; i++) {
            g.putString(x, y + i, "│" + String.format("%" + (w - 2) + "s", "") + "│");
        }
        g.putString(x, y + h - 1, bot);
        g.setForegroundColor(ColorScheme.TITLE_FG);
        g.putString(x + 2, y, " " + title + " ");
    }

    public static char digitChar(int step) {
        if (step >= 1 && step <= 9) return (char) ('0' + step);
        if (step > 9) return '#';
        return '•';
    }
}