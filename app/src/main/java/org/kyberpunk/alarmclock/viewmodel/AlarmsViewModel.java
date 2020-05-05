package org.kyberpunk.alarmclock.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;

import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;
import org.kyberpunk.alarmclock.schedule.AlarmScheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmsViewModel {
    private AlarmDao dao;
    private ExecutorService executor;
    private AlarmScheduler scheduler;

    public AlarmsViewModel(Context context) {
        dao = AlarmDatabase.getInstance(context).alarmDao();
        executor = Executors.newSingleThreadExecutor();
        scheduler = new AlarmScheduler(context);
    }

    public LiveData<List<AlarmEntity>> getAll() {
        return dao.findAllLive();
    }

    public void update(AlarmEntity alarm) {
        executor.execute(() -> {
            dao.update(alarm);
            if (alarm.isEnabled())
                scheduler.scheduleAlarm(alarm);
            else
                scheduler.cancelAlarm(alarm);
        });
    }

    public void delete(AlarmEntity alarm) {
        executor.execute(() -> {
            dao.delete(alarm);
            if (alarm.isEnabled())
                scheduler.cancelAlarm(alarm);
        });
    }
}
