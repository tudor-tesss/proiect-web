package com.idis.presentation.functions;

import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class StatisticsFunctions implements IUserController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/statistics/categories/{id}", method = HttpVerbs.POST)
    @Function(name = "createCategoryStatistics")
    public static CompletableFuture<HttpResponse> createCategoryStatistics(String id) {
        UUID categoryId;
        try {
            categoryId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = e.getMessage();

            return HttpResponse.create(400, responseContent);
        }

        var command = new CreateCategoryStatisticsCommand(categoryId);

        try {
            return mediator
                    .send(command)
                    .thenCompose(r -> {
                        var responseContent = r.toString();

                        return HttpResponse.create(200, responseContent);
                    });
        } catch (Exception e) {
            var responseContent = e.getMessage();

            return HttpResponse.create(400, responseContent);
        }
    }
}
