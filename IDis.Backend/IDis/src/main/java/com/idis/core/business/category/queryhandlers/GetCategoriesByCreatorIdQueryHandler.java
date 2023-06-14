package com.idis.core.business.category.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.queries.GetCategoriesByCreatorIdQuery;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetCategoriesByCreatorIdQueryHandler implements IRequestHandler<GetCategoriesByCreatorIdQuery, List<Category>> {
    @Override
    public CompletableFuture<List<Category>> handle(GetCategoriesByCreatorIdQuery getCategoriesByCreatorIdCommand) {
        var creatorId = getCategoriesByCreatorIdCommand.creatorId();

        return QueryProvider.getAllAsync(Category.class).thenApply(allCategories -> {
            var categories = allCategories.stream()
                    .filter(category -> category.getCreatorId().equals(creatorId))
                    .toList();

            if(categories.isEmpty()) {
                throw new IllegalArgumentException(BusinessErrors.Category.CreatorHasNoCategories);
            }

            return categories;
        });
    }
}
