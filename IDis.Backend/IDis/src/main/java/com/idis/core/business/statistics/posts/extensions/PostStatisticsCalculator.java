package com.idis.core.business.statistics.posts.extensions;

import com.idis.core.business.statistics.posts.commandresponses.PostStatistics;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.posts.postreply.PostReply;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.HashMap;

public final class PostStatisticsCalculator {
    private Post post;

    public PostStatisticsCalculator(Post post) {this.post = post;}
    public PostStatistics calculate() {
        var repliesInPost = NimbleJQueryProvider
                .getAll(PostReply.class)
                .stream()
                .filter(r -> r.getParentPostId().equals(post.getId()))
                .toList();

        var repliesCount = repliesInPost.size();

        var averageScoreByRating = new HashMap<String, Double>();
        for(var rating : post.getRatings().keySet()) {
            var sum = 0.0;
            for(var reply : repliesInPost) {
                sum += reply.getRatings().get(rating);
            }

            averageScoreByRating.put(rating, sum / repliesCount);
        }

        var averageScore = averageScoreByRating
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new PostStatistics(post.getId(), post.getTitle(), repliesCount, averageScore, averageScoreByRating);
    }
}
