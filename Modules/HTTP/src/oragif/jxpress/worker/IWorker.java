package oragif.jxpress.worker;

import oragif.jxpress.http.Request;
import oragif.jxpress.http.Response;

public interface IWorker {
    public void handle(Request request, Response response);
}
