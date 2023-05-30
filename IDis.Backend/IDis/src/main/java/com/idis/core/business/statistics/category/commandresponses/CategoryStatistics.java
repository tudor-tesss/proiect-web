package com.idis.core.business.statistics.category.commandresponses;

import com.idis.core.domain.posts.parentpost.Post;

import java.util.Map;
import java.util.UUID;

public record CategoryStatistics(
        UUID categoryId,
        String categoryName,
        int postCount,
        double averageScore,
        Map<String, Map<UUID, Integer>> postsByRatings,
        Map<String, Double> averageScorePerRating,
        Map<UUID, Double> postsByAverageScore,
        Map<String, String> postsAndDays,
        Map<String, Integer> postCountByDay
) { }