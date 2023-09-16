package oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;

public class Request {
    private final HttpExchange exchange;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
    }
}
