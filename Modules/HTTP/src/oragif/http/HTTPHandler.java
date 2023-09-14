package oragif.http;

import com.sun.net.httpserver.*;
import oragif.http.middleware.MiddlewareHandler;
import oragif.http.routing.Route;
import oragif.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HTTPHandler {
    private final Logger logger = new Logger("HTTP-Server:unknown");
    private boolean isRunning;
    private MiddlewareHandler middlewareHandler;
    private InetSocketAddress socketAddress;
    private final HttpServer httpServer;
    {
        try {
            this.httpServer = HttpServer.create();
        } catch (IOException e) {
            logger.error("HTTP server failed to create, bye!");
            throw new RuntimeException(e);
        }
    }

    public HTTPHandler() {
        this.isRunning = false;
    }

    public HTTPHandler(int port) {
        this();
        this.listen(port);
    }

    public boolean isRunning() { return this.isRunning; }

    public boolean listen(int port) {
        if (this.isRunning) {
            return false;
        }
        this.socketAddress = new InetSocketAddress(port);
        logger.setTag("HTTP-Server:" + port);
        return true;
    }

    public boolean start() {
        try {
            this.httpServer.bind(this.socketAddress, 0);
            this.httpServer.setExecutor(null);
            this.httpServer.start();
            this.isRunning = true;
            logger.info("Server started on local address: http://localhost:"+ this.socketAddress.getPort());
        } catch (IOException e) {
            logger.error("Error starting on port: " + this.socketAddress.getPort() + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public void stop() {
        logger.info("Server/" + this.socketAddress.getPort() + " is shutting down in 10 seconds");
        this.httpServer.stop(10);
        this.isRunning = false;
    }

    public void addRoute(String path, Route route) { this.httpServer.createContext(path, route); }

    public void removeRoute(String path) { this.httpServer.removeContext(path); }
}
