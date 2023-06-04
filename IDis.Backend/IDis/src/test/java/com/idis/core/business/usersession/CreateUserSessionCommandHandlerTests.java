package com.idis.core.business.usersession;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandhandlers.CreateUserSessionCommandHandler;
import com.idis.core.business.usersession.commands.CreateUserSessionCommand;
import com.idis.core.domain.user.User;
import com.idis.core.domain.usersession.UserSession;
import com.nimblej.networking.database.NimbleJQueryProvider;
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

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(User.class)).thenReturn(List.of());

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.UserSession.Create.UserDoesNotExist));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var user = User.create("test", "test", "test_mail");
        var command = new CreateUserSessionCommand(user.getId(), "127.0.0.1");

        try (var mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> NimbleJQueryProvider.insert(any(UserSession.class))).thenAnswer(invocation -> null);

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
