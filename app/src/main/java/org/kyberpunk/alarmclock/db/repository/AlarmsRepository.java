package org.kyberpunk.alarmclock.db.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.model.Alarm;

import java.util.List;

public class AlarmsRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> allAlarms;

    public AlarmsRepository(Context context) {
        AlarmDatabase db = Room.databaseBuilder(context, AlarmDatabase.class, "database-name").build();
        alarmDao = db.alarmDao();

    }
}
