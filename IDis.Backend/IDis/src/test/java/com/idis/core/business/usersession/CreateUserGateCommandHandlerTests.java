package com.idis.core.business.usersession;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.usersession.commandhandlers.CreateUserGateCommandHandler;
import com.idis.core.business.usersession.commands.CreateUserGateCommand;
import com.idis.core.domain.user.User;
import com.idis.core.domain.usersession.UserGate;
import com.idis.infrastructure.services.SendGridService;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class CreateUserGateCommandHandlerTests {
    @Test
    public void when_userDoesNotExist_then_shouldFail() {
        // Arrange
        var command = new CreateUserGateCommand("test_mail");

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of());
            mock.when(() -> QueryProvider.insert(any(UserGate.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.UserGate.Create.UserDoesNotExist));
        }
    }

    @Test
    public void when_notViolatingConstraints_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var command = new CreateUserGateCommand("test_mail");
        var user = User.create("test", "test", "test_mail");

        try (var mock = Mockito.mockStatic(QueryProvider.class); var sendGridMock = Mockito.mockStatic(SendGridService.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> QueryProvider.insert(any(UserGate.class))).thenAnswer(invocation -> null);
            mock.when(() -> SendGridService.sendEmail(any(String.class), any(String.class), any(String.class))).thenAnswer(invocation -> null);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertTrue(result.equals(user.getId()));
        }
    }

    private CreateUserGateCommandHandler sut() {
        return new CreateUserGateCommandHandler();
    }
}
