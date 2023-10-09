package com.oragif.test;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.middleware.Session;
import com.oragif.jxpress.worker.Worker;
import com.oragif.jxpress.middleware.Middleware;

@Middleware(path = "/")
public class MiddlewareTest extends Worker {
    @Override
    public void handle(Request request, Response response) {
        Session session = (Session) request.getMiddlewareData("session");
        session.setSessionData("test", "test session data");
        request.setMiddlewareData("test", "Data from middleware");
        response.addHeader("Test", "Middleware Worked");
    }
}
