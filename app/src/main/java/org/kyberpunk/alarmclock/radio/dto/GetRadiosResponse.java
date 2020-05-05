package org.kyberpunk.alarmclock.radio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetRadiosResponse {
    private Map<String, Radio> data;

    public GetRadiosResponse() {
        this.data = new HashMap<>();
    }

    public Map<String, Radio> getData() {
        return data;
    }

    public void setData(Map<String, Radio> data) {
        this.data = data;
    }
}
