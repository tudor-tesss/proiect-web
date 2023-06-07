package com.idis.core.business.statistics.category.extensions;

import java.util.Map.Entry;

public final class CsvGeneratorForCategories {
    private CategoryStatisticsCalculator calculator;

    public CsvGeneratorForCategories(CategoryStatisticsCalculator calculator) {
        this.calculator = calculator;
    }

    public String generateCsv() {
        var statistics = calculator.calculate();
        StringBuilder sb = new StringBuilder();

        sb.append("Category ID, Category Name, Post Count, Average Score\n")
                .append(statistics.categoryId()).append(", ")
                .append(statistics.categoryName()).append(", ")
                .append(statistics.postCount()).append(", ")
                .append(statistics.averageScore()).append("\n")
                .append("\nAverage Score per Rating\n");

        for (Entry<String, Double> entry : statistics.averageScorePerRating().entrySet()) {
            sb.append(entry.getKey()).append(", ").append(entry.getValue()).append("\n");
        }

        sb.append("\nPost Count by Day\n");

        for (Entry<String, Integer> entry : statistics.postCountByDay().entrySet()) {
            sb.append(entry.getKey()).append(", ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }
}
