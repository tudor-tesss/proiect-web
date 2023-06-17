package com.idis.core.business.statistics.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commandhandlers.CreateCategoryStatisticsCommandHandler;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCategoryStatisticsCommandHandlerTests {
    @Test
    public void when_categoryDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new CreateCategoryStatisticsCommand(UUID.randomUUID());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(Category.class, command.categoryId())).thenReturn((Optional.empty()));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.CategoryStatistics.CategoryDoesNotExist));
        }
    }

    @Test
    public void when_categoryExists_then_shouldSucceed() {
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var category = Category.create("categoryName",ratings, UUID.randomUUID());
        var command = new CreateCategoryStatisticsCommand(category.getId());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getById(Category.class, command.categoryId())).thenReturn((Optional.of(category)));

            // Act
            var res = sut().handle(command).get();

            // Assert
            assertNotNull(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CreateCategoryStatisticsCommandHandler sut() {
        return new CreateCategoryStatisticsCommandHandler();
    }
}
