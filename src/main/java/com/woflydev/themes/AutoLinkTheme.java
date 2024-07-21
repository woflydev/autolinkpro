package com.woflydev.themes;

import com.formdev.flatlaf.FlatDarkLaf;

public class AutoLinkTheme extends FlatDarkLaf {
    public static boolean setup() {
        return setup(new AutoLinkTheme());
    }

    @Override
    public String getName() {
        return "Autolink Theme";
    }
}
