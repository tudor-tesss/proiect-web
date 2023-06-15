package com.idis.core.business.posts.parentpost.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetPostsByCreatorIdQuery;
import com.idis.core.domain.category.Category;
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

        return QueryProvider.getAllAsync(Post.class).thenApply(allPosts -> {
            var posts = allPosts.stream()
                    .filter(post -> post.getAuthorId().equals(creatorId))
                    .toList();

            if (posts.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.Category.CreatorHasNoCategories);
            }

            return posts;
        });
    }
}
