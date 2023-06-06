package com.idis.shared.web.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class HttpResponse {
    private int status;
    private String content;
    private Map<String, String> headers;
    private Map<String, String> cookies;

    protected HttpResponse(int status, String content, Map<String, String> headers, Map<String, String> cookies) {
        this.status = status;
        this.content = content;
        this.headers = headers;
        this.cookies = cookies;

        System.out.println("Headers2: " + headers);
    }

    public static CompletableFuture<HttpResponse> create(int status, String content) {
        var headers = new HashMap<String, String>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/json");

        var cookies = new HashMap<String, String>();

        var response = new HttpResponse(status, content, headers, cookies);
        return CompletableFuture.completedFuture(response);
    }

    public static CompletableFuture<HttpResponse> create(int status, String content, Map<String, String> headers) {
        var cookies = new HashMap<String, String>();

        var response = new HttpResponse(status, content, headers, cookies);

        System.out.println("Headers1: " + headers);
        return CompletableFuture.completedFuture(response);
    }

    public void setCookie(String name, String value, int maxAge) {
        cookies.put(name, value + "; Max-Age=" + maxAge);
    }

    public int getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public Map<String, String> getHeaders() {
        // Include cookies in the headers.
        if (!cookies.isEmpty()) {
            StringBuilder cookieHeader = new StringBuilder();
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookieHeader.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
            }
            headers.put("Set-Cookie", cookieHeader.toString());
        }
        return headers;
    }
}
