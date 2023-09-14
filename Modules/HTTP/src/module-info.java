module oragif.http {
    requires java.logging;
    requires jdk.httpserver;
    requires logger;
    exports oragif.http;
    exports oragif.http.routing;
    exports oragif.http.response;
}