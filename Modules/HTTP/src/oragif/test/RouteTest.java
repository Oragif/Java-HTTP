package oragif.test;

import oragif.jxpress.routing.Route;
import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;
import oragif.jxpress.worker.Method;

@Route(path = "/routetest", method = Method.GET, provides = "text/plain")
public class RouteTest implements IRequestHandler {
    @Override
    public void handle(Request request, Response response) {
        response.send("test");
    }
}
