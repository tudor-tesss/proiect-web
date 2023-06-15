package com.idis.presentation.functions;

import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.category.queries.GetAllCategoriesQuery;
import com.idis.core.business.category.queries.GetCategoriesByCreatorIdQuery;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Controller;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CategoryController implements IController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path= "/categories", method = HttpVerbs.POST)
    @Controller(name = "createCategory")
    public static CompletableFuture<HttpResponse> createCategory(String requestBody){
        var command = Serialization.deserialize(requestBody, CreateCategoryCommand.class);
        try {
            return mediator
                    .send(command)
                    .thenCompose(category -> {
                        var responseContent = Serialization.serialize(category);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }

    @Route(path = "/categories",method = HttpVerbs.GET)
    @Controller(name = "getAllCategories")
    public static CompletableFuture<HttpResponse> getAllCategories(String requestBody){
        var command = new GetAllCategoriesQuery();
        try {
            return mediator
                    .send(command)
                    .thenCompose(allCategories -> {
                        var responseContent = Serialization.serialize(allCategories);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }

    @Route(path = "/users/{id}/categories",method = HttpVerbs.GET)
    @Controller(name = "getCategoriesByCreatorId")
    public static CompletableFuture<HttpResponse> getCategoriesByCreatorId(String id, String requestBody){
        UUID creatorId;
        try {
            creatorId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetCategoriesByCreatorIdQuery(creatorId);
        try {
            return mediator
                    .send(command)
                    .thenCompose(categories -> {
                        var responseContent = Serialization.serialize(categories);

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }
}
