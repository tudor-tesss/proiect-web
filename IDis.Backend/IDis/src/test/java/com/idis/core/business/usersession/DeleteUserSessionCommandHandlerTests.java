package com.idis.core.business.usersession;

import com.idis.core.business.usersession.commandhandlers.DeleteUserSessionCommandHandler;
import com.idis.core.business.usersession.commands.DeleteUserSessionCommand;
import com.idis.core.domain.usersession.UserSession;
import com.nimblej.networking.database.NimbleJQueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

public class DeleteUserSessionCommandHandlerTests {
    @Test
    public void when_userSessionIsFound_then_shouldDelete() {
        // Arrange
        var session = UserSession.create(UUID.randomUUID(), "localhost");
        var command = new DeleteUserSessionCommand(session.getId());

        try (var mock = Mockito.mockStatic(NimbleJQueryProvider.class)) {
            mock.when(() -> NimbleJQueryProvider.getAll(UserSession.class)).thenReturn(List.of(session));
            mock.when(() -> NimbleJQueryProvider.delete(any(UserSession.class))).thenAnswer(invocation -> null);

            // Act
            sut().handle(command);

            // Assert
            mock.verify(() -> NimbleJQueryProvider.delete(any(UserSession.class)));
        }
    }

    private DeleteUserSessionCommandHandler sut() {
        return new DeleteUserSessionCommandHandler();
    }
}
