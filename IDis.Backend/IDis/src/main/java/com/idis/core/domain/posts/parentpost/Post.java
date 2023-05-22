package com.idis.core.domain.posts.parentpost;

import com.idis.core.domain.DomainErrors;
import com.nimblej.core.BaseObject;


import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public final class Post extends BaseObject {
    private UUID authorId;
    private UUID categoryId;
    private String title;
    private String body;
    private Map<String, Integer> ratings;
    private LocalDate createdAt;

    public Post() {}

    private Post(UUID authorId, UUID categoryId, String title, String body, Map<String, Integer> ratings) {
        super();
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.title = title;
        this.body = body;
        this.ratings = ratings;
        this.createdAt = LocalDate.now();
    }

    public static Post create(UUID authorId, UUID categoryId, String title, String body, Map<String, Integer> ratings) throws IllegalArgumentException {
        var titleResult = title != null && !title.isBlank();
        if(!titleResult) {
            throw new IllegalArgumentException(DomainErrors.Post.TitleNullOrEmpty);
        }

        var bodyResult = body != null && !body.isBlank();
        if(!bodyResult) {
            throw new IllegalArgumentException(DomainErrors.Post.BodyNullOrEmpty);
        }

        ratings.forEach((key, value) -> {
            if(value<1 || value>5) {
                throw new IllegalArgumentException(DomainErrors.Post.RatingValueOutOfInterval);
            }
        });
        return new Post(authorId, categoryId, title, body, ratings);
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
