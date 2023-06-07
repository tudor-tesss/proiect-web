package com.idis.core.business.statistics.posts.extensions;

public final class DocbookGeneratorForPosts {
    private PostStatisticsCalculator calculator;

    public DocbookGeneratorForPosts(PostStatisticsCalculator calculator) {
        this.calculator = calculator;
    }

    public String generateDocbook() {
        var statistics = calculator.calculate();
        var sb = new StringBuilder();

        sb.append("<article>")
                .append("<title>").append(statistics.title()).append("</title>")
                .append("<section><title>Statistics</title>")
                .append("<para>Replies Count: ").append(statistics.repliesCount()).append("</para>")
                .append("<para>Average Score: ").append(statistics.averageScore()).append("</para>")
                .append("<section><title>Average Score by Rating</title>");

        for (var entry : statistics.averageScoreByRating().entrySet()) {
            sb.append("<para>").append(entry.getKey()).append(": ").append(entry.getValue()).append("</para>");
        }

        sb.append("</section></section></article>");

        return sb.toString();
    }
}
