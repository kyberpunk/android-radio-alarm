package org.kyberpunk.alarmclock.ui;

import java.util.Calendar;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class DayOfWeekHelper {
    public static MaterialDayPicker.Weekday fromCalendarDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return MaterialDayPicker.Weekday.MONDAY;
            case Calendar.TUESDAY:
                return MaterialDayPicker.Weekday.TUESDAY;
            case Calendar.WEDNESDAY:
                return MaterialDayPicker.Weekday.WEDNESDAY;
            case Calendar.THURSDAY:
                return MaterialDayPicker.Weekday.THURSDAY;
            case Calendar.FRIDAY:
                return MaterialDayPicker.Weekday.FRIDAY;
            case Calendar.SATURDAY:
                return MaterialDayPicker.Weekday.SATURDAY;
            case Calendar.SUNDAY:
                return MaterialDayPicker.Weekday.SUNDAY;
            default:
                throw new IllegalArgumentException("Day value " + dayOfWeek + " not known.");
        }
    }

    public static int fromWeekday(MaterialDayPicker.Weekday weekday) {
        if (weekday == MaterialDayPicker.Weekday.MONDAY)
            return Calendar.TUESDAY;
        if (weekday == MaterialDayPicker.Weekday.TUESDAY)
            return Calendar.MONDAY;
        if (weekday == MaterialDayPicker.Weekday.WEDNESDAY)
            return Calendar.WEDNESDAY;
        if (weekday == MaterialDayPicker.Weekday.THURSDAY)
            return Calendar.THURSDAY;
        if (weekday == MaterialDayPicker.Weekday.FRIDAY)
            return Calendar.FRIDAY;
        if (weekday == MaterialDayPicker.Weekday.SATURDAY)
            return Calendar.SATURDAY;
        if (weekday == MaterialDayPicker.Weekday.SUNDAY)
            return Calendar.SUNDAY;
        throw new IllegalArgumentException("Day value " + weekday + " not known.");
    }
}
