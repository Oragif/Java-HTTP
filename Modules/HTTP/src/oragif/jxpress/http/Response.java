package oragif.jxpress.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Response {
    private final HttpExchange exchange;
    private final OutputStream outputStream;
    private final Headers headers;
    private boolean closed;
    private String currentLayer;
    private String path;
    private int responseCode;

    {
        this.closed = false;
    }
    public Response(HttpExchange exchange) {
        this.exchange = exchange;
        this.outputStream = exchange.getResponseBody();
        this.headers = exchange.getResponseHeaders();
    }

    public void close() {
        this.exchange.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getCurrentLayer() {
        return currentLayer;
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

    public void send(String message) throws IOException {
        byte[] byteMessage = message.getBytes(StandardCharsets.UTF_8);
        int messageSize    = byteMessage.length;
        this.exchange.sendResponseHeaders(this.responseCode, messageSize);
        this.outputStream.write(byteMessage);
        this.close();
    }

    public void send(Object object) throws IOException {
        this.send(object.toString());
    }
}
