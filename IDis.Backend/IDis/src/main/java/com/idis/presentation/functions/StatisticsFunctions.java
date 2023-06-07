package com.idis.presentation.functions;

import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsDocbookCommand;
import com.idis.core.business.statistics.posts.commands.CreatePostsStatisticsCommand;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsDocbookCommand;
import com.idis.core.business.statistics.category.commands.ExportCategoryStatisticsAsPdfCommand;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsPdfCommand;
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

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
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

    @Route(path = "/posts/statistics", method = HttpVerbs.POST)
    @Function(name = "createPostsStatistics")
    public static CompletableFuture<HttpResponse> createPostsStatistics(String requestBody) {
        var command = new CreatePostsStatisticsCommand();
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

    @Route(path = "/posts/{id}/statistics/pdf", method = HttpVerbs.POST)
    @Function(name = "exportPostStatisticsAsPdf")
    public static CompletableFuture <HttpResponse> exportPostStatisticsAsPdf (String id, String body){
        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new ExportPostStatisticsAsPdfCommand(postId);
        var headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/pdf");
        try {
            return mediator
                    .send(command)
                    .thenCompose(pdf ->{

                        String pdfEncode = Base64.getEncoder().encodeToString(pdf);
                        return HttpResponse.create(200, pdfEncode,headers);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }

    @Route(path="/categories/{id}/statistics/pdf",method = HttpVerbs.POST)
    @Function(name = "exportCategoryStatisticsAsPdf")
    public static CompletableFuture <HttpResponse> exportCategoryStatisticsAsPdf (String id, String body){
        UUID categoryId;
        try {
            categoryId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new ExportCategoryStatisticsAsPdfCommand(categoryId);
        var headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/pdf");

        try{
            return mediator
                    .send(command)
                    .thenCompose(pdf ->{

                        String pdfEncode = Base64.getEncoder().encodeToString(pdf);
                        return HttpResponse.create(200, pdfEncode,headers);
                    });
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }

    @Route(path = "/posts/{id}/statistics/docbook", method = HttpVerbs.POST)
    @Function(name = "exportPostStatisticsAsDocbook")
    public static CompletableFuture<HttpResponse> exportPostStatisticsAsDocbook(String id, String body){
        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new ExportPostStatisticsAsDocbookCommand(postId);
        var headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/docbook+xml");

        try {
            return mediator
                    .send(command)
                    .thenCompose(d -> HttpResponse.create(200, d, headers));
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }

    @Route(path = "/categories/{id}/statistics/docbook", method = HttpVerbs.POST)
    @Function(name = "exportCategoryStatisticsAsDocbook")
    public static CompletableFuture<HttpResponse> exportCategoryStatisticsAsDocbook(String id, String body){
        UUID categoryId;
        try {
            categoryId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new ExportCategoryStatisticsAsDocbookCommand(categoryId);
        var headers = new HashMap<String, String>();

        headers.put("Content-Type", "application/docbook+xml");

        try {
            return mediator
                    .send(command)
                    .thenCompose(d -> HttpResponse.create(200, d, headers));
        } catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}
