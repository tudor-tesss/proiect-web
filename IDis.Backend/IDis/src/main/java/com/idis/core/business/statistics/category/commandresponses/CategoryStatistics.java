package com.idis.core.business.statistics.category.commandresponses;

import java.util.Map;
import java.util.UUID;

public record CategoryStatistics(
        UUID categoryId,
        String categoryName,
        int postCount,
        double averageScore,
        Map<String, Map<String, Integer>> postsByRatings,
        Map<String, Double> averageScorePerRating,
        Map<String, Double> postsByAverageScore,
        Map<String, String> postsAndDays,
        Map<String, Integer> postCountByDay
) { }