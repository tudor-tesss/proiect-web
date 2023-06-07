package com.idis.core.business.posts.postreply.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.postreply.command.CreatePostReplyCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.posts.postreply.PostReply;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static com.idis.shared.functional.FunctionalExtensions.any;

public final class CreatePostReplyCommandHandler implements IRequestHandler<CreatePostReplyCommand, PostReply> {
    @Override
    public CompletableFuture<PostReply> handle(CreatePostReplyCommand createPostReplyCommand) {
        var users = QueryProvider.getById(User.class, createPostReplyCommand.authorId());
        if(users.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.PostReply.UserDoesNotExist);
        }

        var posts = QueryProvider.getById(Post.class, createPostReplyCommand.parentPostId());
        if(posts.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.PostReply.PostDoesNotExist);
        }

        var post = posts.get();

        var postRatings = post.getRatings().keySet();
        var parentPostRatings = new ArrayList<>(postRatings);
        Collections.sort(parentPostRatings);

        var postReplyRatings = createPostReplyCommand.ratings().keySet();
        var replyRatings = new ArrayList<>(postReplyRatings);
        Collections.sort(replyRatings);

        var ratingsMatch = parentPostRatings.equals(replyRatings);
        if(!ratingsMatch) {
            throw new IllegalArgumentException(BusinessErrors.PostReply.RatingsDoNotMatch);
        }

        try {
            var reply = PostReply.create(createPostReplyCommand.authorId(), createPostReplyCommand.parentPostId(), createPostReplyCommand.title(), createPostReplyCommand.body(), createPostReplyCommand.ratings());
            QueryProvider.insert(reply);

            return CompletableFuture.completedFuture(reply);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
