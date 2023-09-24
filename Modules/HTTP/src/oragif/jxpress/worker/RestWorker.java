package oragif.jxpress.worker;

import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

public class RestWorker implements IWorker {
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
