package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsCsvCommand;
import com.idis.core.business.statistics.posts.extensions.CsvGeneratorForPosts;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class ExportPostStatisticsAsCsvCommandHandler implements IRequestHandler<ExportPostStatisticsAsCsvCommand, String> {
    @Override
    public CompletableFuture<String> handle(ExportPostStatisticsAsCsvCommand request) {
        var csv = "";
        var post = QueryProvider.getById(Post.class, request.postId());

        if (post.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
        }

        var postStatisticsCalculator = new PostStatisticsCalculator(post.get());
        var csvGeneratorForPosts = new CsvGeneratorForPosts(postStatisticsCalculator);

        try {
            csv = csvGeneratorForPosts.generateCsv();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(csv);
    }
}
