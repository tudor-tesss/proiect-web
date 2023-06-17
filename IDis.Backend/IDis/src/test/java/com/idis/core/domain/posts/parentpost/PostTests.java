package com.idis.core.domain.posts.parentpost;

import com.idis.core.domain.DomainErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_titleIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String title) {
        // Arrange
        var authorId = UUID.randomUUID();
        var categoryId = UUID.randomUUID();
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Post.create(authorId, categoryId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.Post.TitleNullOrEmpty));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_bodyIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String body) {
        // Arrange
        var authorId = UUID.randomUUID();
        var categoryId = UUID.randomUUID();
        var title = "title";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Post.create(authorId, categoryId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.Post.BodyNullOrEmpty));
    }

    @Test
    public void given_create_when_ratingValueIsOutOfInterval_then_shouldThrowIllegalArgumentException() {
        // Arrange
        var authorId = UUID.randomUUID();
        var categoryId = UUID.randomUUID();
        var title = "title";
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 60000);
        }};

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            Post.create(authorId, categoryId, title, body, ratings);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.Post.RatingValueOutOfInterval));
    }

    @Test
    public void given_create_when_notViolatingConstraints_then_shouldCreate() {
        // Arrange
        var authorId = UUID.randomUUID();
        var categoryId = UUID.randomUUID();
        var title = "title";
        var body = "body";
        var ratings = new HashMap<String, Integer>() {{
            put("rat1", 1);
            put("rat2", 2);
        }};

        // Act
        var post = Post.create(authorId, categoryId, title, body, ratings);

        // Assert
        assertTrue(post != null);
        assertTrue(post.getAuthorId().equals(authorId));
        assertTrue(post.getCategoryId().equals(categoryId));
        assertTrue(post.getTitle().equals(title));
        assertTrue(post.getBody().equals(body));
        assertTrue(post.getRatings().equals(ratings));
    }
}
