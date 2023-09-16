package oragif.jxpress;

import com.sun.net.httpserver.HttpServer;
import oragif.logger.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

public class JXpress {
    private final HttpServer httpServer;
    private final Logger logger;
    private Executor executor;

    {
        this.logger = new Logger("JXpress");
    }

    public JXpress() throws IOException {
        this.httpServer = HttpServer.create();
    }

    public void listen(int port) {
        try {
            this.httpServer.bind(new InetSocketAddress(port), 0);
            this.httpServer.createContext("/", new RequestManager());
            this.httpServer.start();

            logger.info("Server started on local address: http://localhost:"+ port);
        } catch (IOException e) {
            logger.error("Error starting on port: " + port + e.getLocalizedMessage());
        }
    }

    public void stop() {
        this.httpServer.stop(10);
    }
}
