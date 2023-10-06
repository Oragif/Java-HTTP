package com.oragif.jxpress.worker;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;

public interface IWorker {
    public void handle(Request request, Response response);
    public Method getMethod();
}
