package com.woflydev.view.util.swing;

import raven.datetime.component.time.TimePicker;

import javax.swing.*;
import java.time.LocalTime;

import static javax.swing.SwingConstants.HORIZONTAL;

/**
 * Custom wrapper for DJ-Raven's TimePicker.
 * Offers much needed quality-of-life improvements, such as the ability to actually get the time.
 * @author woflydev
 */
public class CustomTimePicker extends TimePicker {
    private LocalTime currentSelectedTime;

    /**
     * Creates a JFormattedTextField, attaches the TimePicker, and applies TimePicker defaults.
     * <p>
     * For SOME REASON, the method getSelectedDate/getSelectedTime only fires when a selection is made, and
     * then just drops the value. And so I had to wrap it with getDate/getTime to store persistently.
     * @return A JFormattedTextField on which the TimePicker has been attached.
     */
    public JFormattedTextField createParent() {
        LocalTime defaultTime = LocalTime.now().withSecond(0).withNano(0).plusMinutes(65);

        JFormattedTextField ftf = new JFormattedTextField();
        setEditor(ftf);

        setOrientation(HORIZONTAL);
        setSelectedTime(defaultTime);
        set24HourView(true);

        // WHY DOESN'T THE METHOD DO WHAT IT SAYS IT DOES
        addTimeSelectionListener(timeEvent -> currentSelectedTime = getSelectedTime());

        return ftf;
    }

    public LocalTime getTime() { return currentSelectedTime; }
    public void setTime(LocalTime time) { currentSelectedTime = time; setSelectedTime(time); }
}
