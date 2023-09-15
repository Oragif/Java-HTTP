package oragif.test;

import oragif.http.response.HttpResponseBuilder;
import oragif.http.routing.Route;

public class test extends Route {
    String test;

    public test(String test) {
        this.test = test;
    }

    @Override
    public void get(HttpResponseBuilder responseBuilder) {
        System.out.println(this.test);
        responseBuilder.HTTP200().sendResponse(this.test);
    }
}
