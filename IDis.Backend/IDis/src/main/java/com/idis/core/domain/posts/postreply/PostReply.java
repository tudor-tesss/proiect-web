package com.idis.core.domain.posts.postreply;

import com.idis.core.domain.DomainErrors;
import com.nimblej.core.BaseObject;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public final class PostReply extends BaseObject {
    private UUID authorID;
    private UUID parentPostId;
    private String title;
    private String body;
    private Map<String, Integer> ratings;
    private Date createdAt;

    public PostReply() {}

    private PostReply(UUID authorID, UUID parentPostId, String title, String body, Map<String, Integer> ratings) {
        super();
        this.authorID = authorID;
        this.parentPostId = parentPostId;
        this.title = title;
        this.body = body;
        this.ratings = ratings;
        this.createdAt = new Date();
    }

    public static PostReply create(UUID authorId, UUID parentPostId, String title, String body, Map<String, Integer> ratings) throws IllegalArgumentException {
        var titleResult = title != null && !title.isBlank();
        if(!titleResult) {
            throw new IllegalArgumentException(DomainErrors.PostReply.TitleNullOrEmpty);
        }

        var bodyResult = body != null && !body.isBlank();
        if(!bodyResult) {
            throw new IllegalArgumentException(DomainErrors.PostReply.BodyNullOrEmpty);
        }

        ratings.forEach((key, value) -> {
            if(value<1 || value>5) {
                throw new IllegalArgumentException(DomainErrors.PostReply.RatingValueOutOfInterval);
            }
        });
        return new PostReply(authorId, parentPostId, title, body, ratings);
    }

    public UUID getAuthorID() { return authorID; }

    public UUID parentPostId() { return parentPostId; }

    public String getTitle() { return title; }

    public String getBody() { return body; }

    public Map<String, Integer> getRatings() { return ratings; }

    public Date getCreatedAt() { return createdAt; }
}
