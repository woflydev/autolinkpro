package com.woflydev.view.util.table;

import javax.swing.*;
import javax.swing.table.TableModel;

/**
 * A custom wrapper for the JTable class, automatically applying the BorderlessTableRenderer for a seamless
 * user experience.
 */
public class CustomJTable extends JTable {
    public CustomJTable(TableModel tableModel) {
        super(tableModel);

        setDefaultRenderer(Object.class, new BorderlessTableRenderer());
        setCellSelectionEnabled(false);
        setFillsViewportHeight(true);
    }
}
