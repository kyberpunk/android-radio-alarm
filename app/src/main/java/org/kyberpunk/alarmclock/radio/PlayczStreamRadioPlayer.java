package org.kyberpunk.alarmclock.radio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.kyberpunk.alarmclock.radio.dto.GetStreamResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class PlayczStreamRadioPlayer implements RadioPlayer {
    private static final String TAG = "PlayczStreamRadioPlayer";

    private final MediaPlayer mediaPlayer;
    private PlayczClient playczClient;

    public PlayczStreamRadioPlayer(PlayczClient playczClient) {
        mediaPlayer = new MediaPlayer();
        this.playczClient = playczClient;
    }

    @Override
    public boolean play(String radioId, Bitrate bitrate) {
        try {
            Call<GetStreamResponse> call = playczClient.getStream(radioId, bitrate.getValue());
            Response<GetStreamResponse> response = call.execute();
            if (response.isSuccessful()) {
                String pubpoint = response.body().getData().getStream().getPubpoint();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(pubpoint);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
            return true;
        } catch (IOException e) {
            Log.w(TAG, "Cannot play radio.", e);
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
        } catch (IllegalStateException e) {
            Log.w(TAG, "Cannot stop radio.", e);
        }
    }

    @Override
    public void release() {
        mediaPlayer.release();
    }
}
