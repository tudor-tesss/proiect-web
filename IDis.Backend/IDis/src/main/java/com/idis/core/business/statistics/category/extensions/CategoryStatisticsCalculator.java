package com.idis.core.business.statistics.category.extensions;

import com.idis.core.business.statistics.category.commandresponses.CategoryStatistics;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class CategoryStatisticsCalculator {
    private Category category;

    public CategoryStatisticsCalculator(Category category) {
        this.category = category;
    }

    public CategoryStatistics calculate() {
        var postsInCategory = NimbleJQueryProvider
                .getAll(Post.class)
                .stream()
                .filter(p -> p.getCategoryId().equals(category.getId()))
                .toList();

        var postCount = postsInCategory.size();

        var postsByRatings = new HashMap<String, Map<UUID, Integer>>();
        for (var rating : category.getRatingFields()) {
            var postsAndRatings = new HashMap<UUID, Integer>();
            for (var post : postsInCategory) {
                postsAndRatings.put(post.getId(), post.getRatings().get(rating));
            }

            postsAndRatings = postsAndRatings.entrySet().stream()
                    .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new
                    ));

            postsByRatings.put(rating, postsAndRatings);
        }


        var averageScorePerRating = new HashMap<String, Double>();
        for (var rating : category.getRatingFields()) {
            var sum = 0.0;
            for (var post : postsInCategory) {
                sum += post.getRatings().get(rating);
            }

            averageScorePerRating.put(rating, sum / postCount);
        }

        var postsByAverageScore = new HashMap<UUID, Double>();
        for (var post : postsInCategory) {
            var sum = 0.0;
            for (var rating : category.getRatingFields()) {
                sum += post.getRatings().get(rating);
            }

            postsByAverageScore.put(post.getId(), sum / category.getRatingFields().size());
        }
        postsByAverageScore = postsByAverageScore.entrySet().stream()
                .sorted(Map.Entry.<UUID, Double>comparingByKey().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                ));

        var averageScore = averageScorePerRating
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        var postsAndDays = new HashMap<String, String>();
        for (var post : postsInCategory) {
            var date = new SimpleDateFormat("yyyy-MM-dd").format(post.getCreatedAt());

            postsAndDays.put(post.getTitle(), date);
        }

        var postsByDay = new HashMap<String, Integer>();
        for (var post : postsInCategory) {
            var date = new SimpleDateFormat("yyyy-MM-dd").format(post.getCreatedAt());

            if (postsByDay.containsKey(date)) {
                postsByDay.put(date, postsByDay.get(date) + 1);
            } else {
                postsByDay.put(date, 1);
            }
        }

        return new CategoryStatistics(
                category.getId(),
                category.getName(),
                postCount,
                averageScore,
                postsByRatings,
                averageScorePerRating,
                postsByAverageScore,
                postsAndDays,
                postsByDay
        );
    }
}
