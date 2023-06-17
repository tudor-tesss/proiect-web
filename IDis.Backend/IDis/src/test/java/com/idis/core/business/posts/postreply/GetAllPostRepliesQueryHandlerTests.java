package com.idis.core.business.posts.postreply;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.postreply.queries.GetAllPostRepliesQuery;
import com.idis.core.business.posts.postreply.queryhandlers.GetAllPostRepliesQueryHandler;
import com.idis.core.domain.posts.postreply.PostReply;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllPostRepliesQueryHandlerTests {
    @Test
    public void when_noRepliesFound_then_shouldFail() {
        // Arrange
        var command = new GetAllPostRepliesQuery(UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(PostReply.class)).thenReturn(CompletableFuture.completedFuture(List.of()));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.PostReply.NoRepliesFound));
        }
    }

    @Test
    public void when_repliesFound_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var reply = PostReply.create(UUID.randomUUID(), UUID.randomUUID(), "content", "body", new HashMap<>());
        var command = new GetAllPostRepliesQuery(reply.getParentPostId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(PostReply.class)).thenReturn(CompletableFuture.completedFuture(List.of(reply)));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertTrue(result.size() > 0);
        }
    }

    private GetAllPostRepliesQueryHandler sut() {
        return new GetAllPostRepliesQueryHandler();
    }
}
