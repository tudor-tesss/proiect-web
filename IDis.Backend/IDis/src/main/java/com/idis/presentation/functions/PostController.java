package com.idis.presentation.functions;

import com.idis.core.business.posts.parentpost.command.*;
import com.idis.core.business.posts.postreply.command.CreatePostReplyCommand;
import com.idis.core.business.posts.postreply.command.GetAllPostRepliesCommand;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Controller;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PostController implements IController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/posts", method = HttpVerbs.POST)
    @Controller(name = "createPost")
    public static CompletableFuture<HttpResponse> createPost(String requestBody) {
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

    @Route(path = "/categories/{id}/posts", method = HttpVerbs.GET)
    @Controller(name = "getAllPostsInsideACategory")
    public static CompletableFuture<HttpResponse> getAllPostsInsideACategory(String id, String requestBody) {
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
    @Controller(name = "createPostReply")
    public static CompletableFuture<HttpResponse> createPostReply(String id, String requestBody) {
        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
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

    @Route(path = "/posts/{id}", method = HttpVerbs.GET)
    @Controller(name = "getPostById")
    public static CompletableFuture<HttpResponse> getPostById(String id, String requestBody) {
        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetPostByIdCommand(postId);
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

    @Route(path = "/posts/{id}/replies", method = HttpVerbs.GET)
    @Controller(name = "getAllPostReplies")
    public static CompletableFuture<HttpResponse> getAllPostReplies(String id, String requestBody) {
        UUID postId;

        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetAllPostRepliesCommand(postId);
        try {
            return mediator.send(command).thenCompose(r -> {
                var responseContent = Serialization.serialize(r);

                return HttpResponse.create(200, responseContent);
            });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/users/{id}/posts", method = HttpVerbs.GET)
    @Controller(name = "getPostsByCreatorId")
    public static CompletableFuture<HttpResponse> getPostsByCreatorId(String id, String requestBody){
        UUID creatorId;
        try {
            creatorId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetPostsByCreatorIdCommand(creatorId);
        try {
            return mediator.send(command).thenCompose(p -> {
                var responseContent = Serialization.serialize(p);

                return HttpResponse.create(200, responseContent);
            });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/posts", method = HttpVerbs.GET)
    @Controller(name = "getAllPosts")
    public static CompletableFuture <HttpResponse> getAllPosts (String requestBody) {
        var command = new GetAllPostsCommand();
        try {
            return mediator
                    .send(command)
                    .thenCompose(p -> {
                        var responseContent = Serialization.serialize(p);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}

