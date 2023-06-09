package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetPostByIdCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class GetPostByIdCommandHandler implements IRequestHandler<GetPostByIdCommand, Post> {
    @Override
    public CompletableFuture<Post> handle(GetPostByIdCommand getPostByIdCommand) {
        return QueryProvider.getByIdAsync(Post.class, getPostByIdCommand.postId()).thenApply(post -> {
            if(post.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
            }

            return post.get();
        });
    }
}
