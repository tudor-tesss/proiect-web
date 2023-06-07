package com.idis.core.business.statistics.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commandhandlers.ExportCategoryStatisticsAsPdfCommandHandler;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsPdfCommand;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ExportCategoryStatisticsAsPdfCommandHandlerTests {
    @Test
    public void given_export_when_categoryDoesntExist_then_shouldThrowIllegalArgumentException() {
        // Arrange
        UUID categoryId = UUID.randomUUID();
        var command = command(categoryId);

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(new ArrayList<Category>());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(BusinessErrors.Category.CategoryNotFound));
        }
    }

    @Test
    public void given_export_when_categoryExists_then_shouldSucceed() {
        // Arrange
        List<String> ratings = new ArrayList<>();
        ratings.add("rat1");
        ratings.add("rat2");
        var category = Category.create("categoryName",ratings, UUID.randomUUID());
        var command = command(category.getId());
        List<Category> categories = new ArrayList<>();
        categories.add(category);

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getAll(Category.class)).thenReturn(categories);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private ExportCategoryStatisticsAsPdfCommandHandler sut() {
        return new ExportCategoryStatisticsAsPdfCommandHandler();
    }
    private ExportCategoryStatisticsAsPdfCommand command(UUID categoryId) {
        return new ExportCategoryStatisticsAsPdfCommand(categoryId);
    }
}
