package com.idis.core.business.posts.postreply.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.postreply.command.GetAllPostRepliesCommand;
import com.idis.core.domain.posts.postreply.PostReply;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllPostRepliesCommandHandler implements IRequestHandler<GetAllPostRepliesCommand, List<PostReply>> {
    public CompletableFuture<List<PostReply>> handle(GetAllPostRepliesCommand getAllPostRepliesCommand) {

        var postId = getAllPostRepliesCommand.postId();
        var allReplies = NimbleJQueryProvider.getAll(PostReply.class);
        List<PostReply> replies = new ArrayList<>();

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
