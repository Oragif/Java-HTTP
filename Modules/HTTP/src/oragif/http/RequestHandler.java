package oragif.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import oragif.http.routing.Route;
import oragif.http.routing.RouteHandler;
import oragif.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements HttpHandler {
    private RouteHandler routeHandler;
    private Logger logger = new Logger("RequestHandler");

    public RequestHandler(RouteHandler routeHandler) {
        this.routeHandler = routeHandler;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        logger.info("Path requested: " + path);

        Route route = this.routeHandler.route(path, exchange);
        if (route != null) {
            logger.info("Found valid path: " + route.toString());
            route.handle(exchange);
            return;
        }

        logger.warn("Invalid path requested: " + path);
        exchange.sendResponseHeaders(404, "404 not found".getBytes(StandardCharsets.UTF_8).length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write("404 not found".getBytes(StandardCharsets.UTF_8));
    }
}
