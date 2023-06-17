package com.idis.presentation.functions;

import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.business.user.queries.GetAllUserNamesQuery;
import com.idis.core.business.user.queries.GetUserByIdQuery;
import com.idis.core.business.usersession.commands.*;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Controller;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserController implements IController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/users", method = HttpVerbs.POST)
    @Controller(name = "createUser")
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
    @Controller(name = "createUserGate")
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
    @Controller(name = "passUserGate")
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
    @Controller(name = "createUserSession")
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
    @Controller(name = "refreshUserSession")
    public static CompletableFuture<HttpResponse> refreshUserSession(String id, String requestBody) {
        UUID userId;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = Serialization.deserialize(requestBody, RefreshUserSessionCommand.class);
        command = new RefreshUserSessionCommand(userId, command.sessionId(), command.ipAddress());

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
    @Controller(name = "deleteUserSession")
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
    @Controller(name = "getUserById")
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

    @Route(path = "/users/all/names", method = HttpVerbs.GET)
    @Controller(name = "getAllUserNames")
    public static CompletableFuture<HttpResponse> getAllUserNames(String requestBody) {
        var command = new GetAllUserNamesQuery();

        try {
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

