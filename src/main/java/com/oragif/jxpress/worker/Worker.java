package com.oragif.jxpress.worker;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;

public abstract class Worker {
    protected Method method;
    public Worker() {
        this.method = Method.MIDDLEWARE;
    }
    public abstract void handle(Request request, Response response);

    public Method getMethod() {
        return this.method;
    }
}

