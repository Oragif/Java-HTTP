package com.oragif.test;

import com.oragif.jxpress.routing.Route;
import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.worker.Method;

@Route(path = "/routetest", method = Method.GET, provides = "text/plain")
public class RouteTest implements IRequestHandler {
    @Override
    public void handle(Request request, Response response) {
        response.send("test");
    }
}
