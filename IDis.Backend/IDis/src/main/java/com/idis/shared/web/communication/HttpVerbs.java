package com.idis.shared.web.communication;

/**
 * The {@code HttpVerbs} class defines constants for HTTP verbs (methods) used in
 * HTTP requests. It provides an easy way to reference standard HTTP verbs in code
 * without using string literals.
 * <p>
 * Example usage:
 * <pre>
 * String getVerb = HttpVerbs.GET;
 * String postVerb = HttpVerbs.POST;
 * </pre>
 */
public final class HttpVerbs {

    /**
     * The HTTP GET verb, used for requesting a representation of the specified resource.
     */
    public static final String GET = "GET";

    /**
     * The HTTP POST verb, used for submitting an entity to the specified resource,
     * often causing a change in state or side effects on the server.
     */
    public static final String POST = "POST";

    /**
     * The HTTP PUT verb, used for updating the specified resource with the request payload.
     */
    public static final String PUT = "PUT";

    /**
     * The HTTP PATCH verb, used for applying partial modifications to a resource.
     */
    public static final String PATCH = "PATCH";

    /**
     * The HTTP DELETE verb, used for deleting the specified resource.
     */
    public static final String DELETE = "DELETE";

    /**
     * The HTTP HEAD verb, used for requesting the headers of the specified resource,
     * identical to GET but without the response body.
     */
    public static final String HEAD = "HEAD";

    /**
     * The HTTP OPTIONS verb, used for describing the communication options for the
     * target resource.
     */
    public static final String OPTIONS = "OPTIONS";

    /**
     * The HTTP TRACE verb, used for retrieving a diagnostic message for the
     * request and response of the specified resource.
     */
    public static final String TRACE = "TRACE";

    /**
     * The HTTP CONNECT verb, used for establishing a network connection to a
     * resource, usually for use with a proxy.
     */
    public static final String CONNECT = "CONNECT";
}
