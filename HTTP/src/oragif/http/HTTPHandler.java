package oragif.http;

import com.sun.net.httpserver.*;
import oragif.http.routing.Route;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HTTPHandler {
    private static final Map<Integer, HTTPHandler> handlers;
    private static int handlerCount;
    private static int nextId;
    private static final Logger logger = Logger.getLogger("HTTP-Server");
    static {
        handlers = new HashMap<Integer, HTTPHandler>();
        handlerCount = 0;
        nextId = 0;
    }
    private final int id;
    private boolean isRunning;
    private InetSocketAddress socketAddress;
    private final HttpServer httpServer;
    {
        try {
            this.httpServer = HttpServer.create();
        } catch (IOException e) {
            logger.severe("HTTP server failed to create, bye!");
            throw new RuntimeException(e);
        }
    }

    public HTTPHandler() {
        this.isRunning = false;
        handlers.put(nextId, this);
        this.id = nextId;
        nextId++;
        handlerCount++;
    }

    public HTTPHandler(int port) {
        this();
        this.socketAddress = new InetSocketAddress(port);
    }

    public static int getHandlerCount() {
        return handlerCount;
    }

    public static Map<Integer, HTTPHandler> getAllHandlers() {
        return handlers;
    }

    public static void stopAll() { handlers.forEach((integer, httpHandler) -> httpHandler.stop()); }

    public static void destroyAll() {
        handlers.forEach((integer, httpHandler) -> httpHandler.destroy());
    }

    public int getId() { return this.id; }

    public boolean isRunning() { return this.isRunning; }

    public boolean listen(int port) {
        if (this.isRunning) {
            return false;
        }
        this.socketAddress = new InetSocketAddress(port);

        return true;
    }

    public boolean start() {
        try {
            this.httpServer.bind(this.socketAddress, 0);
            this.httpServer.setExecutor(null);
            this.httpServer.start();
            this.isRunning = true;
        } catch (IOException e) {
            logger.severe("Error starting on port: " + this.socketAddress.getPort() + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    public void stop() {
        logger.info("Server/" + this.socketAddress.getPort() + " is shutting down in 10 seconds");
        this.httpServer.stop(10);
        this.isRunning = false;
    }

    public void destroy() {
        if (this.isRunning) this.stop();
        handlers.remove(this.id);
        handlerCount--;
    }

    public void addRoute(String path, Route route) { this.httpServer.createContext(path, route); }

    public void removeRoute(String path) { this.httpServer.removeContext(path); }
}
