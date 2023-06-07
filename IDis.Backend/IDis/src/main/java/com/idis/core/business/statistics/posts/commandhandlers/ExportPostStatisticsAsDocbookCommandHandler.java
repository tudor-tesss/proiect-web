package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsDocbookCommand;
import com.idis.core.business.statistics.posts.extensions.DocbookGeneratorForPosts;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class ExportPostStatisticsAsDocbookCommandHandler implements IRequestHandler<ExportPostStatisticsAsDocbookCommand, String> {
    @Override
    public CompletableFuture<String> handle(ExportPostStatisticsAsDocbookCommand request) {
        String docbookResult = "";
        var post = QueryProvider.getById(Post.class, request.postId());

        if (post.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
        }

        var postStatisticsCalculator = new PostStatisticsCalculator(post.get());
        var docbookGeneratorForPosts = new DocbookGeneratorForPosts(postStatisticsCalculator);

        try {
            docbookResult = docbookGeneratorForPosts.generateDocbook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(docbookResult);
    }
}
