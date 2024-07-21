package com.woflydev.view.component;

import com.github.lgooddatepicker.components.DatePicker;

import java.time.LocalDate;

public class CustomDatePicker {
    public static DatePicker create() {
        DatePicker dp = new DatePicker();

        dp.setDate(LocalDate.now());

        return dp;
    }
}
