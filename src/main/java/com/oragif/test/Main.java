package com.oragif.test;

import com.oragif.jxpress.JXpress;
import com.oragif.jxpress.middleware.Session;
import com.oragif.jxpress.routing.Router;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JXpress jXpress = new JXpress();
        jXpress.get("/test", (request, response) -> {
            String test = "test";
            response.send("Test: " + test);
        });
        jXpress.get("/test1", (request, response) -> {response.send("Test 1");});
        jXpress.get("/test2", (request, response) -> {response.send("Test 2");});
        jXpress.get("/test3", (request, response) -> {response.send("Test 3");});
        Router router = new Router();
        router.get("/test", (request, response) -> {response.send("/router Test");});
        router.get("/test1", (request, response) -> {response.send("/router Test 1");});
        router.get("/test2", (request, response) -> {response.send("/router Test 2");});
        router.get("/test3", (request, response) -> {response.send("/router Test 3");});
        Router router2 = new Router();
        router2.get("/test", (request, response) -> {response.send("/second Test");});
        router2.get("/test1", (request, response) -> {response.send("/second Test 1");});
        router2.get("/test2", (request, response) -> {response.send("/second Test 2");});
        router2.get("/test3", (request, response) -> {response.send("/second Test 3");});
        router2.get("/", (request, response) -> {response.send("Test: empty path");});
        router.use("/second", router2);
        jXpress.use("/router", router);
        jXpress.set404((request, response) -> {
            response.send("Hippiti hoppiti the path is not a property");
        });
        jXpress.listen(8080);
        jXpress.printRouteTree();
    }
}
