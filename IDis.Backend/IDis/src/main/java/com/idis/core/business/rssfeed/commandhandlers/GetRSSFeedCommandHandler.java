package com.idis.core.business.rssfeed.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.rssfeed.commands.GetRSSFeedCommand;
import com.idis.core.business.rssfeed.extensions.RSSFeedGenerator;
import com.idis.core.domain.posts.parentpost.Post;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class GetRSSFeedCommandHandler implements IRequestHandler<GetRSSFeedCommand, String > {

    public CompletableFuture<String> handle (GetRSSFeedCommand getRSSFeedCommand){

        StringBuilder rssResult = new StringBuilder();

        var posts = sortAfterDate(NimbleJQueryProvider.getAll(Post.class));

        if(posts.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Post.NoPostsAvailable);
        }

        RSSFeedGenerator rssFeedGenerator = new RSSFeedGenerator(posts);

        try {
            rssFeedGenerator.generateRSS();
        } catch (Exception e){
            e.printStackTrace();

        }

        try{
            File xmlRSS = new File("rss.xml");
            Scanner reader = new Scanner(xmlRSS);
            while (reader.hasNextLine()){
                rssResult.append(reader.nextLine());
            }
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        return CompletableFuture.completedFuture(rssResult.toString());

    }

    private List<Post> sortAfterDate(List<Post> posts){

        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getCreatedAt().compareTo(post1.getCreatedAt());
            }
        });
        return posts;
    }

}
