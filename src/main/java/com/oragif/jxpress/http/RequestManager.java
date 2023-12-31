package com.oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.oragif.jxpress.routing.Router;

import java.util.HashMap;
import java.util.List;

public class RequestManager extends Router implements HttpHandler {

    private IRequestHandler notFoundEndpoint;

    {
        this.setRoot("");
        this.set404(((request, response) -> {
            response.send("404 not found");
        }));
    }

    @Override
    public void handle(HttpExchange exchange) {
        HashMap<String, String> cookies = readCookies(exchange);

        Request  request  = new Request(exchange, cookies);
        Response response = new Response(exchange, request);

        this.handle(request, response);
        this.endExchange(request, response);
    }

    private void endExchange(Request request, Response response) {
         if (!response.isClosed()) {
            response.setResponseCode(404);
            this.notFoundEndpoint.handle(request, response);
            response.close();
        }
    }

    private HashMap<String, String> readCookies(HttpExchange exchange) {
        List<String> cookieHeader = exchange.getRequestHeaders().get("Cookie");
        if (cookieHeader == null) return new HashMap<>();
        String[] headerCookies = cookieHeader.get(0).split(";");
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


