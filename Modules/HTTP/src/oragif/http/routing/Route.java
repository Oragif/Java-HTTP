package oragif.http.routing;

import com.sun.net.httpserver.*;
import oragif.http.response.HttpResponseBuilder;

import java.io.IOException;

public abstract class Route implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder(exchange);
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET"    -> this.get(responseBuilder);
            case "POST"   -> this.post(responseBuilder);
            case "PUT"    -> this.put(responseBuilder);
            case "DELETE" -> this.delete(responseBuilder);
            case "PATCH"  -> this.patch(responseBuilder);
            default       -> this.unknownMethod(method, responseBuilder);
        }
        exchange.close();
    }

    private void unknownMethod(String method, HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP501().sendResponse(null);
    }

    public void get(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP405().sendResponse(null);
    }

    public void post(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP405().sendResponse(null);
    }

    public void put(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP405().sendResponse(null);
    }

    public void delete(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP405().sendResponse(null);
    }

    public void patch(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP405().sendResponse(null);
    }
}
