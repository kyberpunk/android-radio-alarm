package org.kyberpunk.alarmclock.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

@Database(entities = {AlarmEntity.class}, version = 6, exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {
    private static final String LOG_TAG = AlarmDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "alarms";

    private static AlarmDatabase instance;

    public abstract AlarmDao alarmDao();

    public static AlarmDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        AlarmDatabase.class, AlarmDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return instance;
    }
}
