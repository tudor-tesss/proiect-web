package com.idis.presentation.functions.test;

import com.idis.core.business.commands.test.CreateTestObjCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.concurrent.CompletableFuture;

public final class TestController implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/test", method = HttpVerbs.POST)
    @Function(name = "postTestResponse")
    public static CompletableFuture<HttpResponse> postTestResponse(String requestBody) {
        var command = Serialization.deserialize(requestBody, CreateTestObjCommand.class);

        return mediator
                .send(command)
                .thenCompose(testObj -> {
                    var responseContent = Serialization.serialize(testObj);

                    if (testObj == null) {
                        return HttpResponse.create(400, responseContent);
                    }

                    return HttpResponse.create(200, responseContent);
                });
    }
}
