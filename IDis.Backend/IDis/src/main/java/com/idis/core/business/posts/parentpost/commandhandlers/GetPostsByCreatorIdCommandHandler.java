package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetPostsByCreatorIdCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetPostsByCreatorIdCommandHandler implements IRequestHandler<GetPostsByCreatorIdCommand, List<Post>> {
    @Override
    public CompletableFuture<List<Post>> handle(GetPostsByCreatorIdCommand getPostsByCreatorIdCommand) {
        var creatorId = getPostsByCreatorIdCommand.creatorId();
        var allPosts = QueryProvider.getAll(Post.class);
        var posts = new ArrayList<Post>();

        for(var post : allPosts) {
            if(post.getAuthorId().equals(creatorId)) {
                posts.add(post);
            }
        }

        if(posts.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.CreatorHasNoPosts);
        }

        return CompletableFuture.completedFuture(posts);
    }
}
