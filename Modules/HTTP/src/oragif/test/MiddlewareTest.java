package oragif.test;

import com.sun.net.httpserver.HttpExchange;
import oragif.http.middleware.Middleware;

public class MiddlewareTest extends Middleware {
    private boolean consume;
    private boolean deny;
    String test;

    public MiddlewareTest(String test) {
        this.test = test;
    }

    public MiddlewareTest(String test, boolean consume, boolean deny) {
        this(test);
        setRedirectionPath("/test/2");
        this.consume = consume;
        this.deny = deny;
    }

    @Override
    public void pathListener(HttpExchange exchange) {
        System.out.println(test);

        if (this.consume) {
            consume();
        }
        if (this.deny) {
            deny();
        }
    }
}
