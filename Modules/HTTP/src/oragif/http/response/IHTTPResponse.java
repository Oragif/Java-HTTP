package oragif.http.response;
/*
*  Docs https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
*  Attribution: Mozilla Corporation & httpwg.org - RFC 9110*
*  License: https://developer.mozilla.org/en-US/docs/MDN/Writing_guidelines/Attrib_copyright_license
*  * https://httpwg.org/specs/rfc9110.html#overview.of.status.codes
*/

public interface IHTTPResponse {
    /*-------------- Info Response --------------*/
    /**
     * Continue -- This interim response indicates that the client should continue the request or ignore the response if the request is already finished.
     * @implNote Continue
     */
    public HttpResponseBuilder HTTP100();

    /**
     * Switching Protocols -- This code is sent in response to an Upgrade request header from the client and indicates the protocol the server is switching to.
     * @implNote Switching Protocols
     */
    public HttpResponseBuilder HTTP101();

    /**
     * Processing -- This code indicates that the server has received and is processing the request, but no response is available yet.
     * @implNote Processing
     */
    public HttpResponseBuilder HTTP102();

    /**
     * Early Hints -- This status code is primarily intended to be used with the Link header, letting the user agent start preloading resources while the server prepares a response.
     * @implNote Early Hints
     */
    public HttpResponseBuilder HTTP103();

    /*-------------- Successful Response --------------*/
    /**
     * OK -- The request succeeded.
     * @implNote OK
     */
    public HttpResponseBuilder HTTP200();

    /**
     * Created -- The request succeeded, and a new resource was created as a result.
     * @implNote Created
     */
    public HttpResponseBuilder HTTP201();

    /**
     * Accepted -- The request has been received but not yet acted upon.
     * @implNote Accepted
     */
    public HttpResponseBuilder HTTP202();

    /**
     * Non-Authoritative Information -- This response code means the returned metadata is not exactly the same as is available from the origin server, but is collected from a local or a third-party copy.
     * @implNote Non-Authoritative
     */
    public HttpResponseBuilder HTTP203();

    /**
     * No Content -- There is no content to send for this request, but the headers may be useful.
     * @implNote No Content
     */
    public HttpResponseBuilder HTTP204();

    /**
     * Reset Content -- Tells the user agent to reset the document which sent this request.
     * @implNote Reset Content
     */
    public HttpResponseBuilder HTTP205();

    /**
     * Partial Content -- This response code is used when the Range header is sent from the client to request only part of a resource.
     * @implNote Partial Content
     */
    public HttpResponseBuilder HTTP206();

    /**
     * Multi-Status -- Conveys information about multiple resources, for situations where multiple status codes might be appropriate.
     * @implNote Multi-Status
     */
    public HttpResponseBuilder HTTP207();

    /**
     * Already Reported -- Used inside a <dav:propstat> response element to avoid repeatedly enumerating the internal members of multiple bindings to the same collection.
     * @implNote Already Reported
     */
    public HttpResponseBuilder HTTP208();

    /**
     * IM Used -- The server has fulfilled a GET request for the resource, and the response is a representation of the result of one or more instance-manipulations applied to the current instance.
     * @implNote IM Used
     */
    public HttpResponseBuilder HTTP226();
    /*-------------- Redirection Response --------------*/

    /**
     * Multiple Choices -- The request has more than one possible response.
     * @implNote Multiple Choices
     */
    public HttpResponseBuilder HTTP300();

    /**
     * Moved Permanently -- The URL of the requested resource has been changed permanently. The new URL SHOULD be given in the response.
     * @implNote Moved Permanently
     * @param newLocation String - path of the relocated resource
     */
    public HttpResponseBuilder HTTP301(String newLocation);

    /**
     * Found -- This response code means that the URI of requested resource has been changed temporarily.
     * @implNote Found
     */
    public HttpResponseBuilder HTTP302();

    /**
     * See Other -- The server sent this response to direct the client to get the requested resource at another URI with a GET request.
     * @implNote See Other
     */
    public HttpResponseBuilder HTTP303();

