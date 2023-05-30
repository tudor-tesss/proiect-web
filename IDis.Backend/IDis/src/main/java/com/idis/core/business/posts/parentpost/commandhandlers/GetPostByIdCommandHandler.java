package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetPostByIdCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class GetPostByIdCommandHandler implements IRequestHandler<GetPostByIdCommand, Post> {
    @Override
    public CompletableFuture<Post> handle(GetPostByIdCommand getPostByIdCommand) {
        var posts = NimbleJQueryProvider.getAll(Post.class);
        var postResult = posts.stream().filter(p -> p.getId().equals(getPostByIdCommand.postId())).findFirst();
        if(postResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
        }
        return CompletableFuture.completedFuture(postResult.get());
    }
}
