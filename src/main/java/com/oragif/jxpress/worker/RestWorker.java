package com.oragif.jxpress.worker;

import com.oragif.jxpress.http.IRequestHandler;
import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;

public class RestWorker extends Worker {
    private final IRequestHandler worker;
    private final Method method;

    public RestWorker(Method requestMethod, IRequestHandler worker) {
        this.method = requestMethod;
        this.worker = worker;
    }

    @Override
    public void handle(Request request, Response response) {
        if (response.isClosed()) return;
        worker.handle(request, response);
    }

    @Override
    public Method getMethod() {
        return this.method;
    }
}
