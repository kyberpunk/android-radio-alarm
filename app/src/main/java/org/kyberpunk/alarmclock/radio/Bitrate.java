package org.kyberpunk.alarmclock.radio;

public enum Bitrate {
    BITRATE_64(64),
    BITRATE_128(128);

    private int value;

    Bitrate(int value) {
        this.value = value;
    }

    public String getValue() {
        return Integer.toString(value);
    }
}
