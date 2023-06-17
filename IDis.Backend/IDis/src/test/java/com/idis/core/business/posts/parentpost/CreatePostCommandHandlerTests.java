package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.CreatePostCommand;
import com.idis.core.business.posts.parentpost.commandhandlers.CreatePostCommandHandler;
import com.idis.core.domain.category.Category;
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

public class CreatePostCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var category = Category.create("name", new ArrayList<>(), UUID.randomUUID());
        var command = new CreatePostCommand(UUID.randomUUID(), category.getId(), "title", "body", new HashMap<>());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
               var actualMessage = exception.getMessage();

               assertTrue(actualMessage.contains(BusinessErrors.Post.UserDoesNotExist));
        }
    }

    @Test
    public void when_categoryDoesNotExist_then_shouldFail() {
        // Arrange
        var user = User.create("username", "name", "email");
        var command = new CreatePostCommand(user.getId(), UUID.randomUUID(), "title", "body", new HashMap<>());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Category.class, command.categoryId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Post.CategoryDoesNotExist));
        }
    }

    @Test
    public void when_ratingsDoNotMatch_then_shouldFail() {
        // Arrange
        var user = User.create("username", "name", "email");
        var categoryRatings = new ArrayList<String>();
        categoryRatings.add("rat1");
        var category = Category.create("name", categoryRatings, user.getId());

        var command = new CreatePostCommand(user.getId(), category.getId(), "title", "body", new HashMap<>());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Category.class, command.categoryId())).thenReturn((Optional.of(category)));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Post.RatingsDoNotMatch));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() {
        // Arrange
        var user = User.create("username", "name", "email");
        var categoryRatings = new ArrayList<String>();
        categoryRatings.add("rat1");
        var category = Category.create("name", categoryRatings, user.getId());

        var postRatings = new HashMap<String, Integer>();
        postRatings.put("rat1", 1);
        var command = new CreatePostCommand(user.getId(), category.getId(), "title", "body", postRatings);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(User.class, command.authorId())).thenReturn((Optional.of(user)));
            mock.when(() -> QueryProvider.getById(Category.class, command.categoryId())).thenReturn((Optional.of(category)));

            // Act
            sut().handle(command);

            // Assert
            assertTrue(true);
        }
    }

    private CreatePostCommandHandler sut() {
        return new CreatePostCommandHandler();
    }

}
