package com.woflydev.view.util.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton[] buttons;

    public ButtonRenderer(String[] labels) {
        setLayout(new GridBagLayout());
        setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);

        buttons = new JButton[labels.length];
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new JButton(labels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 12));
            buttons[i].setMargin(new Insets(2, 2, 2, 2));
            buttons[i].setFocusPainted(false);
            gbc.gridy = i;
            add(buttons[i], gbc);
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        return this;
    }
}
