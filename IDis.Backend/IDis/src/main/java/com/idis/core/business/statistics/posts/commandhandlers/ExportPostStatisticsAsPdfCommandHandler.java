package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsPdfCommand;
import com.idis.core.business.statistics.posts.extensions.PdfGeneratorForPosts;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class ExportPostStatisticsAsPdfCommandHandler implements IRequestHandler<ExportPostStatisticsAsPdfCommand, byte[]> {
    @Override
    public CompletableFuture<byte[]> handle (ExportPostStatisticsAsPdfCommand exportPostStatisticsAsPdfCommand){
        byte[] pdfResult = {};
        var posts = QueryProvider.getAll(Post.class);
        var postResult = posts.stream().filter(p -> p.getId().equals(exportPostStatisticsAsPdfCommand.postId())).findFirst();

        if (postResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
        }
        var postStatisticsCalculator = new PostStatisticsCalculator(postResult.get());
        var pdfGeneratorForPosts = new PdfGeneratorForPosts(postStatisticsCalculator);

        try {
            pdfResult = pdfGeneratorForPosts.generatePdfForPostStats();
        } catch (Exception e){
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(pdfResult);
    }
}
