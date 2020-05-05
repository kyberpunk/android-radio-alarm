package org.kyberpunk.alarmclock.ui;

import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

public interface OnItemClickListener {
    void onItemClicked(AlarmEntity alarm);
}
