package oragif.test;

import oragif.jxpress.JXpress;
import oragif.jxpress.routing.Router;
import oragif.jxpress.worker.middleware.FileReader;
import oragif.jxpress.worker.middleware.WebFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress();
        jXpress.post("/", ((request, response) -> {
            System.out.print(request.getJsonBody(test.class).test);
        }));

        jXpress.publicFolder("website");

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

        jXpress.use("/filetest", new FileReader(Path.of("website/test.html")));
        jXpress.printRouteTree();
        jXpress.listen(8080);
    }
}
