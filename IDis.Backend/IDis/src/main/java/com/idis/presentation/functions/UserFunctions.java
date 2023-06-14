package com.idis.presentation.functions;

import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.business.user.queries.GetUserByIdQuery;
import com.idis.core.business.usersession.commands.*;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Function;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IUserController;
import com.idis.shared.web.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/users", method = HttpVerbs.POST)
    @Function(name = "createUser")
    public static CompletableFuture<HttpResponse> createUser(String requestBody) {
        var command = Serialization.deserialize(requestBody, CreateUserCommand.class);

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

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        var responseContent = Serialization.serialize(r);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/gates", method = HttpVerbs.PATCH)
    @Function(name = "passUserGate")
    public static CompletableFuture<HttpResponse> passUserGate(String id, String requestBody) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = Serialization.deserialize(requestBody, PassUserGateCommand.class);
        command = new PassUserGateCommand(userId, command.code());

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/sessions", method = HttpVerbs.POST)
    @Function(name = "createUserSession")
    public static CompletableFuture<HttpResponse> createUserSession(String id, String requestBody) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = Serialization.deserialize(requestBody, CreateUserSessionCommand.class);
        command = new CreateUserSessionCommand(userId, command.userIpAddress());

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        var sessionId = r.sessionId();
                        res.setCookie("userId", sessionId.toString(), 3600 * 24); // 1 day

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/sessions", method = HttpVerbs.PATCH)
    @Function(name = "checkUserSession")
    public static CompletableFuture<HttpResponse> checkUserSession(String id, String requestBody) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = Serialization.deserialize(requestBody, CheckUserSessionCommand.class);
        command = new CheckUserSessionCommand(userId, command.sessionId(), command.ipAddress());

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/sessions", method = HttpVerbs.DELETE)
    @Function(name = "deleteUserSession")
    public static CompletableFuture<HttpResponse> deleteUserSession(String id, String requestBody) {
        var command = Serialization.deserialize(requestBody, DeleteUserSessionCommand.class);

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}", method = HttpVerbs.GET)
    @Function(name = "getUserById")
    public static CompletableFuture<HttpResponse> getUserById(String id, String requestBody){

        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetUserByIdQuery(userId);

        try{
            return mediator
                    .send(command)
                    .thenCompose(user -> {
                        var responseContent = Serialization.serialize(user);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }

}

