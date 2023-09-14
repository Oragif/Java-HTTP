package oragif.http.response;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
public class HttpResponseBuilder implements IHTTPResponse{
    private final HttpExchange httpExchange;
    private Headers headers;
    private int httpStatusCode;

    public HttpResponseBuilder (HttpExchange exchange) {
        this.httpExchange = exchange;
    }

    public HttpExchange getHttpExchange() {
        return httpExchange;
    }

    public Headers getHeaders() {
        return headers;
    }

    public HttpResponseBuilder appendHeader(String headerName, List<String> values) {
        this.headers.put(headerName, values);
        return this;
    }

    public HttpResponseBuilder appendHeader(String headerName, String value) {
        this.headers.add(headerName, value);
        return this;
    }

    public HttpResponseBuilder removeHeader(String headerName) {
        this.headers.remove(headerName);
        return this;
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public boolean sendResponse(Object body) {
        //Stop if no exchange is present
        if (this.httpExchange == null) return false;

        //Check and convert content to bytes
        int contentLength = -1;
        byte[] byteContent = null;
        if (body != null) {
            byteContent = body.toString().getBytes();
            contentLength = byteContent.length;
        }

        //Try sending the content to the user
        try {
            this.httpExchange.sendResponseHeaders(this.httpStatusCode, contentLength);
            OutputStream responseStream = this.httpExchange.getResponseBody();
            if (byteContent != null) responseStream.write(byteContent);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public HttpResponseBuilder HTTP100() {
        this.httpStatusCode = 100;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP101() {
        this.httpStatusCode = 101;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP102() {
        this.httpStatusCode = 102;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP103() {
        this.httpStatusCode = 103;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP200() {
        this.httpStatusCode = 200;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP201() {
        this.httpStatusCode = 201;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP202() {
        this.httpStatusCode = 202;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP203() {
        this.httpStatusCode = 203;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP204() {
        this.httpStatusCode = 204;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP205() {
        this.httpStatusCode = 205;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP206() {
        this.httpStatusCode = 206;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP207() {
        this.httpStatusCode = 207;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP208() {
        this.httpStatusCode = 208;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP226() {
        this.httpStatusCode = 226;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP300() {
        this.httpStatusCode = 300;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP301() {
        this.httpStatusCode = 301;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP302() {
        this.httpStatusCode = 302;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP303() {
        this.httpStatusCode = 303;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP304() {
        this.httpStatusCode = 304;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP307() {
        this.httpStatusCode = 307;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP308() {
        this.httpStatusCode = 308;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP400() {
        this.httpStatusCode = 400;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP401() {
        this.httpStatusCode = 401;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP403() {
        this.httpStatusCode = 403;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP404() {
        this.httpStatusCode = 404;
        return this;
    }

    public HttpResponseBuilder HTTP405() {
        this.httpStatusCode = 405;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP406() {
        this.httpStatusCode = 406;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP407() {
        this.httpStatusCode = 407;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP408() {
        this.httpStatusCode = 408;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP409() {
        this.httpStatusCode = 409;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP410() {
        this.httpStatusCode = 410;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP411() {
        this.httpStatusCode = 411;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP412() {
        this.httpStatusCode = 412;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP413() {
        this.httpStatusCode = 413;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP414() {
        this.httpStatusCode = 414;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP415() {
        this.httpStatusCode = 415;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP416() {
        this.httpStatusCode = 416;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP417() {
        this.httpStatusCode = 417;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP418() {
        this.httpStatusCode = 418;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP421() {
        this.httpStatusCode = 421;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP422() {
        this.httpStatusCode = 422;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP423() {
        this.httpStatusCode = 423;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP424() {
        this.httpStatusCode = 424;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP425() {
        this.httpStatusCode = 425;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP426() {
        this.httpStatusCode = 426;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP428() {
        this.httpStatusCode = 428;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP429() {
        this.httpStatusCode = 429;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP431() {
        this.httpStatusCode = 431;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP451() {
        this.httpStatusCode = 451;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP500() {
        this.httpStatusCode = 500;
        return this;
    }

    public HttpResponseBuilder HTTP501() {
        this.httpStatusCode = 501;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP502() {
        this.httpStatusCode = 502;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP503() {
        this.httpStatusCode = 503;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP504() {
        this.httpStatusCode = 504;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP505() {
        this.httpStatusCode = 505;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP506() {
        this.httpStatusCode = 506;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP507() {
        this.httpStatusCode = 507;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP508() {
        this.httpStatusCode = 508;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP510() {
        this.httpStatusCode = 510;
        return this;
    }

    @Override
    public HttpResponseBuilder HTTP511() {
        this.httpStatusCode = 511;
        return this;
    }
}
