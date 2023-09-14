module HTTP {
    requires java.logging;
    requires jdk.httpserver;
    exports oragif.http;
    exports oragif.http.routing;
    exports oragif.http.response;
}