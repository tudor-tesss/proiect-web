package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetAllPostsInsideOfACategoryCommand;
import com.idis.core.business.posts.parentpost.commandhandlers.GetAllPostsInsideOfACategoryCommandHandler;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetAllPostsInsideOfACategoryCommandHandlerTests {

    @Test
    public void when_categoryDoesNotHavePosts_then_shouldFail() {
        // Arrange
        var command = command();
        Map<String,Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(UUID.randomUUID(),UUID.randomUUID(),"post","description",ratings);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(List.of(post));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Post.CategoryHasNoPosts));
        }
    }

    @Test
    public void when_categoryHasPosts_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange

        Map<String,Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var command = command();
        var post = Post.create(UUID.randomUUID(),command.categoryId(),"post","description",ratings);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(List.of(post));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    private GetAllPostsInsideOfACategoryCommandHandler sut(){return new GetAllPostsInsideOfACategoryCommandHandler();}
    private GetAllPostsInsideOfACategoryCommand command(){
        return new GetAllPostsInsideOfACategoryCommand(UUID.randomUUID());
    }
}
