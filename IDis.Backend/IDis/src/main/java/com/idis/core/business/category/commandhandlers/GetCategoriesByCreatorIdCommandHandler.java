package com.idis.core.business.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.business.category.commands.GetCategoriesByCreatorIdCommand;
import com.idis.core.domain.category.Category;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetCategoriesByCreatorIdCommandHandler implements IRequestHandler<GetCategoriesByCreatorIdCommand, List<Category>> {

    @Override
    public CompletableFuture<List<Category>> handle(GetCategoriesByCreatorIdCommand getCategoriesByCreatorIdCommand) {

        var creatorId = getCategoriesByCreatorIdCommand.creatorId();
        var allCategories = NimbleJQueryProvider.getAll(Category.class);
        List<Category> categories = new ArrayList<>();

        for (Category category : allCategories) {
            if (category.getCreatorId().equals(creatorId)) {
                categories.add(category);
            }
        }

        if (categories.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Category.CreatorHasNoCategories);
        }

        return CompletableFuture.completedFuture(categories);
    }
}
