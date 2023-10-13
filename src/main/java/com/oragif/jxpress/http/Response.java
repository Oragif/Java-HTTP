package com.oragif.jxpress.http;

import com.oragif.jxpress.event.IRequestOnClose;
import com.oragif.jxpress.event.RequestEventHandler;
import com.oragif.jxpress.util.CookieBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import javax.management.ObjectInstance;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

public class Response {
    private final HttpExchange exchange;
    private final OutputStream outputStream;
    private final Headers headers;
    private boolean closed;
    private int responseCode;
    private RequestEventHandler requestEventHandler;
    private Request request;


    {
        this.responseCode = 200;
        this.closed = false;
        this.requestEventHandler = new RequestEventHandler();
    }

    public Response(HttpExchange exchange, Request request) {
        this.exchange     = exchange;
        this.outputStream = exchange.getResponseBody();
        this.headers      = exchange.getResponseHeaders();
        this.request = request;
    }

    public void close() {
        this.requestEventHandler.triggerOnCloseListeners(this.request, this);
        this.exchange.close();
        this.closed = true;
    }

    public boolean isClosed() {
        return closed;
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

    public void addHeader(String header, List<String> values) {
        this.headers.put(header, values);
    }

    private void addCookie(String cookieString) {
        if (!this.headers.containsKey("Set-Cookie")) {
            this.headers.add("Set-Cookie", cookieString);
        } else {
            List<String> cookies = this.headers.get("Set-Cookie");
            cookies.add(cookieString);
        }
    }

    public void addCookie(String key, String value) {
        String cookieString = key + "=" + value;
        this.addCookie(cookieString);
    }

    public void addCookie(CookieBuilder cookieBuilder) {
        this.addCookie(cookieBuilder.build());
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

    public void listenOnClose(IRequestOnClose requestOnClose) {
        this.requestEventHandler.listenOnClose(requestOnClose);
    }
}
