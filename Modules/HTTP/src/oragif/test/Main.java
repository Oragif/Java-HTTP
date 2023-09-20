package oragif.test;

import oragif.jxpress.JXpress;
import oragif.jxpress.routing.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress();
        jXpress.post("/", ((request, response) -> {
            System.out.print(request.getJsonBody(test.class).test);
        }));
        // jXpress.use((request, response) -> {
        //     System.out.println("Middleware");
        // });
        jXpress.get("/", ((request, response) -> {
            response.send("/");
        }));
        jXpress.get("/test", ((request, response) -> {
            response.send("/test");
        }));
        jXpress.get("/test/test", ((request, response) -> {
            response.send("/test/test");
        }));
        Router router = new Router();
        router.get("/test2", ((request, response) -> {
            response.send("/test/test2");
        }));
        jXpress.use("/test", router);
        jXpress.listen(8080);
    }
}
