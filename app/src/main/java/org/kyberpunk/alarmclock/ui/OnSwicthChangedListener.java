package org.kyberpunk.alarmclock.ui;

import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

public interface OnSwicthChangedListener {
    void onChanged(AlarmEntity alarm, boolean isChecked);
}
