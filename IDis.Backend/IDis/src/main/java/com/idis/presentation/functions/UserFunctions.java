package com.idis.presentation.functions;

import com.idis.core.business.commands.user.CreateUserCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.concurrent.CompletableFuture;

public class UserFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/users", method = HttpVerbs.POST)
    @Function(name = "createUser")
    public static CompletableFuture<HttpResponse> createUser(String requestBody) {
        var command = Serialization.deserialize(requestBody, CreateUserCommand.class);

        //catch exception
        try {
            return mediator
                    .send(command)
                    .thenCompose(user -> {
                        var responseContent = Serialization.serialize(user);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}
