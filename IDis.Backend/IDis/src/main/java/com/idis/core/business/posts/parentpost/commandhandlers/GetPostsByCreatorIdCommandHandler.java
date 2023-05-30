package com.idis.core.business.posts.parentpost.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetPostByIdCommand;
import com.idis.core.business.posts.parentpost.command.GetPostsByCreatorIdCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetPostsByCreatorIdCommandHandler implements IRequestHandler<GetPostsByCreatorIdCommand, List<Post>> {

    @Override
    public CompletableFuture<List<Post>> handle(GetPostsByCreatorIdCommand getPostsByCreatorIdCommand) {

        var creatorId = getPostsByCreatorIdCommand.creatorId();
        var allPosts = NimbleJQueryProvider.getAll(Post.class);
        List<Post> posts = new ArrayList<>();

        for(Post post : allPosts) {
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
