package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsDocbookCommand;
import com.idis.core.business.statistics.category.extensions.CategoryStatisticsCalculator;
import com.idis.core.business.statistics.category.extensions.DocbookGeneratorForCategories;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public final class ExportCategoryStatisticsAsDocbookCommandHandler implements IRequestHandler<ExportCategoryStatisticsAsDocbookCommand, String> {
    @Override
    public CompletableFuture<String> handle(ExportCategoryStatisticsAsDocbookCommand request) {
        String docbookResult = "";
        var category = QueryProvider.getById(Category.class, request.categoryId());

        if (category.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Category.CategoryNotFound);
        }

        var categoryStatisticsCalculator = new CategoryStatisticsCalculator(category.get());
        var docbookGeneratorForCategories = new DocbookGeneratorForCategories(categoryStatisticsCalculator);

        try {
            docbookResult = docbookGeneratorForCategories.generateDocbook();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(docbookResult);
    }
}
