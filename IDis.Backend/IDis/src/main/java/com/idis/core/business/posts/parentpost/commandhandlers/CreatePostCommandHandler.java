package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static com.idis.shared.functional.FunctionalExtensions.any;

public final class CreatePostCommandHandler implements IRequestHandler<CreatePostCommand, Post> {
    @Override
    public CompletableFuture<Post> handle(CreatePostCommand createPostCommand) {
        var users = QueryProvider.getAll(User.class);
        var userResult = any(users, user -> user.getId().equals(createPostCommand.authorId()));
        if (!userResult) {
            throw new IllegalArgumentException(BusinessErrors.Post.UserDoesNotExist);
        }

        var categories = QueryProvider.getAll(Category.class);
        var categoryResult = any(categories, category -> category.getId().equals(createPostCommand.categoryId()));
        if (!categoryResult) {
            throw new IllegalArgumentException(BusinessErrors.Post.CategoryDoesNotExist);
        }

        var category = categories.stream().filter(c -> c.getId().equals(createPostCommand.categoryId())).findFirst().get();
        var categoryRatings = category.getRatingFields();
        Collections.sort(categoryRatings);

        var requestRatings = createPostCommand.ratings().keySet();
        var postRatings = new ArrayList<>(requestRatings);
        Collections.sort(postRatings);

        var ratingsMatch = categoryRatings.equals(postRatings);

        if (!ratingsMatch) {
            throw new IllegalArgumentException(BusinessErrors.Post.RatingsDoNotMatch);
        }

        try {
            var post = Post.create(createPostCommand.authorId(), createPostCommand.categoryId(), createPostCommand.title(), createPostCommand.body(), createPostCommand.ratings());
            QueryProvider.insert(post);

            return CompletableFuture.completedFuture(post);
        }
        catch (Exception e){
            throw e;
        }
    }
}
