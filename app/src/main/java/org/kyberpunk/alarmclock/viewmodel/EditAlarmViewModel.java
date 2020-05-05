package org.kyberpunk.alarmclock.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import org.kyberpunk.alarmclock.db.AlarmDatabase;
import org.kyberpunk.alarmclock.db.dao.AlarmDao;
import org.kyberpunk.alarmclock.db.entity.AlarmEntity;
import org.kyberpunk.alarmclock.model.Alarm;
import org.kyberpunk.alarmclock.radio.Bitrate;
import org.kyberpunk.alarmclock.radio.dto.GetRadiosResponse;
import org.kyberpunk.alarmclock.radio.PlayczClient;
import org.kyberpunk.alarmclock.radio.PlayczClientFactory;
import org.kyberpunk.alarmclock.radio.PlayczStreamRadioPlayer;
import org.kyberpunk.alarmclock.radio.dto.Radio;
import org.kyberpunk.alarmclock.radio.RadioPlayer;
import org.kyberpunk.alarmclock.schedule.AlarmScheduler;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAlarmViewModel extends ViewModel {
    private AlarmDao dao;
    private AlarmEntity alarm;
    private ExecutorService executor;
    private boolean isNew;
    private LiveData<AlarmEntity> alarmLiveData;
    private PlayczClient playczClient;
    private RadioPlayer radioPlayer;
    private boolean isPlaying;
    private AlarmScheduler scheduler;
    private Lock lock;

    private EditAlarmViewModel() {
        PlayczClientFactory playczClientFactory = new PlayczClientFactory();
        playczClient = playczClientFactory.createClient();
        executor = Executors.newSingleThreadExecutor();
        radioPlayer = new PlayczStreamRadioPlayer(playczClient);
        lock = new ReentrantLock();
    }

    public EditAlarmViewModel(Context context) {
        this();
        dao = AlarmDatabase.getInstance(context).alarmDao();
        alarm = new AlarmEntity();
        final Calendar cldr = Calendar.getInstance();
        alarm.setHours(cldr.get(Calendar.HOUR_OF_DAY));
        alarm.setMinutes(cldr.get(Calendar.MINUTE));
        alarm.setEnabled(true);
        Set<Integer> defaultDays = new HashSet<>();
        defaultDays.add(cldr.get(Calendar.DAY_OF_WEEK));
        alarm.setDays(defaultDays);
        isNew = true;
        scheduler = new AlarmScheduler(context);
    }

    public EditAlarmViewModel(Context context, AlarmEntity alarm)
    {
        this();
        dao = AlarmDatabase.getInstance(context).alarmDao();
        this.alarm = alarm;
        isNew = false;
        scheduler = new AlarmScheduler(context);
    }

    public void setEnabled(boolean isEnabled) {
        alarm.setEnabled(isEnabled);
    }

    public void setTime(int hours, int minutes) {
        alarm.setHours(hours);
        alarm.setMinutes(minutes);
    }

    public void setDays(Set<Integer> days) {
        alarm.setDays(days);
    }

    public void setRadioName(String name) {
        alarm.setRadioName(name);
    }

    public void setRadioShortCut(String shortcut) {
        alarm.setRadioShortcut(shortcut);
    }

    public boolean isNew() {
        return isNew;
    }

    public Alarm getCurrent() {
        return alarm;
    }

    public void save() {
        if (isNew)
            executor.execute(() -> {
                int id = (int) dao.insert(alarm);
                alarm.setId(id);
                if (alarm.isEnabled())
                    scheduler.scheduleAlarm(alarm);
            });
        else
            executor.execute(() -> {
                alarm.setEnabled(true);
                dao.update(alarm);
                scheduler.scheduleAlarm(alarm);
            });
    }

    public void delete() {
        if (!isNew) {
            executor.execute(() -> {
                dao.delete(alarm);
                if (alarm.isEnabled())
                    scheduler.cancelAlarm(alarm);
            });
        }
    }

    public CompletableFuture<Collection<Radio>> getRadios() {
        Call<GetRadiosResponse> call = playczClient.getRadios();
        final CompletableFuture<Collection<Radio>> future = new CompletableFuture<>();
        call.enqueue(new Callback<GetRadiosResponse>() {
            @Override
            public void onResponse(Call<GetRadiosResponse> call, Response<GetRadiosResponse> response) {
                if (response.isSuccessful())
                    future.complete(response.body().getData().values());
                else
                    future.completeExceptionally(new RuntimeException("Failed with status: " + response.code()));
            }

            @Override
            public void onFailure(Call<GetRadiosResponse> call, Throwable t) {
                future.completeExceptionally(t);
            }
        });
        return future;
    }

    public boolean validate() {
        return !alarm.getDays().isEmpty()
                && alarm.getRadioName() != null
                && alarm.getRadioShortcut() != null;
    }

    public CompletableFuture<Boolean> play() {
        return CompletableFuture.supplyAsync(() -> {
            lock.lock();
            Boolean result = radioPlayer.play(alarm.getRadioShortcut(), Bitrate.BITRATE_64);
            lock.unlock();
            isPlaying = result;
            return result;
        }, executor);
    }

    public void stop() {
        executor.execute(() -> {
            lock.lock();
            if (isPlaying)
                radioPlayer.stop();
            lock.unlock();
            isPlaying = false;
        });
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void releasePlayer() { radioPlayer.release(); }
}
