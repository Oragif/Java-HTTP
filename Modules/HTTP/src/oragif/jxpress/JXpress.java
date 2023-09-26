package oragif.jxpress;

import com.sun.net.httpserver.HttpServer;
import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.RequestManager;
import oragif.jxpress.routing.IRouting;
import oragif.jxpress.routing.Route;
import oragif.jxpress.routing.Router;
import oragif.jxpress.worker.IWorker;
import oragif.jxpress.worker.RestWorker;
import oragif.logger.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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

    public JXpress(String... packages) throws IOException {
        this.httpServer = HttpServer.create();
        this.requestManager = new RequestManager();
        this.getAnnotatedRoutes(packages);
    }

    public JXpress() throws IOException {
        this(null);
    }

    private void getAnnotatedRoutes(String[] packages) {
        ConfigurationBuilder configurationBuilder;
        if (packages == null) {
            Collection<URL> allPackagePrefixes = Arrays.stream(Package.getPackages()).map(p -> p.getName())
                    .map(s -> s.split("\\.")[0]).distinct().map(s -> ClasspathHelper.forPackage(s)).reduce((c1, c2) -> {
                        Collection<URL> c3 = new HashSet<>();
                        c3.addAll(c1);
                        c3.addAll(c2);
                        return c3;
                    }).get();
            configurationBuilder = new ConfigurationBuilder().addUrls(allPackagePrefixes);
        } else {
            configurationBuilder = new ConfigurationBuilder().forPackages(packages);
        }
        Reflections reflections = new Reflections(configurationBuilder);
        Set<Class<?>> routes = reflections.getTypesAnnotatedWith(Route.class);
        routes.stream().forEach(aClass -> {
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
            }
        );
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
