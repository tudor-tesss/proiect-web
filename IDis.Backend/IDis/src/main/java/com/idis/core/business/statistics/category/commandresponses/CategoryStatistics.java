package com.idis.core.business.statistics.category.commandresponses;

import com.idis.core.domain.posts.parentpost.Post;

import java.util.Map;
import java.util.UUID;

public record CategoryStatistics(
        UUID categoryId,
        String categoryName,
        int postCount,
        double averageScore,
        Map<String, Map<String, Integer>> postsByRatings,
        Map<String, Double> averageScorePerRating,
        Map<Double, Post> postsByAverageScore,
        Map<String, String> postsAndDays,
        Map<String, Integer> postCountByDay
) { }