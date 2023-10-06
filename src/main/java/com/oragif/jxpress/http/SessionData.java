package com.oragif.jxpress.http;

import java.time.LocalDateTime;

public class SessionData {
    private LocalDateTime lastSessionDateTime;
    private String lastRequestedPath;
    private String sessionId;
    private Object data;

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SessionData(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public LocalDateTime getLastSessionDateTime() {
        return lastSessionDateTime;
    }

    public void setLastSessionDateTime(LocalDateTime lastSessionDateTime) {
        this.lastSessionDateTime = lastSessionDateTime;
    }

    public String getLastRequestedPath() {
        return this.lastRequestedPath;
    }

    public void setLastRequestedPath(String lastRequestedPath) {
        this.lastRequestedPath = lastRequestedPath;
    }
}
