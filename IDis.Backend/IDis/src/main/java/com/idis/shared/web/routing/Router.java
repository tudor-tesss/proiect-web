package com.idis.shared.web.routing;

import com.idis.shared.web.communication.Function;
import com.idis.shared.web.communication.HttpResponse;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

/**
 * The {@code Router} class is an implementation of {@code HttpHandler} and is responsible for
 * routing incoming HTTP requests to the appropriate controller methods based on the request
 * method (HTTP verb) and path. The routing is achieved by scanning the controller class for
 * methods annotated with {@code Route} and {@code Function} annotations. The registered routes
 * are then matched against the incoming request's method and path, and the corresponding
 * controller method is invoked.
 * <p>
 * The {@code Router} class supports path parameters, which are denoted by curly braces in the
 * route path (e.g., "/example/{id}"). When a request is received with a matching path, the
 * path parameter values are extracted and passed as arguments to the associated controller
 * method. The request body is also passed as an argument to the controller method.
 * <p>
 * Example usage:
 * <pre>
 * {@literal @}Route(path = "/example/{id}", method = HttpVerbs.GET)
 * public CompletableFuture{@literal <}HttpResponse{@literal >} exampleEndpoint(String id, String requestBody) {
 *     // Handle the request here
 * }
 * </pre>
 */
public final class Router implements HttpHandler {
    /**
     * The {@code RouteKey} class represents a combination of an HTTP method and a path used as a key
     * for storing controller methods in the route map.
     */
    private static class RouteKey {
        private String method;
        private String path;

        public RouteKey(String method, String path) {
            this.method = method;
            this.path = path;
        }
    }

    private Map<RouteKey, Method> routeMap = new HashMap<>();
    private Set<Object> controllers;

    /**
     * Constructs a {@code Router} instance, scanning the provided controller objects for methods
     * annotated with {@code Route} and {@code Function} annotations and registering them in the route map.
     *
     * @param controllers The controller objects containing the route methods.
     */
    public Router(Set<Object> controllers) {
        this.controllers = controllers;
        for (Object controller : controllers) {
            registerRoutesForController(controller);
        }
        displayRoutes();
    }

    /**
     * Handles the incoming HTTP request by matching the request's method and path to the registered routes
     * and invoking the associated controller method.
     *
     * @param httpExchange The {@code HttpExchange} object representing the incoming request and response.
     * @throws IOException if an I/O error occurs while reading the request or writing the response.
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        var requestMethod = httpExchange.getRequestMethod();
        var requestPath = httpExchange.getRequestURI().getPath();
        var requestBody = readRequestBody(httpExchange);

        // Handle OPTIONS request directly
        if (requestMethod.equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
            httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
            httpExchange.sendResponseHeaders(200, 0);
            httpExchange.getResponseBody().close();
            return;
        }

        routeRequest(requestMethod, requestPath, requestBody)
                .thenAccept(response -> {
                    try {
                        // Set the CORS headers
                        httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        httpExchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
                        httpExchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                        if (response.getHeaders().containsKey("Content-Type")) {
                            httpExchange.getResponseHeaders().set("Content-Type", response.getHeaders().get("Content-Type"));
                        }

                        httpExchange.sendResponseHeaders(response.getStatus(), response.getContent().length());
                        var outputStream = httpExchange.getResponseBody();
                        outputStream.write(response.getContent().getBytes());
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Routes the incoming HTTP request to the appropriate controller method based on the request method
     * and path. If a matching route is found, the associated controller method is invoked, and its
     * {@code CompletableFuture<HttpResponse>} result is returned.
     *
     * @param requestMethod The HTTP request method.
     * @param requestPath   The HTTP request path.
     * @param requestBody   The HTTP request body as a string.
     * @return A {@code CompletableFuture<HttpResponse>} representing the result of
     * he invoked controller method, or a completed future with an HTTP 404 (Not Found) response if no
     * matching route is found.
     */
    private CompletableFuture<HttpResponse> routeRequest(String requestMethod, String requestPath, String requestBody) {
        for (var entry : routeMap.entrySet()) {
            var routeKey = entry.getKey();
            if (!requestMethod.equalsIgnoreCase(routeKey.method)) {
                continue;
            }

            var routePattern = routeKey.path.replaceAll("\\{[^/]+\\}", "([^/]+)");
            var pattern = Pattern.compile(routePattern);
            var matcher = pattern.matcher(requestPath);

            if (matcher.matches()) {
                System.out.println("Routing request to " + entry.getValue().getName() + "()" + " for " + requestMethod + " " + requestPath);

                try {
                    var params = new Object[matcher.groupCount() + 1];
                    for (var i = 1; i <= matcher.groupCount(); i++) {
                        params[i - 1] = matcher.group(i);
                    }
                    params[params.length - 1] = requestBody;

                    Object controller = controllers.stream()
                            .filter(c -> c.getClass().equals(entry.getValue().getDeclaringClass()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No matching controller found"));

                    return ((CompletableFuture<HttpResponse>) entry.getValue().invoke(controller, params))
                            .exceptionally(ex -> {
                                ex.printStackTrace();
                                return HttpResponse.create(500, "Internal Server Error").join();
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    return HttpResponse.create(500, "Internal Server Error");
                }
            }
        }

        return HttpResponse.create(404, "Not Found");
    }


    /**
     * Reads the request body from the {@code HttpExchange} object and returns it as a string.
     *
     * @param httpExchange The {@code HttpExchange} object representing the incoming request.
     * @return The request body as a string.
     * @throws IOException if an I/O error occurs while reading the request body.
     */
    private String readRequestBody(HttpExchange httpExchange) throws IOException {
        var inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        var bufferedReader = new BufferedReader(inputStreamReader);
        var stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        bufferedReader.close();
        inputStreamReader.close();

        return stringBuilder.toString();
    }

    /**
     * Displays the registered routes in the console. This method is called during the construction
     * of the {@code Router} instance for logging purposes.
     */
    private void displayRoutes() {
        System.out.println("Registered routes:");
        for (var entry : routeMap.entrySet()) {
            var routeKey = entry.getKey();
            var controllerClass = entry.getValue().getDeclaringClass().getSimpleName();

            System.out.println(routeKey.method + " " + routeKey.path + " -> " + controllerClass + "." + entry.getValue().getName() + "()");
        }
    }

    /**
     * Scans the provided controller object for methods annotated with {@code Route} and {@code Function}
     * annotations and registers them in the route map.
     *
     * @param controller The controller object containing the route methods.
     */
    private void registerRoutesForController(Object controller) {
        for (var method : controller.getClass().getDeclaredMethods()) {
            var route = method.getAnnotation(Route.class);
            var function = method.getAnnotation(Function.class);

            if (route != null && function != null) {
                if (!function.name().equals(method.getName())) {
                    throw new IllegalStateException("Function name '" + function.name() + "' does not match method name '" + method.getName() + "'");
                }
                routeMap.put(new RouteKey(route.method(), route.path()), method);
            }
        }
    }

}
