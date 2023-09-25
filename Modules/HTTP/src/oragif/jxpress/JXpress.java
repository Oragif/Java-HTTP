package oragif.jxpress;

import com.sun.net.httpserver.HttpServer;
import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.RequestManager;
import oragif.jxpress.routing.IRouting;
import oragif.jxpress.routing.Router;
import oragif.jxpress.worker.IWorker;
import oragif.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class JXpress implements IRouting {
    private final HttpServer httpServer;
    private final Logger logger;
    private Executor executor;
    private final RequestManager requestManager;

    {
        this.logger = new Logger("JXpress");
    }

    public JXpress() throws IOException {
        this.httpServer = HttpServer.create();
        this.requestManager = new RequestManager();
    }

    public void printRouteTree() {
        this.requestManager.printRouteTree("", 0);
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

            logger.info("Server started on local address: http://localhost:"+ port);
        } catch (IOException e) {
            logger.error("Error starting on port: " + port + e.getLocalizedMessage());
        }
    }

    public void stop() {
        this.httpServer.stop(10);
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
