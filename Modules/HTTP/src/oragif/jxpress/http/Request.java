package oragif.jxpress.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import oragif.jxpress.worker.Method;

import java.io.IOException;

public class Request {
    private final HttpExchange exchange;
    private final Method method;
    private byte[] body;

    public Request(HttpExchange exchange) {
        this.exchange = exchange;
        this.method   = Method.valueOf(exchange.getRequestMethod());
        this.readBody();
    }

    private void readBody() {
        try {
            this.body = this.exchange.getRequestBody().readAllBytes();
        } catch (IOException e) {
            // If it fails it's just empty
        }
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
}
