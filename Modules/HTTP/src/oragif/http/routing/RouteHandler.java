package oragif.http.routing;

import com.sun.net.httpserver.HttpExchange;
import oragif.http.middleware.Middleware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteHandler {
    private final Map<String, Path> paths = new HashMap<>();

    private String[] pathSplitter(String path) {
        return path.split("/");
    }

    private Path getPath(String[] uriPaths) {
        Path currentPath = this.paths.get(uriPaths[0]);
        if (currentPath == null) return null;

        for (int i = 1; i < uriPaths.length; i++) {
            if (currentPath.paths.get(uriPaths[i]) == null) return null;
            currentPath = currentPath.paths.get(uriPaths[i]);
        }

        return currentPath;
    }

    private Route getRoute(String[] uriPaths) {
        return getPath(uriPaths).route;
    }

    private boolean pathExists(String[] paths) {
        return getPath(paths) != null;
    }

    private Path findOrCreatePath(String path) {
        String[] uriPaths = this.pathSplitter(path);

        if (paths.get(uriPaths[0]) == null) this.paths.put(uriPaths[0], new Path());
        Path lastPath;
        Path currentPath = this.paths.get(uriPaths[0]);

        for (int i = 1; i < uriPaths.length; i++) {
            lastPath = currentPath;
            currentPath = currentPath.paths.get(uriPaths[i]);
            if (currentPath != null) continue;

            lastPath.paths.put(uriPaths[i], new Path());
            currentPath = lastPath.paths.get(uriPaths[i]);
        }

        return currentPath;
    }

    public void addListener(String path, Middleware listener) {
        findOrCreatePath(path).middleware.add(listener);
    }

    public void addRoute(String path, Route route) {
        findOrCreatePath(path).route = route;
    }

    public Route route(String path, HttpExchange exchange) {
        String[] uriPaths = this.pathSplitter(path);

        if (!this.pathExists(uriPaths)) return null;

        Path currentPath = this.paths.get(uriPaths[0]);

        boolean triggerMiddleware = true;
        for (int i = 0; i < uriPaths.length; i++) {
            if (i != 0) currentPath = currentPath.paths.get(uriPaths[i]);

            if (!triggerMiddleware) continue;

            Middleware middleware = triggerMiddleware(currentPath, exchange);
            if (middleware == null) continue;

            if (middleware.isConsuming()) { triggerMiddleware = false; }
            if (middleware.isDenying()) {
                if (middleware.getRedirectionPath() == null) return null;
                return getRoute(middleware.getRedirectionPath());
            }
        }
        return currentPath.route;
    }

    private Middleware triggerMiddleware(Path currentPath, HttpExchange exchange) {
        for (Middleware middleware: currentPath.middleware) {
            middleware.pathListener(exchange);
            if (middleware.isConsuming() || middleware.isDenying()) {
                return middleware;
            }
        }
        return null;
    }

    private class Path {
        public List<Middleware> middleware = new ArrayList<Middleware>();
        public Map<String, Path> paths = new HashMap<>();
        public Route route;
    }
}
