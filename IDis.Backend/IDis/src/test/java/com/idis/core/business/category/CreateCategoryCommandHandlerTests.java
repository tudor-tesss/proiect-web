package com.idis.core.business.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commandhandlers.CreateCategoryCommandHandler;
import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class CreateCategoryCommandHandlerTests {
    @Test
    public void when_nameIsAlreadyTaken_then_shouldFail() {
        // Arrange
        var command = command();
        var category = Category.create(command.name(), command.ratingFields(), command.creatorId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of(category));
            mock.when(() -> QueryProvider.insert(any(Category.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Category.CategoryAlreadyExists));
        }
    }

    @Test
    public void when_domainFails_then_shouldFail() {
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var command = new CreateCategoryCommand(null, ratings, UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of());
            mock.when(() -> QueryProvider.insert(any(Category.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(DomainErrors.Category.NameNullOrEmpty));
        }
    }

    @Test
    public void when_domainSucceeds_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        var command = command();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of());
            mock.when(() -> QueryProvider.insert(any(Category.class))).thenAnswer(invocation -> null);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertTrue(result.getName().equals(command.name()));
            assertTrue(result.getRatingFields().equals(command.ratingFields()));
            assertTrue(result.getCreatorId().equals(command.creatorId()));
        }
    }

    private CreateCategoryCommandHandler sut(){ return new CreateCategoryCommandHandler();}

    private CreateCategoryCommand command(){
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
            return new CreateCategoryCommand("categoryName",ratings,UUID.randomUUID());
    }

}
