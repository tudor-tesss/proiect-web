package com.idis.core.business.posts.postreply;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.idis.core.business.posts.postreply.command.CreatePostReplyCommand;
import com.idis.core.business.posts.postreply.commandhandlers.CreatePostReplyCommandHandler;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatePostReplyCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new CreatePostReplyCommand(UUID.randomUUID(), UUID.randomUUID(), "title", "body", new HashMap<>());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.PostReply.UserDoesNotExist));
        }
    }

    @Test
    public void when_parentPostDoesNotExist_then_shouldFail() {
        // Arrange
        var user = User.create("username", "name", "email");
        var command = new CreatePostReplyCommand(user.getId(), UUID.randomUUID(), "title", "body", new HashMap<>());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Category.class, command.parentPostId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.PostReply.PostDoesNotExist));
        }
    }

    @Test
    public void when_ratingsDoNotMatch_then_shouldFail() {
        // Arrange
        var user = User.create("username", "name", "email");
        var categoryRatings = new HashMap<String, Integer>();
        categoryRatings.put("rat1", 1);
        var post = Post.create(user.getId(), UUID.randomUUID(), "title", "body", new HashMap<>());

        var postRatings = new HashMap<String, Integer>();
        postRatings.put("rat1", 1);
        var command = new CreatePostReplyCommand(user.getId(), post.getId(), "title", "body", postRatings);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Post.class, command.parentPostId())).thenReturn((Optional.of(post)));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.PostReply.RatingsDoNotMatch));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() {
        // Arrange
        var user = User.create("username", "name", "email");
        var postRatings = new HashMap<String, Integer>();
        postRatings.put("rat1", 1);
        var post = Post.create(user.getId(), UUID.randomUUID(), "title", "body", postRatings);

        var command = new CreatePostReplyCommand(user.getId(), post.getId(), "title", "body", postRatings);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Post.class, command.parentPostId())).thenReturn((Optional.of(post)));

            // Act
            sut().handle(command);

            // Assert
            assertTrue(true);
        }
    }

    private CreatePostReplyCommandHandler sut() {
        return new CreatePostReplyCommandHandler();
    }
}
