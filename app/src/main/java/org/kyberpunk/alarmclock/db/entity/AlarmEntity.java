package org.kyberpunk.alarmclock.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.kyberpunk.alarmclock.db.converters.DaysOfWeekConverter;
import org.kyberpunk.alarmclock.model.Alarm;

import java.util.Set;

@Entity(tableName = "alarms")
@TypeConverters({DaysOfWeekConverter.class})
public class AlarmEntity implements Alarm {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean enabled;
    private int hours;
    private int minutes;
    private Set<Integer> days;
    private String radioShortcut;
    private String radioName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public Set<Integer> getDays() {
        return days;
    }

    public void setDays(Set<Integer> days) {
        this.days = days;
    }

    public String getRadioShortcut() {
        return radioShortcut;
    }

    public void setRadioShortcut(String radioShortcut) {
        this.radioShortcut = radioShortcut;
    }

    public String getRadioName() {
        return radioName;
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
    }
}
