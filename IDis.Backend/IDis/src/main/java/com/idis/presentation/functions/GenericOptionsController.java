package com.idis.presentation.functions;

import com.idis.shared.web.communication.Controller;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GenericOptionsController implements IController {
    @Route(path = "/{path:.*}", method = HttpVerbs.OPTIONS)
    @Controller(name = "optionsHandler")
    public static CompletableFuture<HttpResponse> optionsHandler(String path, String requestBody) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/json");

        return HttpResponse.create(200, "", headers);
    }
}