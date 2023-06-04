package com.idis.core.business.usersession;

import com.idis.core.business.usersession.commandhandlers.CheckUserSessionCommandHandler;
import com.idis.core.business.usersession.commands.CheckUserSessionCommand;
import com.idis.core.business.BusinessErrors;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.time.TimeProviderContext;
import com.nimblej.networking.database.NimbleJQueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class CheckUserSessionCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var command = command();

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserSession.class)).thenReturn(List.of());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
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
        var command = new CheckUserSessionCommand(userSession.getUserId(), userSession.getId(), "some ip");

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserSession.class)).thenReturn(List.of(userSession));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

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
        var command = new CheckUserSessionCommand(userSession.getUserId(), userSession.getId(), "some ip");

        TimeProviderContext.advanceTimeTo(new Date(userSession.getCreatedAt().getTime() + 1000 * 60 * 60 * 24 + 1));

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserSession.class)).thenReturn(List.of(userSession));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

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
        var command = new CheckUserSessionCommand(UUID.randomUUID(), userSession.getId(), "some ip");

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserSession.class)).thenReturn(List.of(userSession));

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

            // Assert
            var actualMessage = exception.getMessage();

            System.out.println(actualMessage);
            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Check.UserSessionUserIdDoesNotMatch));
        }
    }

    private CheckUserSessionCommandHandler sut() {
        return new CheckUserSessionCommandHandler();
    }

    private CheckUserSessionCommand command() {
        return new CheckUserSessionCommand(UUID.randomUUID(), UUID.randomUUID(), "some ip");
    }
}
