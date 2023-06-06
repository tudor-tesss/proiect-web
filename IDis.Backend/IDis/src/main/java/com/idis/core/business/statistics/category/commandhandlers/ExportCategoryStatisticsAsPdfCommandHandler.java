package com.idis.core.business.statistics.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsPdfCommand;
import com.idis.core.business.statistics.category.extensions.CategoryStatisticsCalculator;
import com.idis.core.business.statistics.category.extensions.PdfGeneratorForCategory;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.concurrent.CompletableFuture;

public class ExportCategoryStatisticsAsPdfCommandHandler implements IRequestHandler<ExportCategoryStatisticsAsPdfCommand, byte[]> {
    public CompletableFuture<byte[]> handle (ExportCategoryStatisticsAsPdfCommand exportCategoryStatisticsAsPdfCommand){
        byte[] pdfResult = {};
        var categories = QueryProvider.getAll(Category.class);
        var categoryResult = categories.stream().filter(p -> p.getId().equals(exportCategoryStatisticsAsPdfCommand.categoryId())).findFirst();

        if (categoryResult.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Category.CategoryDoesNotExist);
        }
        var categoryStatisticsCalculator = new CategoryStatisticsCalculator(categoryResult.get());
        var pdfGeneratorForCategory = new PdfGeneratorForCategory(categoryStatisticsCalculator);

        try {
            pdfResult = pdfGeneratorForCategory.generatePdfForCategoryStats();
        } catch (Exception e){
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(pdfResult);
    }
}
