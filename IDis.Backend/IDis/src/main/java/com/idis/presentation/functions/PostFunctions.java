package com.idis.presentation.functions;

import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import javax.print.attribute.standard.Media;
import java.util.concurrent.CompletableFuture;

public class PostFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/posts", method = HttpVerbs.POST)
    @Function(name = "createPost")
    public static CompletableFuture <HttpResponse> createPost (String requestBody) {
        var command = Serialization.deserialize(requestBody, CreatePostCommand.class);

        try {
            return mediator
                    .send(command)
                    .thenCompose(post -> {
                        var responseContent = Serialization.serialize(post);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}
