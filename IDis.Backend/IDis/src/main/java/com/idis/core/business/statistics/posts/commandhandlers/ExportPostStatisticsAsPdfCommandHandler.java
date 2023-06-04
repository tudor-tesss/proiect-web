package com.idis.core.business.statistics.posts.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.command.ExportPostStatisticsAsPdfCommand;
import com.idis.core.business.statistics.posts.extensions.PdfGeneratorForPosts;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class ExportPostStatisticsAsPdfCommandHandler implements IRequestHandler<ExportPostStatisticsAsPdfCommand, byte[]> {
    public CompletableFuture<byte[]> handle (ExportPostStatisticsAsPdfCommand exportPostStatisticsAsPdfCommand){
        byte[] pdfResult={};
        var posts = NimbleJQueryProvider.getAll(Post.class);
        var postResult = posts.stream().filter(p -> p.getId().equals(exportPostStatisticsAsPdfCommand.postId())).findFirst();

        if(postResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Post.PostNotFound);
        }
        PostStatisticsCalculator postStatisticsCalculator = new PostStatisticsCalculator(postResult.get());
        PdfGeneratorForPosts pdfGeneratorForPosts = new PdfGeneratorForPosts(postStatisticsCalculator);

        try {
            pdfResult = pdfGeneratorForPosts.generatePdfForPostStats();
        }catch (Exception e){
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(pdfResult);
    }
}
