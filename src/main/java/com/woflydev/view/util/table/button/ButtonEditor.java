package com.woflydev.view.util.table.button;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private final JPanel panel;
    private final JButton[] buttons;
    private final Runnable[] actions;

    public ButtonEditor(JCheckBox checkBox, String[] labels, Runnable[] actions) {
        panel = new JPanel(new GridBagLayout());
        this.buttons = new JButton[labels.length];
        this.actions = actions;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);

        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new JButton(labels[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 12));
            buttons[i].setMargin(new Insets(2, 2, 2, 2));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            gbc.gridy = i;
            panel.add(buttons[i], gbc);
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            if (e.getSource() == buttons[i]) {
                actions[i].run();
                stopCellEditing();
            }
        }
    }
}
