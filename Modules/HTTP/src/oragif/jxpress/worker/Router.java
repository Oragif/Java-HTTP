package oragif.jxpress.worker;

import oragif.jxpress.http.RequestHandler;
import oragif.jxpress.IRouting;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Router implements IWorker, IRouting {
    protected List<IWorker> workers;
    protected HashMap<String, Router> layers;
    protected HashMap<String, RestWorker> restWorkers;
    protected String root;

    {
        this.workers     = new ArrayList<>();
        this.layers      = new HashMap<>();
        this.restWorkers = new HashMap<>();
    }

    @Override
    public void handle(Request request, Response response) {
        if (this.root != "") response.nextLevel();
        this.triggerWorkers(request, response);
        this.triggerNextLayer(request, response);
    }

    private void triggerNextLayer(Request request, Response response) {
        for (int i = response.getMaxLevel(); i >= response.getLevel(); i--) {
            String leveledPath = response.getLeveledPath(response.getLevel(), i);

            Router layer = this.layers.get(leveledPath);
            if (layer != null) {
                layer.handle(request, response);
                return;
            }

            RestWorker restWorker = this.restWorkers.get(response.getMethod() + leveledPath);
            if (restWorker != null) {
                restWorker.handle(request, response);
                return;
            }
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
        layers.put(path, worker);
    }
    private void addRestWorker(String path, RestWorker restWorker) { this.restWorkers.put(restWorker.getRequestMethod() + path, restWorker); }

    public void setRoot(String root) {
        this.root = root;
    }


    @Override
    public void get(String path, RequestHandler method) {
        this.addRestWorker(path, new RestWorker("GET", method));
    }

    @Override
    public void post(String path, RequestHandler method) {
        this.addRestWorker(path, new RestWorker("POST", method));
    }

    @Override
    public void put(String path, RequestHandler method) {
        this.addRestWorker(path, new RestWorker("PUT", method));
    }

    @Override
    public void delete(String path, RequestHandler method) {
        this.addRestWorker(path, new RestWorker("DELETE", method));
    }

    @Override
    public void use(String path, Router router) {
        this.addLayer(path, router);
        router.setRoot(path);
    }
    @Override
    public void use(Worker worker) { this.addWorker(worker); }
}
