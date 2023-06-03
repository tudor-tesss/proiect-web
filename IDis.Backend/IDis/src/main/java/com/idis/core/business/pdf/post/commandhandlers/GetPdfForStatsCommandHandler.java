package com.idis.core.business.pdf.post.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.pdf.post.commands.GetPdfForPostStatsCommand;
import com.idis.core.business.pdf.post.extensions.PdfGeneratorForPosts;
import com.idis.core.business.statistics.posts.extensions.PostStatisticsCalculator;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class GetPdfForStatsCommandHandler implements IRequestHandler<GetPdfForPostStatsCommand, byte[]> {
    public CompletableFuture<byte[]> handle (GetPdfForPostStatsCommand getPdfForPostStatsCommand){
        byte[] pdfResult={};
        var posts = NimbleJQueryProvider.getAll(Post.class);
        var postResult = posts.stream().filter(p -> p.getId().equals(getPdfForPostStatsCommand.postId())).findFirst();

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
