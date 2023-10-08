package com.oragif.jxpress.routing;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.worker.Worker;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.HashMap;

public class Router extends Routing {
    protected String root;

    {
        this.middleware = new ArrayList<>();
        this.layers    = new HashMap<>();
        this.endpoints = new HashMap<>();
    }

    /**
     * Recursively print the Route tree to console.
     * @param path The path of the route
     * @param depth The depth of the route in the tree
     * @param port The port number
     */
    public void printRouteTree(String path, int depth, int port) {
        String indent = "";
        if (depth != 0) indent = ("┃" + " ".repeat(3)).repeat(depth - 1) + "┣" + "━".repeat(3);
        String route = path.isEmpty() ? "/" : path;
        int middlewareCount = this.middleware.size();
        System.out.printf("%s┫ %s : Middleware Count: %d%n", indent, route, middlewareCount);

        endpoints.forEach((stringMethodPair, worker) -> {
            String endpointIndent = ("┃" + " ".repeat(3)).repeat(depth);
            System.out.printf("%s┣━━━ http://localhost:%d%s%s : %s : %s%n",
                    endpointIndent, port, path, stringMethodPair.getLeft(), stringMethodPair.getRight(), worker.getClass().getSimpleName());
        });

        layers.forEach((s, router) -> router.printRouteTree(path + s, depth + 1, port));
    }

    public void handle(Request request, Response response) {
        if (response.isClosed()) return;
        if (this.root != "") response.nextLevel();
        this.triggerWorkers(request, response);

        if (triggerEndpoint(request, response)) return;

        triggerNextLayer(request, response);
    }

    private void triggerNextLayer(Request request, Response response) {
        Router layer = this.layers.get(response.getCurrentLeveledPath());
        if (layer != null) {
            layer.handle(request, response);
        }
    }

    private boolean triggerEndpoint(Request request, Response response) {
        if (response.getLevel() == response.getMaxLevel()) {
            Worker endpoint = this.endpoints.get(new ImmutablePair<>(response.getCurrentLeveledPath(), request.getMethod()));
            if (endpoint != null) {
                endpoint.handle(request, response);
            }
            return true;
        }
        return false;
    }

    private void triggerWorkers(Request request, Response response) {
        for (Worker worker: this.middleware) {
            worker.handle(request, response);
            if (response.isClosed()) {
                return;
            }
        }
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
