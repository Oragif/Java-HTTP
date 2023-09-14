package oragif.http.middleware;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiddlewareHandler {
    private final Map<String, Path> paths = new HashMap<>();

    private String[] pathSplitter(String path) {
        return path.split("/");
    }

    private boolean pathExists(String[] paths) {
        Path currentPath = this.paths.get(paths[0]);

        for (int i = 1; i < paths.length; i++) {
            if (currentPath.paths.get(paths[i]) == null) return false;
        }
        return true;
    }

    public void addListener(String path, Middleware listener) {
        String[] paths = this.pathSplitter(path);

        if (this.paths.get(paths[0]) == null) this.paths.put(paths[0], new Path());
        Path lastPath;
        Path currentPath = this.paths.get(paths[0]);

        for (int i = 1; i < paths.length; i++) {
            lastPath = currentPath;
            currentPath = currentPath.paths.get(paths[i]);
            if (currentPath != null) continue;

            lastPath.paths.put(paths[i], new Path());
            currentPath = lastPath.paths.get(paths[i]);
        }

        currentPath.middleware.add(listener);
    }

    public void triggerEvent(String path, HttpExchange exchange) {
        String[] paths = this.pathSplitter(path);

        if (!this.pathExists(paths)) return;

        Path currentPath = this.paths.get(paths[0]);

        mainLoop:
        for (int i = 0; i < paths.length; i++) {
            if (i != 0) currentPath = currentPath.paths.get(paths[i]);
            for (Middleware event: currentPath.middleware) {
                event.pathListener(exchange);
                if (event.consumed) {
                    break mainLoop;
                }
            }
        }
    }

    private class Path {
        public List<Middleware> middleware = new ArrayList<Middleware>();
        public Map<String, Path> paths = new HashMap<>();
    }
}
