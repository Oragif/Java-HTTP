package oragif.http;

import com.sun.net.httpserver.*;
import oragif.http.routing.RouteHandler;
import oragif.http.routing.Route;
import oragif.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class HTTPHandler {
    private final Logger logger = new Logger("HTTP-Server:unknown");
    private boolean isRunning;
    private RouteHandler routeHandler;
    private InetSocketAddress socketAddress;
    private final HttpServer httpServer;
    private int handlerThreads;

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
        this.routeHandler = new RouteHandler();
    }

    public HTTPHandler(int port) {
        this();
        this.listen(port);
    }

    public HTTPHandler(int port, int handlerThreads) {
        this();
        this.listen(port);
        this.setHandlerThreads(handlerThreads);
    }

    public boolean isRunning() { return this.isRunning; }

    public boolean setHandlerThreads(int handlerThreads) {
        if (this.isRunning) { return false; }
        this.handlerThreads = handlerThreads;

        return true;
    }

    public boolean listen(int port) {
        if (this.isRunning) { return false; }

        this.socketAddress = new InetSocketAddress(port);
        logger.setTag("HTTP-Server:" + port);
        return true;
    }

    private void setupThreads() {
        if (this.handlerThreads > 0) {
            this.httpServer.setExecutor(Executors.newFixedThreadPool(this.handlerThreads));
        } else {
            this.httpServer.setExecutor(null);
        }
    }

    public boolean start() {
        if (this.socketAddress == null) {
            logger.error("No port specified, can't launch Http server");
            return false;
        }

        try {
            this.httpServer.bind(this.socketAddress, 0);
            this.setupThreads();
            this.httpServer.createContext("/", new RequestHandler(this.routeHandler));
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

    public void addRoute(String path, Route route) { this.routeHandler.addRoute(path, route); }

    /**
     * @// TODO: 15/09/2023  Fix this function
     * @param path
     */
    public void removeRoute(String path) { this.httpServer.removeContext(path); }
}
