package org.kyberpunk.alarmclock.ui;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class DaysStringBuilder {
    private Set<Integer> days;
    private int activeColor;
    DateFormatSymbols symbols;

    public DaysStringBuilder setActiveColor(int activeColor) {
        this.activeColor = activeColor;
        return this;
    }

    public DaysStringBuilder setDays(Set<Integer> days) {
        this.days = days;
        return this;
    }

    public DaysStringBuilder setLocale(Locale locale) {
        symbols = new DateFormatSymbols(locale);
        return this;
    }

    public Spannable build() {
        Spannable span = new SpannableString(createDaysString());
        if (days.contains(Calendar.MONDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.TUESDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 2, 3, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.WEDNESDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 4, 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.THURSDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 6, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.FRIDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 8, 9, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.SATURDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 10, 11, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (days.contains(Calendar.SUNDAY))
            span.setSpan(new ForegroundColorSpan(activeColor), 12, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return span;
    }

    private String createDaysString() {
        List<String> weekdays = new ArrayList<String>();
        Collections.addAll(weekdays, symbols.getWeekdays());
        weekdays.remove(0);
        Collections.rotate(weekdays, -1);
        return weekdays.stream()
                .map(s -> s.substring(0, 1).toUpperCase())
                .collect(Collectors.joining(","));
    }
}
