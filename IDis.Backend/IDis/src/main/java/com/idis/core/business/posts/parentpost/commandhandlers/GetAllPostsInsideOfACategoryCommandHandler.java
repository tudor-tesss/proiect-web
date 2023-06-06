package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetAllPostsInsideOfACategoryCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllPostsInsideOfACategoryCommandHandler implements IRequestHandler<GetAllPostsInsideOfACategoryCommand, List<Post>> {
    public CompletableFuture<List<Post>> handle(GetAllPostsInsideOfACategoryCommand getAllCategoriesCommand) {
        var allPosts = QueryProvider.getAll(Post.class);
        var posts = new ArrayList<Post>();

        for (var post : allPosts) {
            if(post.getCategoryId().equals(getAllCategoriesCommand.categoryId())){
                posts.add(post);
            }
        }

        if (posts.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.CategoryHasNoPosts);
        }

        return CompletableFuture.completedFuture(posts);
    }
}
