package oragif.api;

import oragif.http.response.HttpResponseBuilder;
import oragif.http.routing.Route;

public class test extends Route {
    @Override
    public void get(HttpResponseBuilder responseBuilder) {
        responseBuilder.HTTP200().sendResponse("Yooo");
    }
}
