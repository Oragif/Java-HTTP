package com.oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.oragif.jxpress.routing.Router;

import java.util.HashMap;

public class RequestManager extends Router implements HttpHandler {
    HashMap<String, SessionData> sessionData;
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

        Request  request  = new Request(exchange, cookies);
        Response response = new Response(exchange);

        this.handle(request, response);
        this.endExchange(exchange, request, response);
    }

    private void endExchange(HttpExchange exchange, Request request, Response response) {
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
}


