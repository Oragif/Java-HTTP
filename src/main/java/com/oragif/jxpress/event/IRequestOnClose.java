package com.oragif.jxpress.event;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;

public interface IRequestOnClose {
    public void onClose(Request request, Response response);
}
