package org.kyberpunk.alarmclock.schedule;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import org.kyberpunk.alarmclock.radio.Bitrate;
import org.kyberpunk.alarmclock.radio.PlayczClient;
import org.kyberpunk.alarmclock.radio.PlayczClientFactory;
import org.kyberpunk.alarmclock.radio.PlayczStreamRadioPlayer;
import org.kyberpunk.alarmclock.radio.RadioPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RadioService extends Service {
    public static final String RADIO_SHORTCUT = "RADIO_SHORTCUT";

    private RadioPlayer radioPlayer;
    private PowerManager.WakeLock wakeLock;
    private ExecutorService executor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PlayczClientFactory playczClientFactory = new PlayczClientFactory();
        PlayczClient playczClient = playczClientFactory.createClient();
        radioPlayer = new PlayczStreamRadioPlayer(playczClient);
        executor = Executors.newSingleThreadExecutor();

        PowerManager mgr = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "app:RadioService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        wakeLock.acquire();
        String radioShortcut = intent.getExtras().getString(RADIO_SHORTCUT);
        executor.execute(() -> radioPlayer.play(radioShortcut, Bitrate.BITRATE_64));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.execute(() -> radioPlayer.stop());
        radioPlayer.release();
        wakeLock.release();
    }
}
