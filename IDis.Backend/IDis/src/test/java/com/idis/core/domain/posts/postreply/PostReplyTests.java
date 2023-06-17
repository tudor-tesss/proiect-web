package com.idis.core.domain.posts.postreply;

import com.idis.core.domain.DomainErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostReplyTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_titleIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String title) {
        // Arrange
        var authorId = UUID.randomUUID();
        var parentPostId = UUID.randomUUID();
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            PostReply.create(authorId, parentPostId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.PostReply.TitleNullOrEmpty));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_bodyIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String body) {
        // Arrange
        var authorId = UUID.randomUUID();
        var parentPostId = UUID.randomUUID();
        var title = "title";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            PostReply.create(authorId, parentPostId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.PostReply.BodyNullOrEmpty));
    }

    @Test
    public void given_create_when_ratingValueIsOutOfInterval_then_shouldThrowIllegalArgumentException() {
        // Arrange
        var authorId = UUID.randomUUID();
        var parentPostId = UUID.randomUUID();
        var title = "title";
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 60000);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            PostReply.create(authorId, parentPostId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.PostReply.RatingValueOutOfInterval));
    }

    @Test
    public void given_create_when_notViolatingConstraints_then_shouldCreate() {
        // Arrange
        var authorId = UUID.randomUUID();
        var parentPostId = UUID.randomUUID();
        var title = "title";
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var postReply = PostReply.create(authorId, parentPostId, title, body, ratings);

        // Assert
        assertTrue(postReply != null);
        assertTrue(postReply.getAuthorID().equals(authorId));
        assertTrue(postReply.getParentPostId().equals(parentPostId));
        assertTrue(postReply.getTitle().equals(title));
        assertTrue(postReply.getBody().equals(body));
        assertTrue(postReply.getRatings().equals(ratings));
    }
}
