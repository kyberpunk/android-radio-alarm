package org.kyberpunk.alarmclock.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.kyberpunk.alarmclock.AlarmFiredActivity;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlarmScheduler {
    private AlarmManager alarmManager;
    private Context context;
    private Locale locale;

    public AlarmScheduler(Context context) {
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
        this.locale = context.getResources().getConfiguration().getLocales().get(0);
    }

    public void scheduleAlarms(List<AlarmEntity> alarms) {
        for (AlarmEntity alarm : alarms) {
            scheduleAlarm(alarm);
        }
    }

    public void scheduleAlarm(AlarmEntity alarm) {
        PendingIntent intent = createPendingIntent(alarm.getId());
        long millis = getNextFireMillis(alarm);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, intent);
    }

    private PendingIntent createPendingIntent(int id) {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra(AlarmFiredActivity.ALARM_ID, id);
        return PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private long getNextFireMillis(AlarmEntity alarm) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHours());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        setNearestDay(calendar, alarm);
        return calendar.getTimeInMillis();
    }

    private void setNearestDay(Calendar alarmTime, AlarmEntity alarm) {
        while (!daySuitable(alarmTime, alarm)) {
            alarmTime.add(Calendar.DATE, 1);
        }
    }

    private boolean daySuitable(Calendar alarmTime, AlarmEntity alarm) {
        Calendar now = Calendar.getInstance(locale);
        return alarm.getDays().contains(alarmTime.get(Calendar.DAY_OF_WEEK))
                && alarmTime.getTimeInMillis() > now.getTimeInMillis();
    }

    public void cancelAlarm(AlarmEntity alarm) {
        alarmManager.cancel(createPendingIntent(alarm.getId()));
    }
}
