package oragif.jxpress.http;

import java.io.IOException;

@FunctionalInterface
public interface RequestHandler {
    void handle(Request request, Response response);
}
