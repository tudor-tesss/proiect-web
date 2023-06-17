package com.idis.core.business.usersession;

import com.idis.core.business.usersession.commandhandlers.RefreshUserSessionCommandHandler;
import com.idis.core.business.usersession.commands.RefreshUserSessionCommand;
import com.idis.core.business.BusinessErrors;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import com.idis.shared.time.TimeProviderContext;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefreshUserSessionCommandHandlerTests {
    @Test
    public void when_userSessionDoesNotExist_then_shouldFail() {
        // Arrange
        var command = command();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(UserSession.class, command.sessionId())).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Check.UserSessionDoesNotExist));
        }
    }

    @Test
    public void when_userSessionIpAddressDoesNotMatch_then_shouldFail() {
        // Arrange
        var userSession = UserSession.create(UUID.randomUUID(), "some other ip");
        var command = new RefreshUserSessionCommand(userSession.getUserId(), userSession.getId(), "some ip");

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(UserSession.class, command.sessionId())).thenReturn(CompletableFuture.completedFuture(Optional.of(userSession)));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Check.UserSessionIpAddressDoesNotMatch));
        }
    }

    @Test
    public void when_userSessionExpired_then_shouldFail() {
        // Arrange
        var userSession = UserSession.create(UUID.randomUUID(), "some ip");
        var command = new RefreshUserSessionCommand(userSession.getUserId(), userSession.getId(), "some ip");

        TimeProviderContext.advanceTimeTo(new Date(userSession.getCreatedAt().getTime() + 1000 * 60 * 60 * 24 + 1));

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(UserSession.class, command.sessionId())).thenReturn(CompletableFuture.completedFuture(Optional.of(userSession)));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });

            // Assert
            var actualMessage = exception.getMessage();

            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Check.UserSessionExpired));
        }
    }

    @Test
    public void when_userIdDoesNotMatch_then_shouldFail() {
        // Arrange
        var userSession = UserSession.create(UUID.randomUUID(), "some ip");
        var command = new RefreshUserSessionCommand(UUID.randomUUID(), userSession.getId(), "some ip");

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getByIdAsync(UserSession.class, command.sessionId())).thenReturn(CompletableFuture.completedFuture(Optional.of(userSession)));

            // Act
            var result = sut().handle(command);

            var exception = assertThrows(ExecutionException.class, () -> {
                result.get();
            });
            
            // Assert
            var actualMessage = exception.getMessage();

            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Check.UserSessionUserIdDoesNotMatch));
        }
    }

    private RefreshUserSessionCommandHandler sut() {
        return new RefreshUserSessionCommandHandler();
    }

    private RefreshUserSessionCommand command() {
        return new RefreshUserSessionCommand(UUID.randomUUID(), UUID.randomUUID(), "some ip");
    }
}
