package com.oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.oragif.jxpress.routing.Router;

public class RequestManager extends Router implements HttpHandler {
    {
        this.setRoot("");
    }
    @Override
    public void handle(HttpExchange exchange) {
        Request  request  = new Request(exchange);
        Response response = new Response(exchange);
        this.handle(request, response);
        if (!response.isClosed()) {
            response.setResponseCode(404);
            response.send("404 not found");
            response.close();
        }
    }
}
