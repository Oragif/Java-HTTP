package oragif.http.middleware;

import com.sun.net.httpserver.HttpExchange;

public abstract class Middleware {
    private boolean consumed;
    private boolean denying;
    private String redirectionPath;
    protected void consume() {
        this.consumed = true;
    }
    protected void deny() { this.denying = true; }

    public boolean isConsuming() {
        return this.consumed;
    }

    public boolean isDenying() {
        return this.denying;
    }

    public String getRedirectionPath() {
        return this.redirectionPath;
    }

    protected void setRedirectionPath(String path) {
        this.redirectionPath = path;
    }

    /**
     * Listens for when a specific path is triggered.
     * Use consume() to stop all other middleware running afterwards.
     * Beware previous middlewares will always run.
     * @param exchange Current exchange with client
     */
    public abstract void pathListener(HttpExchange exchange);
}