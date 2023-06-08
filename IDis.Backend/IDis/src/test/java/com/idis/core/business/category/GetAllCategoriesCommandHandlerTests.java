package com.idis.core.business.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commandhandlers.GetAllCategoriesCommandHandler;
import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class GetAllCategoriesCommandHandlerTests {

    @Test
    public void when_categoriesDoNotExist_then_shouldFail() {
        // Arrange
        var command = command();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.Category.NoCategoriesInDatabase));
        }
    }

    @Test
    public void when_categoriesExist_then_shouldSucceed() throws ExecutionException, InterruptedException{
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var category = Category.create("name",ratings,UUID.randomUUID());
        var command = new GetAllCategoriesCommand();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(List.of(category));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    private GetAllCategoriesCommandHandler sut(){return new GetAllCategoriesCommandHandler();}

    private GetAllCategoriesCommand command(){
        return new GetAllCategoriesCommand();
    }
}
