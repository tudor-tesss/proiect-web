package com.idis.core.business.commandhandlers.category;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.commands.category.CreateCategoryCommand;
import com.idis.core.business.commands.user.CreateUserCommand;
import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.category.Category;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.concurrent.CompletableFuture;
import static com.nimblej.extensions.functional.FunctionalExtensions.any;
public final class CreateCategoryCommandHandler implements IRequestHandler<CreateCategoryCommand, Category> {

    public CompletableFuture<Category> handle (CreateCategoryCommand createCategoryCommand){
        var categories = NimbleJQueryProvider.getAll(Category.class);

        var duplicateResult = any(categories, category -> category.getName().equals(createCategoryCommand.name()));

        if(duplicateResult){
            throw new IllegalArgumentException(BusinessErrors.Category.CategoryAlreadyExists);
        }

        try{
            var category = Category.create(createCategoryCommand.name(), createCategoryCommand.ratingFields(),createCategoryCommand.cretorId());
            NimbleJQueryProvider.insert(category);

            return CompletableFuture.completedFuture(category);
        }
        catch (Exception e){
            throw e;
        }
    }

}
