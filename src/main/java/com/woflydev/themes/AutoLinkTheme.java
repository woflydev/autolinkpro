package com.woflydev.themes;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * Custom theme inspired by macOS Dark Mode, specifically made for AutoLinkPro.
 * @author woflydev
 */
public class AutoLinkTheme extends FlatDarkLaf {
    public static boolean setup() {
        return setup(new AutoLinkTheme());
    }

    @Override
    public String getName() {
        return "Autolink Theme";
    }
}
