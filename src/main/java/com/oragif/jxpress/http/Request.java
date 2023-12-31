package com.oragif.jxpress.http;

import com.google.gson.Gson;
import com.oragif.jxpress.event.IRequestOnClose;
import com.oragif.jxpress.event.RequestEventHandler;
import com.sun.net.httpserver.HttpExchange;
import com.oragif.jxpress.worker.Method;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class Request {
    private final HttpExchange exchange;
    private final Method method;
    private byte[] body;
    private HashMap<String, String> cookies;
    private HashMap<String, List<String>> headers;
    private HashMap<String, String[]> parameters;
    private HashMap<String, Object> middlewareData;
    private String ip;
    private int level;
    private int maxLevel;
    private String path;
    private String[] leveledPath;

    {
        this.headers = new HashMap<>();
        this.middlewareData = new HashMap<>();
    }

    public Request(HttpExchange exchange, HashMap<String, String> cookies) {
        this.exchange = exchange;
        this.method   = Method.valueOf(exchange.getRequestMethod());
        this.cookies  = cookies;
        this.readBody();
        this.readHeaders();
        this.parameters   = this.getPathParameters();
        this.ip           = exchange.getRemoteAddress().getHostName();
        this.path         = exchange.getRequestURI().getPath();
        this.leveledPath  = splitPath(this.path);
        if (this.leveledPath.length == 0) this.leveledPath = new String[]{"/"};
        this.maxLevel     = this.leveledPath.length - 1;
    }

    private static String[] splitPath(String pathString) {
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(path1 -> "/".concat(path1.toString())).toArray(String[]::new);
    }

    public String getPath() {
        return this.path;
    }

    public String getLeveledPathFromRoot(int level) {
        return String.join("", Arrays.copyOfRange(this.leveledPath, 0, level));
    }

    public String getLeveledPath(int fromLevel, int toLevel) {
        return String.join("", Arrays.copyOfRange(this.leveledPath, fromLevel, toLevel));
    }

    public String getCurrentLeveledPath() {
        return this.leveledPath[this.level];
    }

    public int getLevel() {
        return this.level;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public int nextLevel() {
        this.level += 1;
        return this.getLevel();
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

    public HashMap<String, Object> getAllMiddlewareData() {
        return middlewareData;
    }

    public Object getMiddlewareData(String key) {
        return middlewareData.get(key);
    }

    public void setMiddlewareData(String key, Object data) {
        this.middlewareData.put(key, data);
    }

    public String getIp() {
        return this.ip;
    }
}