    /**
     * Not Modified -- This is used for caching purposes. It tells the client that the response has not been modified, so the client can continue to use the same cached version of the response.
     * @implNote Not Modified
     */
    public HttpResponseBuilder HTTP304();

    /**
     * Temporary Redirect -- The server sends this response to direct the client to get the requested resource at another URI with the same method that was used in the prior request.
     * @implNote Temporary Redirect
     */
    public HttpResponseBuilder HTTP307();

    /**
     * Permanent Redirect -- This means that the resource is now permanently located at another URI, specified by the Location: HTTP Response header.
     * @implNote Permanent Redirect
     */
    public HttpResponseBuilder HTTP308();
    /*-------------- Client Error Response --------------*/

    /**
     * Bad Request -- The server cannot or will not process the request due to something that is perceived to be a client error.
     * @implNote Bad Request
     */
    public HttpResponseBuilder HTTP400();

    /**
     * Unauthorized -- The client must authenticate itself to get the requested response.
     * @implNote Unauthorized
     */
    public HttpResponseBuilder HTTP401();

    /**
     * Forbidden -- The client does not have access rights to the content; that is, it is unauthorized, so the server is refusing to give the requested resource.
     * @implNote Forbidden
     */
    public HttpResponseBuilder HTTP403();

    /**
     * Not Found -- The server cannot find the requested resource. In the browser, this means the URL is not recognized.
     * @implNote Not Found
     */
    public HttpResponseBuilder HTTP404();
    /**
     * Method Not Allowed -- The request method is known by the server but is not supported by the target resource.
     * @implNote Method Not Allowed
     */
    public HttpResponseBuilder HTTP405();

    /**
     * Not Acceptable -- This response is sent when the web server, after performing server-driven content negotiation, doesn't find any content that conforms to the criteria given by the user agent.
     * @implNote Not Acceptable
     */
    public HttpResponseBuilder HTTP406();

    /**
     * Proxy Authentication Required -- This is similar to 401 Unauthorized but authentication is needed to be done by a proxy.
     * @implNote Proxy Authentication Required
     */
    public HttpResponseBuilder HTTP407();

    /**
     * Request Timeout -- This response is sent on an idle connection by some servers, even without any previous request by the client. It means that the server would like to shut down this unused connection.
     * @implNote Request Timeout
     */
    public HttpResponseBuilder HTTP408();

    /**
     * Conflict -- This response is sent when a request conflicts with the current state of the server.
     * @implNote Conflict
     */
    public HttpResponseBuilder HTTP409();

    /**
     * Gone -- This response is sent when the requested content has been permanently deleted from server, with no forwarding address.
     * @implNote Gone
     */
    public HttpResponseBuilder HTTP410();

    /**
     * Length Required -- Server rejected the request because the Content-Length header field is not defined and the server requires it.
     * @implNote Length Required
     */
    public HttpResponseBuilder HTTP411();

    /**
     * Precondition Failed -- The client has indicated preconditions in its headers which the server does not meet.
     * @implNote Precondition Failed
     */
    public HttpResponseBuilder HTTP412();

    /**
     * Payload Too Large -- Request entity is larger than limits defined by server.
     * @implNote Payload Too Large
     */
    public HttpResponseBuilder HTTP413();

    /**
     * URI Too Long -- The URI requested by the client is longer than the server is willing to interpret.
     * @implNote URI Too Long
     */
    public HttpResponseBuilder HTTP414();

    /**
     * Unsupported Media Type -- The media format of the requested data is not supported by the server, so the server is rejecting the request.
     * @implNote Unsupported Media Type
     */
    public HttpResponseBuilder HTTP415();

    /**
     * Range Not Satisfiable -- The range specified by the Range header field in the request cannot be fulfilled. It's possible that the range is outside the size of the target URI's data.
     * @implNote Range Not Satisfiable
     */
    public HttpResponseBuilder HTTP416();

    /**
     * Expectation Failed -- This response code means the expectation indicated by the Expect request header field cannot be met by the server.
     * @implNote Expectation Failed
     */
    public HttpResponseBuilder HTTP417();

