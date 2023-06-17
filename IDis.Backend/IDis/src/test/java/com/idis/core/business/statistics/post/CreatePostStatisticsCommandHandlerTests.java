package com.idis.core.business.statistics.post;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.commandhandlers.CreatePostStatisticsCommandHandler;
import com.idis.core.business.statistics.posts.commands.CreatePostStatisticsCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreatePostStatisticsCommandHandlerTests {
    @Test
    public void when_postDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new CreatePostStatisticsCommand(UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(Post.class, command.postId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.PostStatistics.PostDoesNotExist));
        }
    }

    @Test
    public void when_postExists_then_shouldSucceed() {
        // Arrange
        var post = Post.create(UUID.randomUUID(), UUID.randomUUID(), "title", "content", new HashMap<>());
        var command = new CreatePostStatisticsCommand(post.getId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(Post.class, command.postId())).thenReturn((Optional.of(post)));

            // Act
            var res = sut().handle(command).get();

            // Assert
            assertNotNull(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CreatePostStatisticsCommandHandler sut() {
        return new CreatePostStatisticsCommandHandler();
    }
}
