package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetAllPostsInsideOfACategoryCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllPostsInsideOfACategoryCommandHandler  implements IRequestHandler<GetAllPostsInsideOfACategoryCommand, List<Post>> {

    public CompletableFuture<List<Post>> handle(GetAllPostsInsideOfACategoryCommand getAllCategoriesCommand) {

        var categoryId = getAllCategoriesCommand.categoryId();
        var allPosts = NimbleJQueryProvider
                .getAll(Post.class);
        List<Post> posts = new ArrayList<>();

        for (Post post: allPosts) {
            if(post.getCategoryId().equals(categoryId)){
                posts.add(post);
            }
        }

        if (posts.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.CategoryHasNoPosts);
        }

        return CompletableFuture.completedFuture(posts);
    }
}
