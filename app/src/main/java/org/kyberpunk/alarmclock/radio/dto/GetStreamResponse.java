package org.kyberpunk.alarmclock.radio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetStreamResponse {
    private String redir;
    private StreamData data;

    public String getRedir() {
        return redir;
    }

    public void setRedir(String redir) {
        this.redir = redir;
    }

    public StreamData getData() {
        return data;
    }

    public void setData(StreamData data) {
        this.data = data;
    }
}
