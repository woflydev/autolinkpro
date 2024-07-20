package com.woflydev.controller;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WindowUtils
{
    private static final ArrayList<JFrame> openWindows = new ArrayList<>();

    public static void infoBox(String infoMessage) {
        JOptionPane.showMessageDialog(
                null,
                infoMessage,
                "Info",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void infoBox(JScrollPane pane, String title) {
        JOptionPane.showMessageDialog(
                null,
                pane,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void errorBox(String errorMessage) {
        JOptionPane.showMessageDialog(
                null,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static int confirmationBox(String confirmMessage) {
        return JOptionPane.showConfirmDialog(
                null,
                confirmMessage,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );
    }

    public static void applyWindowSettings(JFrame f, String title, Dimension d, LayoutManager lm, int disposeOp) {
        f.setDefaultCloseOperation(disposeOp);
        f.setSize(d);
        f.setResizable(false);
        f.setTitle(title);
        f.setLocationRelativeTo(null);
        f.setLayout(lm);
    }

    public static void refreshPanel(JPanel panel) {
        panel.revalidate();
        panel.repaint();
    }

    public static void closeAllWindows() {
        for (JFrame window : openWindows) {
            window.dispose();
        }
        openWindows.clear();
    }

    public static void register(JFrame frame) {
        if (frame == null) { System.out.println("WARNING! Tried to register a null frame."); return; }
        if (!openWindows.contains(frame)) {
            frame.setVisible(true);
            openWindows.add(frame);
        } else {
            System.out.println("WARNING! Window is thought to be opened, or you are calling the open method multiple times.");
        }
    }
}