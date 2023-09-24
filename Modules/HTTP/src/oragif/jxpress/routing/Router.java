package oragif.jxpress.routing;

import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;
import oragif.jxpress.worker.IWorker;
import oragif.jxpress.worker.Method;
import oragif.jxpress.worker.RestWorker;
import oragif.jxpress.worker.middleware.WebFolder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public class Router implements IWorker, IRouting {
    // Middleware
    protected List<IWorker> workers;
    // Routers
    protected HashMap<String, Router> layers;
    // Endpoints <Path, Method>
    protected HashMap<Pair<String, Method>, IWorker> endpoints;
    protected String root;
    protected Method method;

    {
        this.workers   = new ArrayList<>();
        this.layers    = new HashMap<>();
        this.endpoints = new HashMap<>();
        this.method    = Method.ROUTER;
    }

    public void printRouteTree(String path, int depth) {
        System.out.println("-".repeat(depth * 3) + "   Base: " + path + " - Middleware Count: " + this.workers.size());
        endpoints.forEach((stringMethodPair, worker) -> System.out.println("-".repeat(depth * 3) + "      " + path + stringMethodPair.getLeft() + " : " + stringMethodPair.getRight() + " : " + worker.getClass().getName()));
        layers.forEach((s, router) -> {
            System.out.println("");
            router.printRouteTree(path + s, depth + 1);
        });
    }

    private static String[] splitPath(String pathString) {
        if (pathString.compareTo("/") == 0) return new String[]{"/"};
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(path1 -> "/".concat(path1.toString())).toArray(String[]::new);
    }

    @Override
    public void handle(Request request, Response response) {
        if (response.isClosed()) return;
        if (this.root != "") response.nextLevel();
        this.triggerWorkers(request, response);
        if (response.getLevel() == response.getMaxLevel()) {
            IWorker endpoint = this.endpoints.get(new ImmutablePair<>(response.getCurrentLeveledPath(), request.getMethod()));
            if (endpoint != null) {
                endpoint.handle(request, response);
            }
            return;
        }

        Router layer = this.layers.get(response.getCurrentLeveledPath());
        if (layer != null) {
            layer.handle(request, response);
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    private void triggerWorkers(Request request, Response response) {
        for (IWorker worker: this.workers) {
            worker.handle(request, response);
            if (response.isClosed()) {
                return;
            }
        }
    }

    private void addWorker(IWorker worker) {
        workers.add(worker);
    }

    private void addRouter(String path, Router worker) {
        Router router = this.layers.get(path);
        if (router == null) {
            layers.put(path, worker);
            return;
        }
        // If router already exists at that path, add all new entries to the old router
        worker.layers.forEach((router::addLayer));
        worker.endpoints.forEach(((p, r) -> router.addEndpoint(p.getLeft(), r)));
        worker.workers.forEach(router::addWorker);
    }

    private void addLayer(String[] paths, Router worker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addLayer(Arrays.copyOfRange(paths, 1, paths.length), worker);
            this.addLayer(paths[0], router);
            return;
        }
        this.addRouter(paths[0], worker);
    }

    private void addLayer(String path, Router worker) {
        this.addLayer(splitPath(path), worker);
    }
    private void addEndpoint(String path, IWorker restWorker) {
       this.addEndpoint(splitPath(path), restWorker);
    }

    private void addEndpoint(String[] paths, IWorker restWorker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addEndpoint(Arrays.copyOfRange(paths, 1, paths.length), restWorker);
            this.addLayer(paths[0], router);
            return;
        }
        this.endpoints.put(new ImmutablePair<>(paths[0], restWorker.getMethod()), restWorker);
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public void get(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.GET, method));
    }

    @Override
    public void post(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.POST, method));
    }

    @Override
    public void put(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.PUT, method));
    }

    @Override
    public void delete(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.DELETE, method));
    }

    @Override
    public void use(String path, Router router) {
        this.addLayer(path, router);
        router.setRoot(path);
    }

    @Override
    public void use(String path, IWorker worker) {
        this.addEndpoint(path, worker);
    }

    @Override
    public void use(IWorker worker) { this.addWorker(worker); }

    @Override
    public void webFolder(String path, String folderPath) {
        WebFolder.mapFolder(path, new File(folderPath), this);
    }

    @Override
    public void publicFolder(String folderPath) {
        WebFolder.mapFolder("/", new File(folderPath), this);
    }
}
