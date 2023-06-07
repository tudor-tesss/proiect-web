package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.statistics.posts.commandresponses.CreatePostsStatisticsCommandResponse;
import com.idis.core.business.statistics.posts.commandresponses.PostStatistics;
import com.idis.core.business.statistics.posts.commands.CreatePostsStatisticsCommand;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public final class CreatePostsStatisticsCommandHandler implements IRequestHandler<CreatePostsStatisticsCommand, CreatePostsStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreatePostsStatisticsCommandResponse> handle(CreatePostsStatisticsCommand request) {
        var posts = QueryProvider.getAll(Post.class);
        var statistics = new ArrayList<PostStatistics>();

        for (var post : posts) {
            var calculator = new PostStatisticsCalculator(post);

            statistics.add(calculator.calculate());
        }

        return CompletableFuture.completedFuture(new CreatePostsStatisticsCommandResponse(statistics));
    }
}
