package com.woflydev.view.util.component;

import raven.datetime.component.date.DatePicker;

import javax.swing.*;
import java.time.LocalDate;

public class CustomDatePicker extends DatePicker {
    /**
     * Creates a JFormattedTextField, attaches the DatePicker, and applies DatePicker defaults.
     * @return JFormattedTextField on which the DatePicker has been attached.
     */
    public JFormattedTextField create() {
        DatePicker dp = new DatePicker();
        JFormattedTextField ftf = new JFormattedTextField();
        dp.setEditor(ftf);

        dp.setSelectedDate(LocalDate.now());
        dp.setDateSelectionMode(DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
        dp.setDateSelectionAble(localDate -> !localDate.isBefore(LocalDate.now()));

        return ftf;
    }

    public LocalDate getDate() {
        return getSelectedDate();
    }
}
