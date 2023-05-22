package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commandresponses.CreateCategoryStatisticsCommandResponse;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class CreateCategoryStatisticsCommandHandler implements IRequestHandler<CreateCategoryStatisticsCommand, CreateCategoryStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreateCategoryStatisticsCommandResponse> handle(CreateCategoryStatisticsCommand createCategoryStatisticsCommand) {
        var categoryResult = NimbleJQueryProvider
                .getAll(Category.class)
                .stream()
                .filter(category -> category.getId().equals(createCategoryStatisticsCommand.categoryId()))
                .findFirst();
        if (categoryResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.CategoryStatistics.CategoryDoesNotExist);
        }

        var postsInCategory = NimbleJQueryProvider
                .getAll(Post.class)
                .stream()
                .filter(p -> p.getCategoryId().equals(createCategoryStatisticsCommand.categoryId()))
                .toList();

        var postCount = postsInCategory.size();

        var postsByRatings = new HashMap<String, Map<String, Integer>>();
        for (var rating : categoryResult.get().getRatingFields()) {
            var postsAndRatings = new HashMap<String, Integer>();
            for (var post : postsInCategory) {
                postsAndRatings.put(post.getTitle(), post.getRatings().get(rating));
            }

            postsByRatings.put(rating, postsAndRatings);
        }

        var averageScorePerRating = new HashMap<String, Double>();
        for (var rating : categoryResult.get().getRatingFields()) {
            var sum = 0.0;
            for (var post : postsInCategory) {
                sum += post.getRatings().get(rating);
            }

            averageScorePerRating.put(rating, sum / postCount);
        }

        var postsByAverageScore = new HashMap<String, Double>();
        for (var post : postsInCategory) {
            var sum = 0.0;
            for (var rating : categoryResult.get().getRatingFields()) {
                sum += post.getRatings().get(rating);
            }

            postsByAverageScore.put(post.getTitle(), sum / categoryResult.get().getRatingFields().size());
        }

        var averageScore = averageScorePerRating
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        var statistics = new CreateCategoryStatisticsCommandResponse.CategoryStatistics(
                postCount,
                averageScore,
                postsByRatings,
                averageScorePerRating,
                postsByAverageScore
        );

        return CompletableFuture.completedFuture(new CreateCategoryStatisticsCommandResponse(categoryResult.get().getName(), statistics));
    }
}