package com.oragif.jxpress;

import com.oragif.jxpress.routing.Routing;
import com.oragif.jxpress.middleware.Middleware;
import com.sun.net.httpserver.HttpServer;
import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.http.RequestManager;
import com.oragif.jxpress.routing.Route;
import com.oragif.jxpress.routing.Router;
import com.oragif.jxpress.util.Logger;
import com.oragif.jxpress.worker.Worker;
import com.oragif.jxpress.worker.RestWorker;
import org.atteo.classindex.ClassIndex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

public class JXpress extends Routing {
    private final HttpServer httpServer;
    private final Logger logger;
    private final RequestManager requestManager;
    private Executor executor;
    private int port;

    {
        this.logger = new Logger("JXpress");
    }

    public JXpress() throws IOException {
        this.httpServer = HttpServer.create();
        this.requestManager = new RequestManager();
    }

    private void getAnnotatedRoutes() {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(Route.class);
        classes.forEach(aClass -> {
            if (Arrays.stream(aClass.getInterfaces()).anyMatch(aClass1 -> aClass1.isAssignableFrom(IRequestHandler.class))) {
                IRequestHandler requestHandler;
                try {
                    requestHandler = aClass.asSubclass(IRequestHandler.class).newInstance();
                } catch (InstantiationException | IllegalAccessException e) { return; }
                this.requestManager.addEndpoint(
                        this.requestManager.splitPath(aClass.getAnnotation(Route.class).path()),
                        new RestWorker(aClass.getAnnotation(Route.class).method(), requestHandler)
                );
            }
        });
    }

    private void getAnnotatedMiddleware() {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(Middleware.class);
        classes.forEach(aClass -> {
            if (aClass.getSuperclass() == Worker.class) {
                Worker worker;
                try {
                    worker = aClass.asSubclass(Worker.class).newInstance();
                } catch (InstantiationException | IllegalAccessException e) { return; }
                this.requestManager.use(aClass.getAnnotation(Middleware.class).path(), worker);
            }
        });
    }

    public void printRouteTree() {
        this.requestManager.printRouteTree("", 0, this.port);
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    public void listen(int port) {
        try {
            // Load dynamic routes
            this.getAnnotatedRoutes();
            this.getAnnotatedMiddleware();

            this.httpServer.bind(new InetSocketAddress(port), 0);
            this.httpServer.createContext("/", this.requestManager);
            this.httpServer.setExecutor(this.executor);
            this.httpServer.start();
            this.port = port;
            logger.info("Server started on local address: http://localhost:"+ port);
        } catch (IOException e) {
            logger.error("Error starting on port: " + port + e.getLocalizedMessage());
        }
    }

    /**
     * Will give any connections 10 seconds before forcefully shutting down
     */
    public void stop() {
        this.httpServer.stop(10);
    }

    public void set404(IRequestHandler method) {
        this.requestManager.set404(method);
    }

    @Override
    public void get(String path, IRequestHandler method) {
        this.requestManager.get(path, method);
    }

    @Override
    public void post(String path, IRequestHandler method) {
        this.requestManager.post(path, method);
    }

    @Override
    public void put(String path, IRequestHandler method) {
        this.requestManager.put(path, method);
    }

    @Override
    public void delete(String path, IRequestHandler method) {
        this.requestManager.delete(path, method);
    }

    @Override
    public void use(String path, Router router) {
        this.requestManager.use(path, router);
    }

    @Override
    public void use(String path, Worker worker) {
        this.requestManager.use(path, worker);
    }

    @Override
    public void use(Worker worker) {
        this.requestManager.use(worker);
    }

    @Override
    public void webFolder(String path, String folderPath) {
        this.requestManager.webFolder(path, folderPath);
    }

    @Override
    public void publicFolder(String folderPath) {
        this.requestManager.publicFolder(folderPath);
    }
}
