package com.woflydev;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatInspector;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import com.woflydev.themes.AutoLinkTheme;
import com.woflydev.view.LoginWindow;

public class Main {
    public static void main(String[] args) {
        System.out.println(
                """
                         __          __  _                            _                        _        _ _       _      _____          \s
                         \\ \\        / / | |                          | |            /\\        | |      | (_)     | |    |  __ \\         \s
                          \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___  | |_ ___      /  \\  _   _| |_ ___ | |_ _ __ | | __ | |__) | __ ___ \s
                           \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\    / /\\ \\| | | | __/ _ \\| | | '_ \\| |/ / |  ___/ '__/ _ \\\s
                            \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) |  / ____ \\ |_| | || (_) | | | | | |   <  | |   | | | (_) |
                             \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/  /_/    \\_\\__,_|\\__\\___/|_|_|_| |_|_|\\_\\ |_|   |_|  \\___/\s
                                                                                                                                        \s
                                                                                                                                        \s"""
        );

        FlatInspector.install("ctrl shift I");
        FlatLaf.registerCustomDefaultsSource("themes");
        AutoLinkTheme.setup();
        LoginWindow.open();
    }
}