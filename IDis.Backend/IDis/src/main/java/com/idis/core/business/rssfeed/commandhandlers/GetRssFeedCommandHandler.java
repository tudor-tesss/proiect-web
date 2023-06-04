package com.idis.core.business.rssfeed.commandhandlers;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.rssfeed.commands.GetRssFeedCommand;
import com.idis.core.business.rssfeed.extensions.RssFeedGenerator;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.core.domain.posts.postreply.PostReply;
import com.nimblej.core.IRequestHandler;
import com.nimblej.networking.database.NimbleJQueryProvider;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class GetRssFeedCommandHandler implements IRequestHandler<GetRssFeedCommand, String >{
    public CompletableFuture<String> handle (GetRssFeedCommand getRSSFeedCommand){
        String rssResult = new String();
        var posts = sortAfterDate(NimbleJQueryProvider.getAll(Post.class));
        if(posts.isEmpty()){
            throw new IllegalArgumentException(BusinessErrors.Post.NoPostsAvailable);
        }

        Map<UUID,Integer> numOfReplies = getRepliesNum(posts);

        RssFeedGenerator rssFeedGenerator = new RssFeedGenerator(posts,numOfReplies);

        try {
            rssResult = rssFeedGenerator.generateRSS();
        } catch (Exception e){
            e.printStackTrace();
        }

        return CompletableFuture.completedFuture(rssResult);
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

    private Map<UUID,Integer> getRepliesNum(List<Post> posts){
        Map<UUID,Integer> numOfReplies = new HashMap<>();
        var allReplies = NimbleJQueryProvider.getAll(PostReply.class);

        for(Post post:posts){
            for(PostReply reply : allReplies) {
                if(reply.getParentPostId().equals(post.getId())) {

                    if(numOfReplies.get(post.getId()) == null){
                        numOfReplies.put(post.getId(),0);
                    }
                    var updatedValue = numOfReplies.get(post.getId())+1;
                    numOfReplies.put(post.getId(),updatedValue);
                }
            }

        }
        return numOfReplies;
    }
}
