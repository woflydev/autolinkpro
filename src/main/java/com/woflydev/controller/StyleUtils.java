package com.woflydev.controller;

import javax.swing.*;

/**
 * A collection of utility methods for applying styles to components.
 * Integrates directly with FlatLaf's CSS class support.
 * See <a href="https://github.com/JFormDesigner/FlatLaf">FlatLaf Documentation</a> for detailed information.
 * @author woflydev
 */
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
