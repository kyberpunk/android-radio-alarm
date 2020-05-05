package org.kyberpunk.alarmclock.radio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stream {
    private String pubpoint;

    public String getPubpoint() {
        return pubpoint;
    }

    public void setPubpoint(String pubpoint) {
        this.pubpoint = pubpoint;
    }
}
