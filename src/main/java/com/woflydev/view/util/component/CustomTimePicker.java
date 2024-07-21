package com.woflydev.view.util.component;

import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import java.time.LocalTime;

public class CustomTimePicker {

    public static TimePicker create() {
        TimePickerSettings settings = new TimePickerSettings();

        settings.setFormatForDisplayTime("HH:mm");
        settings.setFormatForMenuTimes("HH:mm");
        settings.generatePotentialMenuTimes(TimePickerSettings.TimeIncrement.FifteenMinutes, null, null);

        LocalTime defaultTime = roundToNearest15Minutes(LocalTime.now().withSecond(0).withNano(0).plusHours(1));
        TimePicker timePicker = new TimePicker(settings);

        timePicker.setTime(defaultTime);

        timePicker.addTimeChangeListener(event -> {
            LocalTime newTime = event.getNewTime();
            LocalTime adjustedTime = roundToNearest15Minutes(newTime);
            if (!newTime.equals(adjustedTime)) {
                timePicker.setTime(adjustedTime);
            }
        });

        return timePicker;
    }

    private static LocalTime roundToNearest15Minutes(LocalTime time) {
        int minutes = time.getMinute();
        int roundedMinutes = (minutes / 15) * 15;
        return LocalTime.of(time.getHour(), roundedMinutes);
    }
}
