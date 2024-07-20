package com.woflydev.view.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private Runnable onButtonClick;

    public ButtonEditor(JCheckBox checkBox, String buttonText, Runnable onButtonClick) {
        super(checkBox);
        button = new JButton(buttonText);
        button.setOpaque(true);
        this.onButtonClick = onButtonClick;

        button.addActionListener(e -> {
            if (onButtonClick != null) {
                onButtonClick.run();
            }
            fireEditingStopped(); // Ensure editing stops after action
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Ensure the onButtonClick action knows the row index
        button.setActionCommand(String.valueOf(row));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return ""; // Return appropriate value if needed
    }
}
