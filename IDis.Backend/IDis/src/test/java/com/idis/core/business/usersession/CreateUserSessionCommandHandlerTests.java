package com.idis.core.business.usersession;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandhandlers.CreateUserSessionCommandHandler;
import com.idis.core.business.usersession.commands.CreateUserSessionCommand;
import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.user.User;
import com.idis.core.domain.usersession.UserSession;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CreateUserSessionCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new CreateUserSessionCommand(UUID.randomUUID(), "127.0.0.1");

        try (MockedStatic<QueryProvider> mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Create.UserDoesNotExist));
        }
    }

    @Test
    public void when_domainFails_then_shouldFail() {
        // Arrange
        var user = User.create("test", "test", "test_mail");
        var command = new CreateUserSessionCommand(user.getId(), null);

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> QueryProvider.insert(any(UserSession.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(DomainErrors.UserSession.IpAddressNullOrEmpty));
        }
    }

    @Test
    public void when_domainSucceeds_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var user = User.create("test", "test", "test_mail");
        var command = new CreateUserSessionCommand(user.getId(), "127.0.0.1");

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> QueryProvider.insert(any(UserSession.class))).thenAnswer(invocation -> null);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertNotNull(result);
        }
    }

    private CreateUserSessionCommandHandler sut() {
        return new CreateUserSessionCommandHandler();
    }
}
