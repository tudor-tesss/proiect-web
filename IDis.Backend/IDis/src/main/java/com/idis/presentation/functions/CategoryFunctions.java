package com.idis.presentation.functions;

import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.concurrent.CompletableFuture;
public class CategoryFunctions implements IUserController {

    private static Mediator mediator = Mediator.getInstance();

    @Route(path= "/categories", method = HttpVerbs.POST)
    @Function(name = "createCategory")
    public static CompletableFuture <HttpResponse> createCategory (String requestBody){
        var command = Serialization.deserialize(requestBody, CreateCategoryCommand.class);

        try{
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
}
