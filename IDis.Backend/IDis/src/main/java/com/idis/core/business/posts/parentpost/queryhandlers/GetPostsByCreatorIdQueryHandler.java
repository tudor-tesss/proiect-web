package com.idis.core.business.posts.parentpost.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetPostsByCreatorIdQuery;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetPostsByCreatorIdQueryHandler implements IRequestHandler<GetPostsByCreatorIdQuery, List<Post>> {
    @Override
    public CompletableFuture<List<Post>> handle(GetPostsByCreatorIdQuery getPostsByCreatorIdCommand) {
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
