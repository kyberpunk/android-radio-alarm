package org.kyberpunk.alarmclock.model;

import java.time.DayOfWeek;
import java.util.Set;

public interface Alarm {
    int getId();
    boolean isEnabled();
    public int getHours();
    public int getMinutes();
    Set<Integer> getDays();
    String getRadioShortcut();
    String getRadioName();
}
