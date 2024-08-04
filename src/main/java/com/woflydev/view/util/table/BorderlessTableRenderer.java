package com.woflydev.view.util.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;

/**
 * Custom TableCellRenderer to remove the annoying border outline when selecting a table's cell.
 */
public class BorderlessTableRenderer extends DefaultTableCellRenderer {
    @Serial
    private static final long serialVersionUID = 1L;

    public Component getTableCellRendererComponent(
            final JTable table,
            final Object value,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int col) {

        return super.getTableCellRendererComponent(
                table,
                value,
                isSelected,
                false,
                row,
                col
        );
    }
}