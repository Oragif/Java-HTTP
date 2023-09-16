package oragif.jxpress.worker;

import oragif.jxpress.IRouting;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Router implements IWorker, IRouting {
    protected List<IWorker> workers;
    protected HashMap<String, IWorker> layers;

    {
        this.workers = new ArrayList<>();
        this.layers  = new HashMap<>();
    }

    @Override
    public void handle(Request request, Response response) {
        for (IWorker worker: this.workers) {
            worker.handle(request, response);

            if (response.isClosed()) {
                return;
            }
        }
        IWorker layer = this.layers.get(response.getCurrentLayer());
        if (layer != null) layer.handle(request, response);
        if (!response.isClosed()) response.close();
    }

    public void addWorker(IWorker worker) {
        workers.add(worker);
    }
    public void addLayer(String path, RestWorker worker) {
        layers.put(path, worker);
    }


    @Override
    public void get(String path, IWorker method) {
        this.addLayer(path, new RestWorker("GET", method));
    }

    @Override
    public void post(String path, IWorker method) {
        this.addLayer(path, new RestWorker("POST", method));
    }

    @Override
    public void put(String path, IWorker method) {
        this.addLayer(path, new RestWorker("PUT", method));
    }

    @Override
    public void delete(String path, IWorker method) {
        this.addLayer(path, new RestWorker("DELETE", method));
    }
}
