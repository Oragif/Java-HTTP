package com.oragif.jxpress.middleware;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.worker.Worker;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Base64;

public class Session extends Worker {
    private static HashMap<String, Session> sessions;
    private HashMap<String, Object> sessionData;
    private String sessionKey;

    static {
        sessions = new HashMap<>();
    }

    {
        this.sessionData = new HashMap<>();
    }

    private static String sha256(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(messageDigest.digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ignored) {}
        return null;
    }

    private static String createSession(Request request) {
        Session session = new Session();
        String key;
        do {
            key = sha256(request.getIp() + LocalDateTime.now());
        } while (sessions.containsKey(key));

        sessions.put(key, session);
        return key;
    }

    private Session getOrCreateSession(Request request) {
        String key = request.getCookie("session");
        if (key == null || !sessions.containsKey(key)) key = createSession(request);
        this.sessionKey = key;
        return sessions.get(key);
    }

    @Override
    public void handle(Request request, Response response) {
        Session session = getOrCreateSession(request);
        request.setMiddlewareData("session", session);
        request.setMiddlewareData("sessionKey", this.sessionKey);
        response.addHeader("Set-Cookie", "session=" + this.sessionKey);
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
}