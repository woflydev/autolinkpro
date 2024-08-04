package com.woflydev.view.util.table.button;

import com.woflydev.controller.StyleUtils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static com.woflydev.controller.StyleUtils.BUTTON_STYLES.*;

/**
 * Custom table cell renderer designed for extensible buttons.
 * @author woflydev
 */
public class ButtonRenderer extends JPanel implements TableCellRenderer {
    private final JButton[] buttons;

    /**
     * Takes an array of labels to display on the buttons.
     * This class should be used with the {@link ButtonEditor} for interactivity.
     * @param labels
     */
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

            // apply different classes depending on button
            if (labels[i].toLowerCase().equals("delete"))
                StyleUtils.applyButtonStyle(buttons[i], WARNING);
            else
                StyleUtils.applyButtonStyle(buttons[i], SECONDARY);

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
