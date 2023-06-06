package com.idis.core.business.category.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.category.commands.GetCategoriesByCreatorIdCommand;
import com.idis.core.domain.category.Category;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.infrastructure.IRequestHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class GetCategoriesByCreatorIdCommandHandler implements IRequestHandler<GetCategoriesByCreatorIdCommand, List<Category>> {
    @Override
    public CompletableFuture<List<Category>> handle(GetCategoriesByCreatorIdCommand getCategoriesByCreatorIdCommand) {
        var creatorId = getCategoriesByCreatorIdCommand.creatorId();
        var allCategories = QueryProvider.getAll(Category.class);
        var categories = allCategories.stream().filter(category -> category.getCreatorId().equals(creatorId)).toList();

        if (categories.isEmpty()) {
            throw new IllegalArgumentException(BusinessErrors.Category.CreatorHasNoCategories);
        }

        return CompletableFuture.completedFuture(categories);
    }
}
