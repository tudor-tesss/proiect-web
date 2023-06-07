package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsCsvCommand;
import com.idis.core.business.statistics.category.extensions.CsvGeneratorForCategories;
import com.idis.core.business.statistics.category.extensions.CategoryStatisticsCalculator;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class ExportCategoryStatisticsAsCsvCommandHandler implements IRequestHandler<ExportCategoryStatisticsAsCsvCommand, String> {
    @Override
    public CompletableFuture<String> handle(ExportCategoryStatisticsAsCsvCommand request) {
        var result = "";
        var category = QueryProvider.getById(Category.class, request.categoryId());

        if (category.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Category.CategoryNotFound);
        }

        var categoryStatisticsCalculator = new CategoryStatisticsCalculator(category.get());
        var csvGeneratorForCategories = new CsvGeneratorForCategories(categoryStatisticsCalculator);

        try {
            result = csvGeneratorForCategories.generateCsv();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(result);
    }
}
