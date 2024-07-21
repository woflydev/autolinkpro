package com.woflydev.controller;

import javax.swing.*;

public class StyleUtils {
    public enum BUTTON_STYLES {
        PRIMARY,
        SECONDARY,
        WARNING
    }

    public static void applyButtonStyle(JComponent c, BUTTON_STYLES style) {
        String cssClass;
        switch (style) {
            case PRIMARY -> cssClass = "primary";
            case SECONDARY -> cssClass = " secondary";
            case WARNING -> cssClass = "warning";
            default -> cssClass = "secondary";
        }

        c.putClientProperty("FlatLaf.styleClass", cssClass);
    }
}
