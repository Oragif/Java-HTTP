package com.oragif.jxpress.routing;

import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.worker.Worker;
import com.oragif.jxpress.worker.Method;
import com.oragif.jxpress.worker.RestWorker;
import com.oragif.jxpress.worker.middleware.FileReader;
import com.oragif.jxpress.worker.middleware.WebFolder;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.StreamSupport;

public class Routing {
    protected List<Worker> middleware;
    protected HashMap<String, Router> layers;
    protected HashMap<Pair<String, Method>, Worker> endpoints;

    public static String[] splitPath(String pathString) {
        if (pathString.compareTo("/") == 0) return new String[]{"/"};
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(path1 -> "/".concat(path1.toString())).toArray(String[]::new);
    }

    protected void addEndpoint(String path, Worker restWorker) {
        this.addEndpoint(splitPath(path), restWorker);
    }

    public void addEndpoint(String[] paths, Worker restWorker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addEndpoint(Arrays.copyOfRange(paths, 1, paths.length), restWorker);
            this.addLayer(paths[0], router);
            return;
        }
        this.endpoints.put(new ImmutablePair<>(paths[0], restWorker.getMethod()), restWorker);
    }


    protected void addLayer(String[] paths, Router worker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addLayer(Arrays.copyOfRange(paths, 1, paths.length), worker);
            this.addLayer(paths[0], router);
            return;
        }
        this.addRouter(paths[0], worker);
    }

    protected void addLayer(String path, Router worker) {
        this.addLayer(splitPath(path), worker);
    }

    protected void addWorker(Worker worker) {
        middleware.add(worker);
    }
    protected void addWorker(String path, Worker worker) {
        this.addWorker(splitPath(path), worker);
    }
    public void addWorker(String[] paths, Worker worker) {
        if (paths.length > 1) {
            Router router = new Router();
            router.addWorker(Arrays.copyOfRange(paths, 1, paths.length), worker);
            this.addLayer(paths[0], router);
            return;
        }
        this.addWorker(worker);
    }
    
    protected void addRouter(String path, Router worker) {
        Router router = this.layers.get(path);
        if (router == null) {
            layers.put(path, worker);
            return;
        }
        // If router already exists at that path, add all new entries to the old router
        worker.layers.forEach((router::addLayer));
        worker.endpoints.forEach(((p, r) -> router.addEndpoint(p.getLeft(), r)));
        worker.middleware.forEach(router::addWorker);
    }
    
    public void get(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.GET, method));
    }

    public void post(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.POST, method));
    }

    public void put(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.PUT, method));
    }

    public void delete(String path, IRequestHandler method) {
        this.addEndpoint(path, new RestWorker(Method.DELETE, method));
    }

    public void use(String path, Router router) {
        this.addLayer(path, router);
        router.setRoot(path);
    }

    public void use(String path, Worker worker) {
        this.addWorker(path, worker);
    }
   
    public void use(Worker worker) { this.addWorker(worker); }
   
    public void webFolder(String path, String folderPath) {
        HashMap<String, FileReader> files = WebFolder.mapFolder(path, new File(folderPath));
        files.forEach((filePath, fileReader) -> this.use(filePath, fileReader));
    }
   
    public void publicFolder(String folderPath) {
        webFolder("/", folderPath);
    }
}
