package oragif.jxpress;

import com.sun.net.httpserver.HttpServer;
import oragif.jxpress.http.RequestHandler;
import oragif.jxpress.worker.Router;
import oragif.jxpress.worker.Worker;
import oragif.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class JXpress implements IRouting {
    private final HttpServer httpServer;
    private final Logger logger;
    private Executor executor;
    private RequestManager requestManager;

    {
        this.logger = new Logger("JXpress");
    }

    public JXpress() throws IOException {
        this.httpServer = HttpServer.create();
        this.requestManager = new RequestManager();
    }

    public void listen(int port) {
        try {
            this.httpServer.bind(new InetSocketAddress(port), 0);
            this.httpServer.createContext("/", this.requestManager);
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
    public void get(String path, RequestHandler method) {
        this.requestManager.get(path, method);
    }

    @Override
    public void post(String path, RequestHandler method) {
        this.requestManager.post(path, method);
    }

    @Override
    public void put(String path, RequestHandler method) {
        this.requestManager.put(path, method);
    }

    @Override
    public void delete(String path, RequestHandler method) {
        this.requestManager.delete(path, method);
    }

    @Override
    public void use(String path, Router router) { this.requestManager.use(path, router); }
    @Override
    public void use(Worker worker) { this.requestManager.use(worker); }
}
