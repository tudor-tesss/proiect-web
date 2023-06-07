package com.idis.core.business.posts.parentpost;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.posts.parentpost.command.GetAllPostsInsideOfACategoryCommand;
import com.idis.core.business.posts.parentpost.commandhandlers.GetAllPostsInsideOfACategoryCommandHandler;
import com.idis.core.domain.category.Category;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetAllPostsInsideOfACategoryCommandHandlerTests {

    @Test
    public void when_categoryDoesNotHavePosts_then_shouldFail() {
        // Arrange
        var command = command();
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var post = Post.create(UUID.randomUUID(),UUID.randomUUID(),"post","description",);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of(category));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Category.CreatorHasNoCategories));
        }
    }

    private GetAllPostsInsideOfACategoryCommandHandler sut(){return new GetAllPostsInsideOfACategoryCommandHandler();}
    private GetAllPostsInsideOfACategoryCommand command(){
        return new GetAllPostsInsideOfACategoryCommand(UUID.randomUUID());
    }
}
