package org.kyberpunk.alarmclock.radio;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class PlayczClientFactory {
    public static final String BASE_URL = "https://api.play.cz/";

    private Retrofit retrofit;

    public PlayczClientFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public PlayczClient createClient() {
        return retrofit.create(PlayczClient.class);
    }
}
