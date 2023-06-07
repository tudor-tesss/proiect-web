package com.idis.core.business.statistics.post;

import com.idis.core.business.BusinessErrors;

import com.idis.core.business.statistics.posts.commandhandlers.ExportPostStatisticsAsPdfCommandHandler;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsPdfCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ExportPostStatisticsAsPdfCommandHandlerTests {
    @Test
    public void given_export_when_postDoesntExist_then_shouldThrowIllegalArgumentException() {
        // Arrange
        UUID postId = UUID.randomUUID();
        var command = command(postId);

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getAll(Post.class)).thenReturn(new ArrayList<Post>());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();
            assertTrue(actualMessage.contains(BusinessErrors.Post.PostNotFound));
        }
    }

    @Test
    public void given_export_when_PostExists_then_shouldSucceed() {
        // Arrange
        Map<String,Integer> ratings = new HashMap<>();
        ratings.put("rat1",1);
        ratings.put("rat2",2);
        var post = Post.create(UUID.randomUUID(),UUID.randomUUID(),"postName","description",ratings);
        var command = command(post.getId());
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
    private ExportPostStatisticsAsPdfCommandHandler sut() {
        return new ExportPostStatisticsAsPdfCommandHandler();
    }
    private ExportPostStatisticsAsPdfCommand command(UUID postId) {
        return new ExportPostStatisticsAsPdfCommand(postId);
    }
}
