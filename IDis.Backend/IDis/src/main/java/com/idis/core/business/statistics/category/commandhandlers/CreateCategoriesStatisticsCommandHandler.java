package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.statistics.category.commandresponses.CategoryStatistics;
import com.idis.core.business.statistics.category.commandresponses.CreateCategoriesStatisticsCommandResponse;
import com.idis.core.business.statistics.category.commands.CreateCategoriesStatisticsCommand;
import com.idis.core.business.statistics.category.extensions.CategoryStatisticsCalculator;
import com.idis.core.domain.category.Category;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public final class CreateCategoriesStatisticsCommandHandler implements IRequestHandler<CreateCategoriesStatisticsCommand, CreateCategoriesStatisticsCommandResponse> {
    @Override
    public CompletableFuture<CreateCategoriesStatisticsCommandResponse> handle(CreateCategoriesStatisticsCommand createCategoriesStatisticsCommand) {
        var categories = NimbleJQueryProvider.getAll(Category.class);
        var statistics = new ArrayList<CategoryStatistics>();

        for (var category : categories) {
            var statisticsCalculator = new CategoryStatisticsCalculator(category);

            statistics.add(statisticsCalculator.calculate());
        }

        return CompletableFuture.completedFuture(new CreateCategoriesStatisticsCommandResponse(statistics));
    }
}