    /**
     * I'm a teapot -- The server refuses the attempt to brew coffee with a teapot.
     * @implNote I'm a teapot
     */
    public HttpResponseBuilder HTTP418();

    /**
     * Misdirected Request -- The request was well-formed but was unable to be followed due to semantic errors.
     * @implNote Misdirected Request
     */
    public HttpResponseBuilder HTTP421();

    /**
     *  --
     * @implNote
     */
    public HttpResponseBuilder HTTP422();

    /**
     * Locked -- The resource that is being accessed is locked.
     * @implNote Locked
     */
    public HttpResponseBuilder HTTP423();

    /**
     * Failed Dependency -- The request failed due to failure of a previous request.
     * @implNote Failed Dependency
     */
    public HttpResponseBuilder HTTP424();

    /**
     * Too Early -- Indicates that the server is unwilling to risk processing a request that might be replayed.
     * @implNote Too Early
     */
    public HttpResponseBuilder HTTP425();

    /**
     * Upgrade Required -- The server refuses to perform the request using the current protocol but might be willing to do so after the client upgrades to a different protocol.
     * @implNote Upgrade Required
     */
    public HttpResponseBuilder HTTP426();

    /**
     * Precondition Required -- The origin server requires the request to be conditional.
     * @implNote Precondition Required
     */
    public HttpResponseBuilder HTTP428();

    /**
     * Too Many Requests -- The user has sent too many requests in a given amount of time ("rate limiting").
     * @implNote Too Many Requests
     */
    public HttpResponseBuilder HTTP429();

    /**
     * Request Header Fields Too Large -- The server is unwilling to process the request because its header fields are too large.
     * @implNote Request Header Fields Too Large
     */
    public HttpResponseBuilder HTTP431();

    /**
     * Unavailable For Legal Reasons -- The user agent requested a resource that cannot legally be provided, such as a web page censored by a government.
     * @implNote Unavailable For Legal Reasons
     */
    public HttpResponseBuilder HTTP451();

    /*-------------- Server Error Response --------------*/
    /**
     * Internal Server Error -- The server has encountered a situation it does not know how to handle.
     * @implNote Internal Server Error
     */
    public HttpResponseBuilder HTTP500();
    /**
     * Not Implemented -- The request method is not supported by the server and cannot be handled.
     * @implNote Not Implemented
     */
    public HttpResponseBuilder HTTP501();

    /**
     * Bad Gateway -- This error response means that the server, while working as a gateway to get a response needed to handle the request, got an invalid response.
     * @implNote Bad Gateway
     */
    public HttpResponseBuilder HTTP502();

    /**
     * Service Unavailable -- The server is not ready to handle the request.
     * @implNote Service Unavailable
     */
    public HttpResponseBuilder HTTP503();

    /**
     * Gateway Timeout -- This error response is given when the server is acting as a gateway and cannot get a response in time.
     * @implNote Gateway Timeout
     */
    public HttpResponseBuilder HTTP504();

    /**
     * HTTP Version Not Supported -- The HTTP version used in the request is not supported by the server.
     * @implNote HTTP Version Not Supported
     */
    public HttpResponseBuilder HTTP505();

    /**
     * Variant Also Negotiates -- The server has an internal configuration error.
     * @implNote Variant Also Negotiates
     */
    public HttpResponseBuilder HTTP506();

    /**
     * Insufficient Storage -- The method could not be performed on the resource because the server is unable to store the representation needed to successfully complete the request.
     * @implNote Insufficient Storage
     */
    public HttpResponseBuilder HTTP507();

    /**
     * Loop Detected -- The server detected an infinite loop while processing the request.
     * @implNote Loop Detected
     */
    public HttpResponseBuilder HTTP508();

    /**
     * Not Extended -- Further extensions to the request are required for the server to fulfill it.
     * @implNote Not Extended
     */
    public HttpResponseBuilder HTTP510();

    /**
     * Network Authentication Required -- Indicates that the client needs to authenticate to gain network access.
     * @implNote Network Authentication Required
     */
    public HttpResponseBuilder HTTP511();
}
