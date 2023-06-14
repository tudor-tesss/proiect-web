package com.idis.core.business.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.queryhandlers.GetCategoriesByCreatorIdQueryHandler;
import com.idis.core.business.category.queries.GetCategoriesByCreatorIdQuery;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GetCategoriesByCreatorIdQueryHandlerTests {
    @Test
    public void when_creatorDoesNotHaveCategories_then_shouldFail() {
        // Arrange
        var command = command();
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var category = Category.create("name",ratings,UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(Category.class)).thenReturn(CompletableFuture.completedFuture(List.of(category)));

            // Act
            var result = sut().handle(command);

            // Assert
            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            var actualMessage = exception.getCause().getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Category.CreatorHasNoCategories));
        }
    }

    @Test
    public void when_creatorHasCategories_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var command = command();
        var category = Category.create("name",ratings,command.creatorId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAllAsync(Category.class)).thenReturn(CompletableFuture.completedFuture(List.of(category)));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }
    private GetCategoriesByCreatorIdQueryHandler sut(){return new GetCategoriesByCreatorIdQueryHandler();}

    private GetCategoriesByCreatorIdQuery command(){
        return new GetCategoriesByCreatorIdQuery(UUID.randomUUID());
    }
}
