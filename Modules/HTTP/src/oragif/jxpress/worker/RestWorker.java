package oragif.jxpress.worker;

import oragif.jxpress.http.IRequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

public class RestWorker implements IWorker {

    private final String requestMethod;
    private final IRequestHandler worker;

    public RestWorker(String requestMethod, IRequestHandler worker) {
        this.requestMethod = requestMethod;
        this.worker        = worker;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    @Override
    public void handle(Request request, Response response) {
        worker.handle(request, response);
    }
}
