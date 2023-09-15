package oragif.test;

import oragif.http.HTTPHandler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HTTPHandlerTest();
    }

    private static void HTTPHandlerTest() throws InterruptedException {
        System.setProperty("javax.net.debug","all");
        HTTPHandler handler = new HTTPHandler(8080);
        handler.addRoute("/test", new test("test"));
        handler.start();
        Thread.sleep(1000000000);
    }
}
