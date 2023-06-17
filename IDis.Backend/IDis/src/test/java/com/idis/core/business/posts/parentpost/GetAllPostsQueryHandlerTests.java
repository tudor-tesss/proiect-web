package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetAllPostsQuery;
import com.idis.core.business.posts.parentpost.queryhandlers.GetAllPostsInsideOfACategoryQueryHandler;
import com.idis.core.business.posts.parentpost.queryhandlers.GetAllPostsQueryHandler;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetAllPostsQueryHandlerTests {
    @Test
    public void when_noPostsAreAvailable_then_shouldFail() {
        // Arrange
        var command = new GetAllPostsQuery();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(List.of());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Post.NoPostsAvailable));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        Map<String,Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(UUID.randomUUID(), UUID.randomUUID(),"post","description", ratings);
        var command = new GetAllPostsQuery();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(List.of(post));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    private GetAllPostsQueryHandler sut() {
        return new GetAllPostsQueryHandler();
    }
}
