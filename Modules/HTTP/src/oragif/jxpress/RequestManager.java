package oragif.jxpress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;
import oragif.jxpress.worker.Router;

public class RequestManager extends Router implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Request request = new Request(exchange);
        Response response = new Response(exchange);
        this.handle(request, response);
    }
}
