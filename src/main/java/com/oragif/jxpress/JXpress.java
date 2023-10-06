package com.oragif.jxpress;

import com.sun.net.httpserver.HttpServer;
import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.http.RequestManager;
import com.oragif.jxpress.routing.IRouting;
import com.oragif.jxpress.routing.Route;
import com.oragif.jxpress.routing.Router;
import com.oragif.jxpress.util.Logger;
import com.oragif.jxpress.worker.IWorker;
import com.oragif.jxpress.worker.RestWorker;
import org.atteo.classindex.ClassIndex;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executor;

public class JXpress implements IRouting {
    private final HttpServer httpServer;
    private final Logger logger;
    private Executor executor;
    private final RequestManager requestManager;
    private int port;

    {
        this.logger = new Logger("JXpress");
    }

    public JXpress() throws IOException {
        this.httpServer = HttpServer.create();
        this.requestManager = new RequestManager();
        this.getAnnotatedRoutes();
    }

    private void getAnnotatedRoutes() {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(Route.class);
        classes.forEach(aClass -> {
            if (Arrays.stream(aClass.getInterfaces()).anyMatch(aClass1 -> aClass1.getName() == IRequestHandler.class.getName())) {
                IRequestHandler requestHandler;
                try {
                    requestHandler = aClass.asSubclass(IRequestHandler.class).newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    return;
                }
                this.requestManager.addEndpoint(
                        this.requestManager.splitPath(aClass.getAnnotation(Route.class).path()),
                        new RestWorker(aClass.getAnnotation(Route.class).method(), requestHandler)
                );
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

    public void stop() {
        this.httpServer.stop(10);
    }

    public void set404(IRequestHandler method) {
        this.requestManager.set404(method);
    }

    public void enableSession() {
        this.requestManager.enableSession();
    }

    public void disableSession() {
        this.requestManager.disableSession();
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
    public void use(String path, IWorker worker) {
        this.requestManager.use(path, worker);
    }

    @Override
    public void use(IWorker worker) {
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
