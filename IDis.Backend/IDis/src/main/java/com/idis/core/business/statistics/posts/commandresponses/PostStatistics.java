package com.idis.core.business.statistics.posts.commandresponses;

import java.util.Map;
import java.util.UUID;

public record PostStatistics (
        UUID postId,
        String title,
        int repliesCount,
        double averageScore,
        Map<String, Double> averageScoreByRating
){}
