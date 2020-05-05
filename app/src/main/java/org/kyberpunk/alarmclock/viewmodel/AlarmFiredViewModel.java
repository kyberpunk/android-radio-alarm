package org.kyberpunk.alarmclock.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;
import org.kyberpunk.alarmclock.radio.Bitrate;
import org.kyberpunk.alarmclock.radio.PlayczClient;
import org.kyberpunk.alarmclock.radio.PlayczClientFactory;
import org.kyberpunk.alarmclock.radio.PlayczStreamRadioPlayer;
import org.kyberpunk.alarmclock.radio.RadioPlayer;
import org.kyberpunk.alarmclock.schedule.AlarmScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmFiredViewModel extends ViewModel {
    private AlarmDao dao;
    private AlarmScheduler scheduler;
    private int alarmId;
    private RadioPlayer radioPlayer;
    private ExecutorService executor;

    public AlarmFiredViewModel(Context context, int alarmId) {
        dao = AlarmDatabase.getInstance(context).alarmDao();
        scheduler = new AlarmScheduler(context);
        this.alarmId = alarmId;
        PlayczClientFactory playczClientFactory = new PlayczClientFactory();
        PlayczClient playczClient = playczClientFactory.createClient();
        radioPlayer = new PlayczStreamRadioPlayer(playczClient);
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<AlarmEntity> getAlarm() {
        return dao.findByIdLive(alarmId);
    }
}
