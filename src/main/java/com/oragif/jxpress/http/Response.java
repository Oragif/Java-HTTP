package com.oragif.jxpress.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class Response {
    private final HttpExchange exchange;
    private final OutputStream outputStream;
    private final Headers headers;
    private boolean closed;
    private int level;
    private int maxLevel;
    private String path;
    private String[] leveledPath;
    private HashMap<String, String[]> parameters;
    private int responseCode;

    private Request request;

    {
        this.responseCode = 200;
        this.closed = false;
    }

    public Response(HttpExchange exchange, Request request) {
        this.exchange     = exchange;
        this.outputStream = exchange.getResponseBody();
        this.headers      = exchange.getResponseHeaders();
        this.path         = exchange.getRequestURI().getPath();
        this.leveledPath  = splitPath(this.path);
        this.parameters   = this.getPathParameters();
        if (this.leveledPath.length == 0) this.leveledPath = new String[]{"/"};
        this.maxLevel     = this.leveledPath.length - 1;
        this.request = request;
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

    private static String[] splitPath(String pathString) {
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(path1 -> "/".concat(path1.toString())).toArray(String[]::new);
    }

    public void close() {
        this.exchange.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public HashMap<String, String[]> getParameters() {
        return this.parameters;
    }

    public String[] getParameter(String key) {
        return this.parameters.get(key);
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

    public void setResponseCode(int code) {
        this.responseCode = code;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public void addHeader(String header, String value) {
        this.headers.add(header, value);
    }

    public void addHeader(String header, List<String> value) {
        this.headers.put(header, value);
    }

    public void setContentType(String type) {
        this.addHeader("Content-Type", type);
    }

    public boolean send(String message) {
        try {
            byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
            int messageSize    = byteMessage.length;
            this.exchange.sendResponseHeaders(this.responseCode, messageSize);
            this.outputStream.write(byteMessage);
            this.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void send(Object object) {
        this.send(object.toString());
    }
}
