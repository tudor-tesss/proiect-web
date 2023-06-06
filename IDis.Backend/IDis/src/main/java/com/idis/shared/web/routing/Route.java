package com.idis.shared.web.routing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Route} annotation is used to define an HTTP route with a specified path
 * and method for a method in a controller class. This annotation enables easy mapping
 * of HTTP requests to controller methods based on the request path and HTTP verb.
 * <p>
 * Example usage:
 * <pre>
 * {@literal @}Route(path = "/example", method = HttpVerbs.GET)
 * public HttpResponse exampleEndpoint() {
 *     // Handle the request here
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {

    /**
     * The path of the HTTP route, e.g., "/example".
     *
     * @return the route path as a string
     */
    String path();

    /**
     * The HTTP method (verb) for the route, e.g., HttpVerbs.GET or "GET".
     *
     * @return the HTTP method as a string
     */
    String method();
}
