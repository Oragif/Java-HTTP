package oragif.jxpress.routing;

import oragif.jxpress.worker.Method;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Route {
    String path() default "/";
    String provides() default "text/plain";
    Method method() default Method.GET;
}