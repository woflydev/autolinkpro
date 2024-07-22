package com.woflydev.view.util.swing;

import raven.datetime.component.date.DateEvent;
import raven.datetime.component.date.DatePicker;
import raven.datetime.component.date.DateSelectionListener;

import javax.swing.*;
import java.time.LocalDate;

/**
 * Custom wrapper for DJ-Raven's DatePicker.
 * Offers much needed quality-of-life improvements, such as the ability to actually get the date.
 * @author woflydev
 */
public class CustomDatePicker extends DatePicker {
    private LocalDate currentSelectedDate;

    /**
     * Creates a JFormattedTextField, attaches the DatePicker, and applies DatePicker defaults.
     * <p>
     * For SOME REASON, the method getSelectedDate/getSelectedTime only fires when a selection is made, and
     * then just drops the value. And so I had to wrap it with getDate/getTime to store persistently.
     * @return JFormattedTextField on which the DatePicker has been attached.
     */
    public JFormattedTextField createParent() {
        JFormattedTextField ftf = new JFormattedTextField();

        setEditor(ftf);

        setSelectedDate(LocalDate.now());
        setDateSelectionMode(DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
        setDateSelectionAble(localDate -> !localDate.isBefore(LocalDate.now()));

        // WHY DOESN'T THE METHOD DO WHAT IT SAYS IT DOES
        addDateSelectionListener(dateEvent -> currentSelectedDate = getSelectedDate());

        return ftf;
    }

    public LocalDate getDate() { return currentSelectedDate; }
    public void setDate(LocalDate date) { currentSelectedDate = date; setSelectedDate(date); }
}
