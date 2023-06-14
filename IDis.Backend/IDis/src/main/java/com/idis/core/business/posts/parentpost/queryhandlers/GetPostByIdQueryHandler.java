package com.idis.core.business.posts.parentpost.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetPostByIdQuery;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class GetPostByIdQueryHandler implements IRequestHandler<GetPostByIdQuery, Post> {
    @Override
    public CompletableFuture<Post> handle(GetPostByIdQuery getPostByIdCommand) {
        return QueryProvider.getByIdAsync(Post.class, getPostByIdCommand.postId()).thenApply(post -> {
            if(post.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
            }

            return post.get();
        });
    }
}
