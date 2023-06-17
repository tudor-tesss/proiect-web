package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetPostByIdQuery;
import com.idis.core.business.posts.parentpost.queries.GetPostsByCreatorIdQuery;
import com.idis.core.business.posts.parentpost.queryhandlers.GetPostsByCreatorIdQueryHandler;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class GetPostsByCreatorIdQueryHandlerTests {
    @Test
    public void when_postDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new GetPostsByCreatorIdQuery(UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(Post.class)).thenReturn(CompletableFuture.completedFuture(List.of()));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Category.CreatorHasNoCategories));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        var userId = UUID.randomUUID();
        Map<String, Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(userId, UUID.randomUUID(),"post","description",ratings);
        var command = new GetPostsByCreatorIdQuery(userId);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(Post.class)).thenReturn(CompletableFuture.completedFuture(List.of(post)));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }
    }

    private GetPostsByCreatorIdQueryHandler sut() {
        return new GetPostsByCreatorIdQueryHandler();
    }
}
