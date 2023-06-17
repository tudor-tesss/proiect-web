package com.idis.core.business.statistics.post;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.statistics.posts.commandhandlers.ExportPostStatisticsAsDocbookCommandHandler;
import com.idis.core.business.statistics.posts.commandhandlers.ExportPostStatisticsAsPdfCommandHandler;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsDocbookCommand;
import com.idis.core.business.statistics.posts.commands.ExportPostStatisticsAsPdfCommand;
import com.idis.core.domain.posts.parentpost.Post;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class ExportPostStatisticsAsDocbookCommandHandlerTests {
    @Test
    public void given_export_when_postDoesntExist_then_shouldThrowIllegalArgumentException() {
        // Arrange
        UUID postId = UUID.randomUUID();
        var command = command(postId);

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getById(Post.class, postId)).thenReturn(Optional.empty());

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

        try(var mock = Mockito.mockStatic(QueryProvider.class)){
            mock.when(() -> QueryProvider.getById(Post.class, post.getId())).thenReturn(Optional.of(post));

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    private ExportPostStatisticsAsDocbookCommandHandler sut() {
        return new ExportPostStatisticsAsDocbookCommandHandler();
    }
    private ExportPostStatisticsAsDocbookCommand command(UUID postId) {
        return new ExportPostStatisticsAsDocbookCommand(postId);
    }
}
