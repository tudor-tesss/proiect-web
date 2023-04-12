package com.idis.presentation.functions;

import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GenericOptionsController implements IUserController {
    @Route(path = "/{path:.*}", method = HttpVerbs.OPTIONS)
    @Function(name = "optionsHandler")
    public static CompletableFuture<HttpResponse> optionsHandler(String path, String requestBody) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/json");

        return HttpResponse.create(200, "", headers);
    }
}