package oragif.jxpress.routing;

import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;
import oragif.jxpress.worker.IWorker;
import oragif.jxpress.worker.RestWorker;
import oragif.jxpress.worker.Worker;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
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
    protected HashMap<Pair<String, String>, RestWorker> restWorkers;
    protected String root;

    {
        this.workers     = new ArrayList<>();
        this.layers      = new HashMap<>();
        this.restWorkers = new HashMap<>();
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
            RestWorker restWorker = this.restWorkers.get(new ImmutablePair<>(response.getCurrentLeveledPath(), request.getMethod()));
            if (restWorker != null) {
                restWorker.handle(request, response);
            }
            return;
        }

        Router layer = this.layers.get(response.getCurrentLeveledPath());
        if (layer != null) {
            layer.handle(request, response);
        }
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
    private void addLayer(String path, Router worker) {
        Router router = this.layers.get(path);
        if (router == null) {
            layers.put(path, worker);
            return;
        }
        // If router already exists at that path, add all new entries to the old router
        worker.layers.forEach((router::addLayer));
        worker.restWorkers.forEach(((p, r) -> router.addRestWorker(p.getLeft(), r)));
        worker.workers.forEach(router::addWorker);
    }
    private void addRestWorker(String path, RestWorker restWorker) {
       this.addRestWorker(splitPath(path), restWorker);
    }

    private void addRestWorker(String[] paths, RestWorker restWorker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addRestWorker(Arrays.copyOfRange(paths, 1, paths.length), restWorker);
            this.addLayer(paths[0], router);
            return;
        }
        this.restWorkers.put(new ImmutablePair<>(paths[0], restWorker.getRequestMethod()), restWorker);
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public void get(String path, IRequestHandler method) {
        this.addRestWorker(path, new RestWorker("GET", method));
    }

    @Override
    public void post(String path, IRequestHandler method) {
        this.addRestWorker(path, new RestWorker("POST", method));
    }

    @Override
    public void put(String path, IRequestHandler method) {
        this.addRestWorker(path, new RestWorker("PUT", method));
    }

    @Override
    public void delete(String path, IRequestHandler method) {
        this.addRestWorker(path, new RestWorker("DELETE", method));
    }

    @Override
    public void use(String path, Router router) {
        this.addLayer(path, router);
        router.setRoot(path);
    }
    @Override
    public void use(IWorker worker) { this.addWorker(worker); }
}
