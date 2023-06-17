package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.queries.GetPostByIdQuery;
import com.idis.core.business.posts.parentpost.queryhandlers.GetPostByIdQueryHandler;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class GetPostByIdQueryHandlerTests {
    @Test
    public void when_postDoesNotExist_then_shouldFail() {
        // Arrange
        var command = command();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(Post.class, command.postId())).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Post.PostNotFound));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        Map<String, Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(UUID.randomUUID(), UUID.randomUUID(),"post","description",ratings);
        var command = new GetPostByIdQuery(post.getId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(Post.class, post.getId())).thenReturn(CompletableFuture.completedFuture(Optional.of(post)));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }
    }

    private GetPostByIdQueryHandler sut(){return new GetPostByIdQueryHandler();}
    private GetPostByIdQuery command(){
        return new GetPostByIdQuery(UUID.randomUUID());
    }
}
