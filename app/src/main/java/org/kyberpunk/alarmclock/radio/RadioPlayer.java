package org.kyberpunk.alarmclock.radio;

public interface RadioPlayer {
    boolean play(String radioId, Bitrate bitrate);
    void stop();
    void release();
}
