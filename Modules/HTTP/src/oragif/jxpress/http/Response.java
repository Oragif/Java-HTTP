package oragif.jxpress.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private int responseCode;

    {
        this.responseCode = 200;
        this.closed = false;
    }

    public Response(HttpExchange exchange) {
        this.exchange     = exchange;
        this.outputStream = exchange.getResponseBody();
        this.headers      = exchange.getResponseHeaders();
        this.path         = exchange.getRequestURI().getPath();
        this.leveledPath  = splitPath(this.path);
        if (this.leveledPath.length == 0) this.leveledPath = new String[]{"/"};
        this.maxLevel     = this.leveledPath.length;
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

    public String getPath() {
        return this.path;
    }
    public String getLeveledPath(int level) {
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
    public void setLevel(int level) {
        this.level = level;
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
