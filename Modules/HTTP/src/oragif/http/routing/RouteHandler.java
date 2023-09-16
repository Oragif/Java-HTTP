package oragif.http.routing;

import com.sun.net.httpserver.HttpExchange;
import oragif.http.middleware.Middleware;
import oragif.http.response.HttpResponseBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class handles the routing of HTTP requests.
 */
public class RouteHandler {
    /**
     * This class represents a path in the routing tree.
     */
    private class Path {
        public List<Middleware> middleware;
        public HashMap<String, Path> paths;
        public Route route;

        {
            this.middleware = new ArrayList<Middleware>();
            this.paths = new HashMap<>();
        }
    }

    private final Path pathTree;

    {
        this.pathTree = new Path();
    }

    /**
     * This method splits a path into its components.
     * @param path The path to split.
     * @return The components of the path.
     */
    private String[] pathSplitter(String path) {
        return path.split("/");
    }

    /**
     * This method retrieves the Path object for a given path.
     * @param uriPaths The components of the path.
     * @return The Path object for the path.
     */
    private Path getPath(String[] uriPaths) {
        // If the length of the URI paths is 0, return the root of the path tree
        if (uriPaths.length == 0) return this.pathTree;

        // Get the first path
        Path currentPath = this.pathTree.paths.get(uriPaths[0]);
        // If the path does not exist, return null
        if (currentPath == null) return null;

        // Loop through the remaining paths
        for (int i = 1; i < uriPaths.length; i++) {
            // If the path corresponding to the current component does not exist, return null
            if (currentPath.paths.get(uriPaths[i]) == null) return null;
            // Update the current path to the path corresponding to the current component
            currentPath = currentPath.paths.get(uriPaths[i]);
        }

        return currentPath;
    }

    /**
     * This method retrieves the Route object for a given path.
     * @param uriPaths The components of the path.
     * @return The Route object for the path.
     */
    private Route getRoute(String[] uriPaths) {
        return getPath(uriPaths).route;
    }

    /**
     * This method checks if a path exists in the routing tree.
     * @param paths The components of the path.
     * @return True if the path exists, false otherwise.
     */
    private boolean pathExists(String[] paths) {
        return getPath(paths) != null;
    }

    /**
     * This method retrieves the Path object for a given path, creating it if it does not exist.
     * @param path The path to retrieve or create.
     * @return The Path object for the path.
     */
    private Path findOrCreatePath(String path) {
        String[] pathComponents = this.pathSplitter(path);
        // If the path is empty, return the root of the path tree
        if (pathComponents.length == 0) return this.pathTree;

        // If the first component of the path does not exist in the path tree, create a new path for it
        if (this.pathTree.paths.get(pathComponents[0]) == null) this.pathTree.paths.put(pathComponents[0], new Path());
        Path lastPath;
        // Get the path corresponding to the first component of the path
        Path currentPath = this.pathTree.paths.get(pathComponents[0]);

        // Loop through the remaining components of the path
        for (int i = 1; i < pathComponents.length; i++) {
            lastPath = currentPath;
            // Update the current path to the next path
            currentPath = currentPath.paths.get(pathComponents[i]);
            // If the current path exists, continue to the next component
            if (currentPath != null) continue;

            // If the current path does not exist, create a new path for it
            lastPath.paths.put(pathComponents[i], new Path());
            // Update the current path to the newly created path
            currentPath = lastPath.paths.get(pathComponents[i]);
        }

        return currentPath;
    }

    /**
     * This method adds a listener to a path.
     * @param path The path to add the listener to.
     * @param listener The listener to add.
     */
    public void addListener(String path, Middleware listener) {
        findOrCreatePath(path).middleware.add(listener);
    }

    /**
     * This method adds a route to a path.
     * @param path The path to add the route to.
     * @param route The route to add.
     */
    public void addRoute(String path, Route route) {
        findOrCreatePath(path).route = route;
    }

    /**
     * This method handles the routing of a request.
     * @param path The path of the request.
     * @param exchange The HttpExchange object for the request.
     * @return The Route object for the request.
     */
    public Route route(String path, HttpExchange exchange) {
        String[] uriPaths = this.pathSplitter(path);

        // Trigger the base middleware
        Middleware middleware = triggerMiddleware(this.pathTree, exchange);

        // If middleware is denying and has a redirection path, return the route for the URI paths
        if (middleware != null && middleware.isDenying()) {
            return middleware.getRedirectionPath() != null ? getRoute(uriPaths) : null;
        }

        // If the path does not exist, return null
        if (!this.pathExists(uriPaths)) return null;
        // If there are no URI paths, return the route for the path tree
        if (uriPaths.length == 0) return this.pathTree.route;

        // Get the path for the first URI path
        Path currentPath = this.pathTree.paths.get(uriPaths[0]);

        // Determine if middleware should be triggered
        boolean triggerMiddleware = middleware == null || !middleware.isConsuming();

        // Loop through the URI paths
        for (int i = 0; i < uriPaths.length; i++) {
            // If not the first URI path, get the path for the current URI path
            if (i != 0) currentPath = currentPath.paths.get(uriPaths[i]);

            // If middleware shouldn't be triggered, continue to the next URI path
            if (!triggerMiddleware) continue;

            // Trigger middleware for the current path
            middleware = triggerMiddleware(currentPath, exchange);
            // If middleware is null, continue to the next URI path
            if (middleware == null) continue;

            // Determine if middleware should be triggered for the next URI path
            triggerMiddleware = !middleware.isConsuming();

            // If middleware is denying and has a redirection path, send a 301 response and return null
            if (middleware.isDenying()) {
                if (middleware.getRedirectionPath() == null) return null;
                new HttpResponseBuilder(exchange)
                        .HTTP301(middleware.getRedirectionPath())
                        .sendResponse(null);
                return null;
            }
        }
        // Return the route for the current path
        return currentPath.route;
    }

    /**
     * This method triggers the middleware for a path and exchange.
     * @param currentPath The current path.
     * @param exchange The HttpExchange object for the request.
     * @return The Middleware object for the path and exchange.
     */
    private Middleware triggerMiddleware(Path currentPath, HttpExchange exchange) {
        for (Middleware middleware: currentPath.middleware) {
            middleware.pathListener(exchange);
            if (middleware.isConsuming() || middleware.isDenying()) {
                return middleware;
            }
        }
        return null;
    }
}