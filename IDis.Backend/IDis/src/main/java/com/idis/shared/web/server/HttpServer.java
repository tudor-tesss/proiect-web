package com.idis.shared.web.server;

import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The {@code HttpServer} class represents an HTTP server that listens for incoming requests on a specified port.
 * It uses the {@link IController} to handle requests and routes them using the {@link Router}.
 * <p>
 * The server is implemented using the {@code com.sun.net.httpserver.HttpServer} package.
 * <p>
 * Usage example:
 * <pre>
 *     HttpServer server = HttpServer.create(8080)
 *                                  .withController(new UserController());
 *     server.start();
 * </pre>
 */
public final class HttpServer {
    private int port;
    private com.sun.net.httpserver.HttpServer server;
    private Set<Object> controllers;
    private Router router;

    /**
     * Private constructor for the HttpServer class.
     * Initializes the server with the given port and sets the initial controller to null.
     *
     * @param port The port on which the server will listen for incoming requests.
     * @throws IOException if an I/O error occurs while creating the server.
     */
    private HttpServer(int port) throws IOException {
        this.port = port;
        this.server = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);

        controllers = new HashSet<>();
    }

    /**
     * Creates an instance of the HttpServer with the specified port.
     * This method serves as a factory method for the HttpServer class.
     *
     * @param port The port on which the server will listen for incoming requests.
     * @return An instance of the HttpServer class.
     * @throws IOException if an I/O error occurs while creating the server.
     */
    public static HttpServer create(int port) throws IOException {
        return new HttpServer(port);
    }

    /**
     * Sets the UserController for handling incoming requests.
     * Returns the current instance of HttpServer, allowing for method chaining.
     *
     * @param controllers An array of IController objects.
     * @return The current instance of the HttpServer class.
     */
    public HttpServer withControllers(IController... controllers) {
        this.controllers.addAll(Arrays.asList(controllers));
        return this;
    }

    /**
     * Starts the HTTP server.
     * The server begins listening for incoming requests on the specified port.
     * Before starting the server, a UserController must be set using the {@link #withControllers} method.
     * Otherwise, an IllegalStateException will be thrown.
     */
    public void start() {
        if (controllers == null) {
            throw new IllegalStateException("Controller must be set before starting the server.");
        }

        router = new Router(controllers);

        server.createContext("/", router);

        server.start();

        System.out.println("Server started at http://localhost:" + port);
    }
}
