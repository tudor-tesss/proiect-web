package com.idis.presentation.functions;

import com.idis.core.business.rssfeed.commands.GetRssFeedCommand;
import com.idis.shared.infrastructure.Mediator;
import com.idis.shared.serialization.Serialization;
import com.idis.shared.web.communication.Controller;
import com.idis.shared.web.communication.HttpResponse;
import com.idis.shared.web.communication.HttpVerbs;
import com.idis.shared.web.communication.IController;
import com.idis.shared.web.routing.Route;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class RssFeedController implements IController {
    private static Mediator mediator = Mediator.getInstance();

    @Route(path = "/rss", method = HttpVerbs.GET)
    @Controller(name = "getRSSFeed")
    public static CompletableFuture <HttpResponse> getRSSFeed(String body){
        var command = new GetRssFeedCommand();

        var headers = new HashMap<String, String>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/xml");

        try {
            return mediator
                    .send(command)
                    .thenCompose(rss -> HttpResponse.create(200, rss, headers));
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }
    }
}
