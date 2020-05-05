package org.kyberpunk.alarmclock.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.kyberpunk.alarmclock.AlarmFiredActivity;
import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

import java.util.concurrent.Executors;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int alarmId = (int) intent.getExtras().get(AlarmFiredActivity.ALARM_ID);

        Intent i = new Intent(context, AlarmFiredActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.putExtra(AlarmFiredActivity.ALARM_ID, alarmId);
        context.startActivity(i);

        Executors.newSingleThreadExecutor().execute(() -> {
            AlarmDao dao = AlarmDatabase.getInstance(context).alarmDao();
            AlarmEntity alarm = dao.findById(alarmId);
            AlarmScheduler scheduler = new AlarmScheduler(context);
            scheduler.scheduleAlarm(alarm);
        });
    }
}
