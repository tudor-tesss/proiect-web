package com.idis.presentation.functions;

import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.idis.core.business.posts.postreply.command.CreatePostReplyCommand;
import com.idis.core.business.posts.parentpost.command.GetAllPostsInsideOfACategoryCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import javax.print.attribute.standard.Media;
import java.util.UUID;
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

    @Route(path = "/category/{id}/posts", method = HttpVerbs.GET)
    @Function(name = "getAllPostsInsideACategory")
    public static CompletableFuture <HttpResponse> getAllPostsInsideACategory (String id, String requestBody) {

        UUID categoryId;
        try {
            categoryId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetAllPostsInsideOfACategoryCommand(categoryId);


        try {
            return mediator
                    .send(command)
                    .thenCompose(posts -> {
                        var responseContent = Serialization.serialize(posts);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/posts/{id}/replies", method = HttpVerbs.POST)
    @Function(name = "createPostReply")
    public static CompletableFuture <HttpResponse> createPostReply (String id, String requestBody) {
        UUID postId;
        try {
            postId = UUID.fromString(id);
        }
        catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = Serialization.deserialize(requestBody, CreatePostReplyCommand.class);

        command = new CreatePostReplyCommand(command.authorId(), postId, command.title(), command.body(), command.ratings());

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
}
