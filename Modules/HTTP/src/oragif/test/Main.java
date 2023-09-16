package oragif.test;

import oragif.http.HttpServer;
import oragif.jxpress.JXpress;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        JXpress jXpress = new JXpress();
        jXpress.listen(8080);
    }

    private static void HTTPHandlerTest() throws InterruptedException, IOException {
        System.setProperty("javax.net.debug","all");
        HttpServer handler = new HttpServer(8080);
        handler.addRoute("/test", new test("test"));
        handler.addRoute("/test/2", new test("test2"));
        handler.addRoute("/test/3", new test("test3"));
        handler.addRoute("/t", new test("t"));
        handler.addMiddleware("/", new MiddlewareTest("test"));
        handler.addMiddleware("/test/2", new MiddlewareTest("test2", true, false));
        handler.addMiddleware("/test/2", new MiddlewareTest("Shouldn't show", false, false));
        handler.addMiddleware("/test/3", new MiddlewareTest("Denied", false, true));
        handler.start();
        Thread.sleep(1000000000);
    }
}
