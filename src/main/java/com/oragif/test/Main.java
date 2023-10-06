package com.oragif.test;

import com.oragif.jxpress.JXpress;
import com.oragif.jxpress.routing.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress();
        jXpress.get("/test", (request, response) -> {response.send("Test");});
        jXpress.get("/test1", (request, response) -> {response.send("Test");});
        jXpress.get("/test2", (request, response) -> {response.send("Test");});
        jXpress.get("/test3", (request, response) -> {response.send("Test");});
        Router router = new Router();
        router.get("/test", (request, response) -> {response.send("Test");});
        router.get("/test1", (request, response) -> {response.send("Test");});
        router.get("/test2", (request, response) -> {response.send("Test");});
        router.get("/test3", (request, response) -> {response.send("Test");});
        Router router2 = new Router();
        router2.get("/test", (request, response) -> {response.send("Test");});
        router2.get("/test1", (request, response) -> {response.send("Test");});
        router2.get("/test2", (request, response) -> {response.send("Test");});
        router2.get("/test3", (request, response) -> {response.send("Test");});
        router.use("/second", router2);
        jXpress.use("/router", router);
        jXpress.set404((request, response) -> {
            response.send("Hippiti hoppiti the path is not a property");
        });
        jXpress.enableSession();
        jXpress.listen(8080);
        jXpress.printRouteTree();
    }
}
