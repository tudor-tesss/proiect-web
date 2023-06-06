package com.idis.core.business.posts.postreply.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.postreply.command.GetAllPostRepliesCommand;
import com.idis.core.domain.posts.postreply.PostReply;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllPostRepliesCommandHandler implements IRequestHandler<GetAllPostRepliesCommand, List<PostReply>> {
    public CompletableFuture<List<PostReply>> handle(GetAllPostRepliesCommand getAllPostRepliesCommand) {
        var postId = getAllPostRepliesCommand.postId();
        var allReplies = QueryProvider.getAll(PostReply.class);
        var replies = new ArrayList<PostReply>();

        for(PostReply reply : allReplies) {
            if(reply.getParentPostId().equals(postId)) {
                replies.add(reply);
            }
        }

        if(replies.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.PostReply.NoRepliesFound);
        }

        return CompletableFuture.completedFuture(replies);
    }
}
