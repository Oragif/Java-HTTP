package com.oragif.jxpress.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.oragif.jxpress.worker.Method;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Request {
    private final HttpExchange exchange;
    private final Method method;
    private byte[] body;
    private HashMap<String, String> cookies;
    private HashMap<String, List<String>> headers;
    private SessionData sessionData;

    {
        this.headers = new HashMap<>();
    }

    public Request(HttpExchange exchange, SessionData sessionData, HashMap<String, String> cookies) {
        this.exchange = exchange;
        this.method   = Method.valueOf(exchange.getRequestMethod());
        this.cookies = cookies;
        this.sessionData = sessionData;
        this.readBody();
        this.readHeaders();
    }
    private void readHeaders() {
        this.exchange.getRequestHeaders().forEach((s, strings) -> this.headers.put(s, strings));
    }

    private void readBody() {
        try {
            this.body = this.exchange.getRequestBody().readAllBytes();
        } catch (IOException e) {
            // If it fails it's just empty
        }
    }

    public Method getMethod() {
        return method;
    }


    public String getRawBody() {
        return new String(this.body);
    }

    public byte[] getByteBody() { return this.body; }

    public <T> T getJsonBody(Class<T> jsonClass) {
        return new Gson().fromJson(this.getRawBody(), jsonClass);
    }

    public HashMap<String, String> getCookies() {
        return this.cookies;
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.headers;
    }

    public SessionData getSessionData() {
        return this.sessionData;
    }
}
