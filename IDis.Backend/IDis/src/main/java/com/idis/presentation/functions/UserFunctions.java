package com.idis.presentation.functions;

import com.idis.core.business.commands.user.CreateUserCommand;
import com.idis.core.business.commands.user.CreateUserGateCommand;
import com.idis.core.business.commands.user.PassUserGateCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.UUID;
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

    @Route(path = "/users/gates", method = HttpVerbs.POST)
    @Function(name = "createUserGate")
    public static CompletableFuture<HttpResponse> createUserGate(String requestBody) {
        var command = Serialization.deserialize(requestBody, CreateUserGateCommand.class);

        //catch exception
        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        var responseContent = Serialization.serialize(r);

                        return HttpResponse.create(200, responseContent);
                    });
        }
        catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/gates", method = HttpVerbs.PATCH)
    @Function(name = "passUserGate")
    public static CompletableFuture<HttpResponse> passUserGate(String id, String requestBody) {
        var command = Serialization.deserialize(requestBody, PassUserGateCommand.class);
        command = new PassUserGateCommand(UUID.fromString(id), command.code());

        //catch exception
        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        // When the user gate is passed, set the session ID as a cookie.
                        String sessionId = r.sessionId(); // Replace this with the appropriate session ID.
                        res.setCookie("sessionId", sessionId, 3600); // Set the cookie to expire in 1 hour.

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

}
