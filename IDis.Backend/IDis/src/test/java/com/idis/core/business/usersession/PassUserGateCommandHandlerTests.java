package com.idis.core.business.usersession;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandhandlers.PassUserGateCommandHandler;
import com.idis.core.business.usersession.commands.PassUserGateCommand;
import com.idis.core.domain.user.User;
import com.idis.core.domain.usersession.UserGate;
import com.nimblej.networking.database.NimbleJQueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class PassUserGateCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new PassUserGateCommand(UUID.randomUUID(), "code");

        try (MockedStatic<NimbleJQueryProvider> mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserGate.class)).thenReturn(new ArrayList<>());
            mock.when(() -> NimbleJQueryProvider.insert(any(UserGate.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> sut().handle(command));

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.UserGate.Pass.UserGateDoesNotExist));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var user = User.create("test", "test", "test_mail");
        var userGate = UserGate.create(user.getId());
        var command = new PassUserGateCommand(user.getId(), userGate.getCode());

        try (var mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> NimbleJQueryProvider.getAll(UserGate.class)).thenReturn(new ArrayList<>(List.of(userGate)));
            mock.when(() -> NimbleJQueryProvider.insert(any(UserGate.class))).thenAnswer(invocation -> null);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertTrue(result.userId().equals(user.getId()));
        }
    }

    private PassUserGateCommandHandler sut() {
        return new PassUserGateCommandHandler();
    }
}
