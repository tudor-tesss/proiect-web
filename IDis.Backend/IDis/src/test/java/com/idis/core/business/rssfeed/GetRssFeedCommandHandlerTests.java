package com.idis.core.business.rssfeed;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.rssfeed.commandhandlers.GetRssFeedCommandHandler;
import com.idis.core.business.rssfeed.commands.GetRssFeedCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class GetRssFeedCommandHandlerTests {

    @Test
    public void given_rssFeed_when_postDoesntExist_then_shouldThrowIllegalArgumentException() {
        // Arrange
        var command = command();

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(new ArrayList<Post>());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(BusinessErrors.Post.NoPostsAvailable));
        }
    }
    @Test
    public void given_rssFeed_when_PostExists_then_shouldSucceed() {
        // Arrange
        Map<String,Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(UUID.randomUUID(),UUID.randomUUID(),"postName","description",ratings);
        var command = command();
        List<Post> posts = new ArrayList<>();
        posts.add(post);

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(posts);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private GetRssFeedCommandHandler sut(){return new GetRssFeedCommandHandler();}
    private GetRssFeedCommand command(){return new GetRssFeedCommand();}
}
