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
    private HashMap<String, String[]> parameters;

    {
        this.headers = new HashMap<>();
    }

    public Request(HttpExchange exchange, HashMap<String, String> cookies) {
        this.exchange = exchange;
        this.method   = Method.valueOf(exchange.getRequestMethod());
        this.cookies = cookies;
        this.readBody();
        this.readHeaders();
        this.parameters   = this.getPathParameters();
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

    private HashMap<String, String[]> getPathParameters() {
        String query = exchange.getRequestURI().getQuery();
        HashMap<String, String[]> parameters = new HashMap<>();
        if (query == null) return parameters;

        String[] splitParameters = query.split("[&]");
        for (String param: splitParameters) {
            String[] keyValue = param.split("=");

            if (keyValue.length < 2) continue;

            String[] values = keyValue[1].split(",");
            parameters.put(keyValue[0], values);
        }
        return parameters;
    }

    public HashMap<String, String[]> getParameters() {
        return this.parameters;
    }

    public String[] getParameter(String key) {
        return this.parameters.get(key);
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
    public String getCookie(String key) {
        return this.cookies.get(key);
    }

    public HashMap<String, List<String>> getHeaders() {
        return this.headers;
    }
    public List<String> getHeader(String key) {
        return this.headers.get(key);
    }
}
