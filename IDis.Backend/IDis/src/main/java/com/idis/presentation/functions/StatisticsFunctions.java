package com.idis.presentation.functions;

import com.idis.core.business.statistics.category.commands.CreateCategoriesStatisticsCommand;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StatisticsFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/categories/{id}/statistics", method = HttpVerbs.POST)
    @Function(name = "createCategoryStatistics")
    public static CompletableFuture<HttpResponse> createCategoryStatistics(String id, String requestBody) {
        UUID categoryId;
        try {
            categoryId = UUID.fromString(id);
        }
        catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(500, responseContent);
        }

        var command = new CreateCategoryStatisticsCommand(categoryId);

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/categories/statistics", method = HttpVerbs.POST)
    @Function(name = "createCategoriesStatistics")
    public static CompletableFuture<HttpResponse> createCategoriesStatistics(String requestBody) {
        var command = new CreateCategoriesStatisticsCommand();

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        HttpResponse res = HttpResponse.create(200, Serialization.serialize(r)).join();

                        return CompletableFuture.completedFuture(res);
                    });
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}