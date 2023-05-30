package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.command.CreatePostStatisticsCommand;
import com.idis.core.business.statistics.posts.commandresponses.CreatePostStatisticsCommandResponse;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class CreatePostStatisticsCommandHandler implements IRequestHandler<CreatePostStatisticsCommand, CreatePostStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreatePostStatisticsCommandResponse> handle(CreatePostStatisticsCommand createPostStatisticsCommand) {
        var postResult = NimbleJQueryProvider
                .getAll(Post.class)
                .stream().filter(p -> p.getId().equals(createPostStatisticsCommand.postId()))
                .findFirst();
        if(postResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.PostStatistics.PostDoesNotExist);
        }

        var statisticsCalculator = new PostStatisticsCalculator(postResult.get());

        return CompletableFuture.completedFuture(new CreatePostStatisticsCommandResponse(statisticsCalculator.calculate()));
    }
}
