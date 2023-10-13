package com.oragif.jxpress.middleware;

import com.oragif.jxpress.event.IRequestOnClose;
import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.worker.Worker;

import java.time.LocalDateTime;
import java.util.HashMap;

import static com.oragif.jxpress.util.Encryption.sha256;

public class Session extends Worker implements IRequestOnClose {
    private static HashMap<String, Session> sessions;
    private HashMap<String, Object> sessionData;
    private String sessionKey;
    private LocalDateTime lastRequestTime;
    private String lastPath;

    static {
        sessions = new HashMap<>();
    }

    {
        this.sessionData = new HashMap<>();
    }

    private static Session createSession(Request request) {
        Session session = new Session();
        String key;
        do {
            key = sha256(request.getIp() + LocalDateTime.now());
        } while (sessions.containsKey(key));

        session.lastRequestTime = LocalDateTime.now();
        session.lastPath = request.getPath();
        session.sessionKey = key;
        sessions.put(key, session);

        return session;
    }

    private Session getOrCreateSession(Request request) {
        String key = request.getCookie("session");
        if (key == null || !sessions.containsKey(key)) return createSession(request);
        return sessions.get(key);
    }

    @Override
    public void handle(Request request, Response response) {
        Session session = getOrCreateSession(request);
        request.setMiddlewareData("session", session);
        request.setMiddlewareData("sessionKey", this.sessionKey);
        response.addCookie("session", this.sessionKey);
        response.listenOnClose(this::onClose);
    }

    public void setSessionData(String key, Object data) {
        this.sessionData.put(key, data);
    }

    public Object getSessionData(String key) {
        return this.sessionData.get(key);
    }

    public HashMap<String, Object> getAllSessionData() {
        return this.sessionData;
    }

    public LocalDateTime getLastRequestTime() {
        return this.lastRequestTime;
    }

    public String getLastPath() {
        return this.lastPath;
    }

    public String getSessionKey() {
        return this.sessionKey;
    }

    @Override
    public void onClose(Request request, Response response) {
        this.lastRequestTime = LocalDateTime.now();
        this.lastPath        = request.getPath();
        sessions.put(this.sessionKey, this);
    }
}