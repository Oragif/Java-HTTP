package oragif.jxpress.worker;

import oragif.jxpress.http.RequestHandler;
import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

public class RestWorker implements IWorker {

    private final String requestMethod;
    private final RequestHandler worker;

    public RestWorker(String requestMethod, RequestHandler worker) {
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
