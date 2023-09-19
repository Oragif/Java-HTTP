package oragif.test;

import oragif.jxpress.JXpress;
import oragif.jxpress.worker.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress();
        jXpress.listen(8080);
        jXpress.post("/", ((request, response) -> {
            System.out.print(request.getBody());
        }));
        jXpress.get("/", ((request, response) -> {
            response.send("test");
        }));
        jXpress.get("/test", ((request, response) -> {
            response.send("test-1");
        }));
        jXpress.get("/test/test", ((request, response) -> {
            response.send("test-2");
        }));
        Router router = new Router();
        router.get("/test", ((request, response) -> {
            response.send("test-2");
        }));
        jXpress.use("/test2", router);
    }
}
