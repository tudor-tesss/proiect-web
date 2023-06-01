package com.idis.presentation.functions;

import com.idis.core.business.category.commands.CreateCategoryCommand;
import com.idis.core.business.category.commands.GetAllCategoriesCommand;
import com.idis.core.business.category.commands.GetCategoriesByCreatorIdCommand;
import com.idis.core.business.rssfeed.commands.GetRSSFeedCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class RSSFeedFunctions implements IUserController {

    private static Mediator mediator = Mediator.getInstance();

    @Route(path="/rss", method = HttpVerbs.GET)
    @Function(name = "getRSSFeed")
    public static CompletableFuture <HttpResponse> getRSSFeed(String body){

        var command = new GetRSSFeedCommand();

        Map<String,String> headers =new HashMap<>();

        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/xml");

        try{
            return mediator
                    .send(command)
                    .thenCompose(rss ->{
                return HttpResponse.create(200,rss,headers);
            });

        }catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }
}
