package com.woflydev.view.util.swing;

import raven.datetime.component.time.TimePicker;

import javax.swing.*;
import java.time.LocalTime;

import static javax.swing.SwingConstants.HORIZONTAL;

public class CustomTimePicker extends TimePicker {
    /**
     * Creates a JFormattedTextField, attaches the TimePicker, and applies TimePicker defaults.
     * @return A JFormattedTextField on which the TimePicker has been attached.
     */
    public JFormattedTextField create() {
        LocalTime defaultTime = LocalTime.now().withSecond(0).withNano(0).plusMinutes(65);

        TimePicker tp = new TimePicker();
        JFormattedTextField ftf = new JFormattedTextField();
        tp.setEditor(ftf);

        tp.setOrientation(HORIZONTAL);
        tp.setSelectedTime(defaultTime);
        tp.set24HourView(true);

        return ftf;
    }

    public LocalTime getTime() {
        return getSelectedTime();
    }
}
