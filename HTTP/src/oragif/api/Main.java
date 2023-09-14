package oragif.api;

import oragif.http.HTTPHandler;

import java.io.Console;

public class Main {

    private static HTTPHandler handler;
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("javax.net.debug","all");
        handler = new HTTPHandler(8080);
        handler.addRoute("/test", new test());
        handler.start();
        Thread.sleep(1000000000);
    }
}
