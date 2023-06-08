package com.idis.core.business.statistics.posts.extensions;

import java.util.stream.Collectors;

public final class CsvGeneratorForPosts {
    private PostStatisticsCalculator calculator;

    public CsvGeneratorForPosts(PostStatisticsCalculator calculator) {
        this.calculator = calculator;
    }

    public String generateCsv() {
        var statistics = calculator.calculate();
        var sb = new StringBuilder();

        sb.append("Post ID,Title,Replies Count,Average Score")
                .append("\n")
                .append(statistics.postId()).append(",")
                .append("\"").append(statistics.title()).append("\"").append(",")
                .append(statistics.repliesCount()).append(",")
                .append(statistics.averageScore());

        var averageScoresByRating = statistics.averageScoreByRating().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

        sb.append(",\"").append(averageScoresByRating).append("\"");

        return sb.toString();
    }
}
