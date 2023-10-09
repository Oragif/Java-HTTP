package com.oragif.test;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;
import com.oragif.jxpress.middleware.Session;
import com.oragif.jxpress.util.CookieBuilder;
import com.oragif.jxpress.worker.Worker;
import com.oragif.jxpress.middleware.Middleware;

import java.time.LocalDateTime;

@Middleware(path = "/")
public class MiddlewareTest extends Worker {
    @Override
    public void handle(Request request, Response response) {
        Session session = (Session) request.getMiddlewareData("session");
        session.setSessionData("test", "test session data");
        response.addCookie(new CookieBuilder("cookieBuilder", "test").maxAge(100).sameSite(CookieBuilder.SameSite.LAX ).expires(LocalDateTime.now()));
        request.setMiddlewareData("test", "Data from middleware");
        response.addHeader("Test", "Middleware Worked");
    }
}
