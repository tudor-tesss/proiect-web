package com.idis.core.business.statistics.category.commandresponses;

import java.util.Map;

public record CreateCategoryStatisticsCommandResponse(String categoryName, CategoryStatistics statistics) {
    public record CategoryStatistics(
            int postCount,
            double averageScore,
            Map<String, Map<String, Integer>> postsByRatings,
            Map<String, Double> averageScorePerRating,
            Map<String, Double> postsByAverageScore
    ) { }
}