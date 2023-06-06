package com.idis.presentation.functions;

import com.idis.core.business.statistics.category.commands.CreateCategoriesStatisticsCommand;
import com.idis.core.business.statistics.category.commands.CreateCategoryStatisticsCommand;
import com.idis.core.business.statistics.posts.commands.CreatePostStatisticsCommand;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Function;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IUserController;
import com.idis.shared.web.routing.Route;

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

            return HttpResponse.create(400, responseContent);
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

    @Route(path = "/posts/{id}/statistics", method = HttpVerbs.POST)
    @Function(name = "createPostStatistics")
    public static CompletableFuture<HttpResponse> createPostStatistics(String id, String requestBody) {
        UUID postId;
        try {
            postId = UUID.fromString(id);
        }
        catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new CreatePostStatisticsCommand(postId);
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
