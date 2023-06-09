package com.idis.core.business.posts.postreply.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.postreply.command.GetAllPostRepliesCommand;
import com.idis.core.domain.posts.postreply.PostReply;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public final class GetAllPostRepliesCommandHandler implements IRequestHandler<GetAllPostRepliesCommand, List<PostReply>> {
    @Override
    public CompletableFuture<List<PostReply>> handle(GetAllPostRepliesCommand getAllPostRepliesCommand) {
        var postId = getAllPostRepliesCommand.postId();

        return QueryProvider.getAllAsync(PostReply.class).thenApply(allReplies -> {
            var replies = allReplies.stream()
                    .filter(reply -> reply.getParentPostId().equals(postId))
                    .collect(Collectors.toList());

            if(replies.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.PostReply.NoRepliesFound);
            }

            return replies;
        });
    }
}
