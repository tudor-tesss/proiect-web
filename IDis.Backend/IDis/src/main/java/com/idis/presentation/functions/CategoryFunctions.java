package com.idis.presentation.functions;

import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.business.category.commands.GetCategoriesByCreatorIdCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CategoryFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path= "/categories", method = HttpVerbs.POST)
    @Function(name = "createCategory")
    public static CompletableFuture <HttpResponse> createCategory (String requestBody){
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

    @Route(path = "/categories", method = HttpVerbs.GET)
    @Function(name = "getAllCategories")
    public static CompletableFuture <HttpResponse> getAllCategories (String requestBody){
        var command = new GetAllCategoriesCommand();

        try{
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

    @Route(path = "/users/{id}/categories", method = HttpVerbs.GET)
    @Function(name = "getCategoriesByCreatorId")
    public static CompletableFuture <HttpResponse> getCategoriesByCreatorId (String id, String requestBody){

        UUID creatorId;
        try {
            creatorId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetCategoriesByCreatorIdCommand(creatorId);

        try{
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
