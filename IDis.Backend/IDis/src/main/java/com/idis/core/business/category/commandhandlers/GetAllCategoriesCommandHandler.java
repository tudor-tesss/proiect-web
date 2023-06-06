package com.idis.core.business.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetAllCategoriesCommandHandler implements IRequestHandler<GetAllCategoriesCommand, List<Category>> {
    @Override
    public CompletableFuture<List<Category>> handle (GetAllCategoriesCommand getAllCategoriesCommand){
        var categories = QueryProvider.getAll(Category.class);

        if(categories.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Category.NoCategoriesInDatabase);
        }

        return CompletableFuture.completedFuture(categories);
    }
}
