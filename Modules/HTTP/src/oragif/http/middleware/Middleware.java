package oragif.http.middleware;

import com.sun.net.httpserver.HttpExchange;

public abstract class Middleware {
    public boolean consumed;
    protected void consume() {
        this.consumed = true;
    }

    /**
     * Listens for when a specific path is triggered.
     * Use consume() to stop all other middleware running afterwards.
     * Beware previous middlewares will always run.
     * @param exchange Current exchange with client
     */
    abstract void pathListener(HttpExchange exchange);
}