package oragif.jxpress.worker;

import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

public class RestWorker implements IWorker {

    private final String requestMethod;
    private final IWorker worker;

    public RestWorker(String requestMethod, IWorker worker) {
        this.requestMethod = requestMethod;
        this.worker        = worker;
    }

    @Override
    public void handle(Request request, Response response) {
        worker.handle(request, response);
    }
}
