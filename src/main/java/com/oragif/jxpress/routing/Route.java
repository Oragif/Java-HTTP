package com.oragif.jxpress.routing;

import com.oragif.jxpress.worker.Method;
import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
public @interface Route {
    String path() default "/";
    String provides() default "text/plain";
    Method method() default Method.GET;
}