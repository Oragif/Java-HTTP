package oragif.jxpress.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.Arrays;

public class Request {
    private final HttpExchange exchange;
    private String body;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
        this.readBody();
    }

    private void readBody() {
        String body;
        try {
            body = Arrays.toString(this.exchange.getRequestBody().readAllBytes());
        } catch (IOException e) {
            body = "";
        }
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }
}
