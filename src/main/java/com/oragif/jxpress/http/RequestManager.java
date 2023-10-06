package com.oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.oragif.jxpress.routing.Router;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;

public class RequestManager extends Router implements HttpHandler {
    HashMap<String, SessionData> sessionData;
    String currentSession;
    boolean sessionEnabled;

    private IRequestHandler notFoundEndpoint;

    {
        this.setRoot("");
        this.sessionData = new HashMap<>();
        this.sessionEnabled = false;
        this.set404(((request, response) -> {
            response.send("404 not found");
        }));
    }

    @Override
    public void handle(HttpExchange exchange) {
        HashMap<String, String> cookies = readCookies(exchange);
        String sessionId = this.sessionEnabled ? this.getOrCreateSessionDataKey(exchange, cookies) : null;
        System.out.println(sessionId == null ? "No session" : sessionId);
        Request  request  = new Request(exchange, this.sessionEnabled ? this.sessionData.get(sessionId) : null, cookies);
        Response response = new Response(exchange, request);
        if (this.sessionEnabled) {
            response.addHeader("Set-Cookie", "sessionId=" + sessionId);
        }

        this.handle(request, response);
        this.endExchange(exchange, request, response);
    }

    private String getOrCreateSessionDataKey(HttpExchange exchange, HashMap<String, String> cookies) {
        if (!this.sessionEnabled) {
            return null;
        }

        String sessionString = cookies.get("sessionId");
        if (sessionString != null && this.sessionData.containsKey(sessionString)) {
            this.currentSession = sessionString;
            return this.currentSession;
        }

        String key = this.createSha256(exchange.getRemoteAddress().getHostString() + LocalDateTime.now().toString());

        SessionData sessionData = new SessionData(key);
        sessionData.setLastRequestedPath(exchange.getRequestURI().getPath());
        sessionData.setLastSessionDateTime(LocalDateTime.now());
        this.sessionData.put(key, sessionData);

        return key;
    }

    public String createSha256(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(hash);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void endExchange(HttpExchange exchange, Request request, Response response) {
        if (this.sessionEnabled) {
            SessionData sessionData;
            sessionData = request.getSessionData();
            sessionData.setLastRequestedPath(exchange.getRequestURI().getPath());
            sessionData.setLastSessionDateTime(LocalDateTime.now());
            this.sessionData.put(this.currentSession, sessionData);
        }

        if (!response.isClosed()) {
            response.setResponseCode(404);
            this.notFoundEndpoint.handle(request, response);
            response.close();
        }
    }

    private HashMap<String, String> readCookies(HttpExchange exchange) {
        String[] headerCookies = exchange.getRequestHeaders().get("Cookie").get(0).split(";");
        if (headerCookies.length == 0) return null;

        HashMap<String, String> cookies = new HashMap<>();
        for (String cookie : headerCookies) {
            String[] keyValue = cookie.split("=", 2);
            cookies.put(keyValue[0].trim(), keyValue.length == 1 ? "" : keyValue[1]);
        }

        return cookies;
    }

    public void set404(IRequestHandler method) {
        this.notFoundEndpoint = method;
    }

    public void enableSession() {
        this.sessionEnabled = true;
    }

    public void disableSession() {
        this.sessionEnabled = false;
    }
}


