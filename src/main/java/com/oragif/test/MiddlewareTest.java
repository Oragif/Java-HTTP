package com.oragif.test;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.worker.Worker;
import com.oragif.jxpress.worker.middleware.Middleware;

@Middleware(path = "/")
public class MiddlewareTest extends Worker {
    @Override
    public void handle(Request request, Response response) {
        response.addHeader("Test", "Middleware Worked");
    }
}
