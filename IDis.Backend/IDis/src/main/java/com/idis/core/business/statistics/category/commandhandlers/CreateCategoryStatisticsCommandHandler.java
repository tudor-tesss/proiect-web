package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commandresponses.CreateCategoryStatisticsCommandResponse;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.idis.core.business.statistics.category.extensions.CategoryStatisticsCalculator;
import com.idis.core.domain.category.Category;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;

public final class CreateCategoryStatisticsCommandHandler implements IRequestHandler<CreateCategoryStatisticsCommand, CreateCategoryStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreateCategoryStatisticsCommandResponse> handle(CreateCategoryStatisticsCommand createCategoryStatisticsCommand) {
        var categoryResult = NimbleJQueryProvider
                .getAll(Category.class)
                .stream()
                .filter(category -> category.getId().equals(createCategoryStatisticsCommand.categoryId()))
                .findFirst();
        if (categoryResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.CategoryStatistics.CategoryDoesNotExist);
        }

        var statisticsCalculator = new CategoryStatisticsCalculator(categoryResult.get());

        return CompletableFuture.completedFuture(new CreateCategoryStatisticsCommandResponse(statisticsCalculator.calculate()));
    }
}