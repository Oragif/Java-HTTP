package com.oragif.jxpress.http;

@FunctionalInterface
public interface IRequestHandler {
    void handle(Request request, Response response);
}
