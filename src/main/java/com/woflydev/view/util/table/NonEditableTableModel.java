package com.woflydev.view.util.table;

import javax.swing.table.DefaultTableModel;

public class NonEditableTableModel extends DefaultTableModel {
    private final int editableColumnIndex; // Column index where editing is allowed (e.g., Actions column)

    public NonEditableTableModel(String[] columnNames, int rowCount) {
        super(columnNames, rowCount);
        this.editableColumnIndex = columnNames.length - 1;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == editableColumnIndex;
    }
}
