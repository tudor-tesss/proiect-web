package com.idis.core.business.statistics.category.extensions;

public final class DocbookGeneratorForCategories {
    private CategoryStatisticsCalculator calculator;

    public DocbookGeneratorForCategories(CategoryStatisticsCalculator calculator) {
        this.calculator = calculator;
    }

    public String generateDocbook() {
        var statistics = calculator.calculate();
        var sb = new StringBuilder();

        sb.append("<article>")
                .append("<title>").append(statistics.categoryName()).append("</title>")
                .append("<section><title>Statistics</title>")
                .append("<para>Post Count: ").append(statistics.postCount()).append("</para>")
                .append("<para>Average Score: ").append(statistics.averageScore()).append("</para>")
                .append("<section><title>Average Score per Rating</title>");

        for (var entry : statistics.averageScorePerRating().entrySet()) {
            sb.append("<para>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</para>");
        }

        sb.append("</section>").append("<section><title>Post Count by Day</title>");

        for (var entry : statistics.postCountByDay().entrySet()) {
            sb.append("<para>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</para>");
        }

        sb.append("</section></section></article>");

        return sb.toString();
    }
}
