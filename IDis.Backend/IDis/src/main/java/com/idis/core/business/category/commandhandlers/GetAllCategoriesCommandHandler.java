package com.idis.core.business.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.domain.category.Category;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
public final class GetAllCategoriesCommandHandler implements IRequestHandler<GetAllCategoriesCommand, List<Category>> {

    @Override
    public CompletableFuture<List<Category>> handle (GetAllCategoriesCommand getAllCategoriesCommand){

        var categories = NimbleJQueryProvider.getAll(Category.class);

        if(categories.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Category.NoCategoriesInDatabase);
        }

        return CompletableFuture.completedFuture(categories);
    }
}
