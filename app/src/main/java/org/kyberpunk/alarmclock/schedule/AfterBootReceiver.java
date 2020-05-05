package org.kyberpunk.alarmclock.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class AfterBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AlarmDao dao = AlarmDatabase.getInstance(context).alarmDao();
            AlarmScheduler scheduler = new AlarmScheduler(context);
            List<AlarmEntity> alarms = dao.findAllEnabled();
            scheduler.scheduleAlarms(alarms);
        });
    }
}
