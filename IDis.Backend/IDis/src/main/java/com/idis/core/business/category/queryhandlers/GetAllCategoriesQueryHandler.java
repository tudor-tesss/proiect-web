package com.idis.core.business.category.queryhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.queries.GetAllCategoriesQuery;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllCategoriesQueryHandler implements IRequestHandler<GetAllCategoriesQuery, List<Category>> {
    @Override
    public CompletableFuture<List<Category>> handle (GetAllCategoriesQuery getAllCategoriesCommand){
        var categories = QueryProvider.getAll(Category.class);

        if(categories.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Category.NoCategoriesInDatabase);
        }

        return CompletableFuture.completedFuture(categories);
    }
}
