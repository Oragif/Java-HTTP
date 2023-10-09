package com.oragif.jxpress.event;

import com.oragif.jxpress.http.Request;
import com.oragif.jxpress.http.Response;

import java.util.ArrayList;
import java.util.List;

public class RequestEventHandler {
     private List<IRequestOnClose> onCloseListeners;

    {
        this.onCloseListeners = new ArrayList<>();
    }

     public void triggerOnCloseListeners(Request request, Response response) {
        this.onCloseListeners.forEach(requestOnClose -> requestOnClose.onClose(request, response));
     }

     public void listenOnClose(IRequestOnClose requestOnClose) {
         this.onCloseListeners.add(requestOnClose);
     }
}
