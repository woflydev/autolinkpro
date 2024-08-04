package com.woflydev.view.util.table;

import javax.swing.table.DefaultTableModel;

/**
 * Normally, setting the table to non-editable would suffice, but
 * since the application uses buttons, we need to make exceptions.
 * <p>
 * This model makes it so that only the last column (the Actions column) is editable.
 * This is useful when constructing tables that utilize {@link com.woflydev.view.util.table.button.ButtonRenderer} and {@link com.woflydev.view.util.table.button.ButtonEditor}.
 * @author woflydev
 */
public class NonEditableTableModel extends DefaultTableModel {
    private final int editableColumnIndex;

    public NonEditableTableModel(String[] columnNames, int rowCount) {
        super(columnNames, rowCount);
        this.editableColumnIndex = columnNames.length - 1;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == editableColumnIndex;
    }
}
