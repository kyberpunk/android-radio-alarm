package org.kyberpunk.alarmclock.radio;

import org.kyberpunk.alarmclock.radio.dto.GetRadiosResponse;
import org.kyberpunk.alarmclock.radio.dto.GetStreamResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlayczClient {
    @GET("json/getRadios")
    Call<GetRadiosResponse> getRadios();

    @GET("json/getStream/{shortcut}/mp3/{bitrate}")
    Call<GetStreamResponse> getStream(@Path("shortcut") String shortcut, @Path("bitrate") String bitrate);
}
