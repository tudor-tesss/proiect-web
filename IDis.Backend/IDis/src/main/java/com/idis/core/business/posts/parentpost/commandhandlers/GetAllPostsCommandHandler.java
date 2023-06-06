package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetAllPostsCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllPostsCommandHandler implements IRequestHandler<GetAllPostsCommand, List<Post>> {
    @Override
    public CompletableFuture<List<Post>> handle (GetAllPostsCommand getAllPostsCommand) {
        var posts = QueryProvider.getAll(Post.class);

        if(posts.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Post.NoPostsAvailable);
        }

        return CompletableFuture.completedFuture(posts);
    }
}
