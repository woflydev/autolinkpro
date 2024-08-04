package com.woflydev.view.util.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom formatting for putting date-times into table cells.
 * @author woflydev
 */
public class DateTimeCellRenderer extends JLabel implements TableCellRenderer {

    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public DateTimeCellRenderer() {
        dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row, int column
    ) {
        if (value instanceof LocalDateTime dateTime) {
            String date = dateTime.format(dateFormatter);
            String time = dateTime.format(timeFormatter);
            setText(String.format("<html>Date: <b>%s</b><br>Time: <b>%s</b></html>", date, time));
        } else setText(value.toString());
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        return this;
    }
}
