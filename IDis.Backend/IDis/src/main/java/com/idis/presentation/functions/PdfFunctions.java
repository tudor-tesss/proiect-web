package com.idis.presentation.functions;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.idis.core.business.pdf.post.commands.GetPdfForPostStatsCommand;
import com.nimblej.core.Function;
import com.nimblej.core.IUserController;
import com.nimblej.core.Mediator;
import com.nimblej.extensions.json.Serialization;
import com.nimblej.networking.http.communication.HttpResponse;
import com.nimblej.networking.http.communication.HttpVerbs;
import com.nimblej.networking.http.routing.Route;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PdfFunctions implements IUserController {

    private static Mediator mediator = Mediator.getInstance();

    @Route(path="/posts/{id}/statistics/pdf",method = HttpVerbs.GET)
    @Function(name = "getPdfForPostStats")
    public static CompletableFuture <HttpResponse> getPdfForPostStats (String id, String body){
        UUID postId;
        try {
            postId = UUID.fromString(id);
        } catch (Exception e) {
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400, responseContent);
        }

        var command = new GetPdfForPostStatsCommand(postId);
        Map<String,String> headers =new HashMap<>();

        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.put("Content-Type", "application/pdf");

        try{
            return mediator
                    .send(command)
                    .thenCompose(pdf ->{

                        String pdfEncode = Base64.getEncoder().encodeToString(pdf);
                        return HttpResponse.create(200,pdfEncode,headers);
                    });
        }catch (Exception e){
            var responseContent = Serialization.serialize(e.getMessage());

            return HttpResponse.create(400,responseContent);
        }
    }
}
