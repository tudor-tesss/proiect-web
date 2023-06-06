package com.idis.core.business.user;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.commandhandlers.CreateUserCommandHandler;
import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.user.User;
import com.idis.shared.database.QueryProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class CreateUserCommandHandlerTests {
    @Test
    public void when_emailAddressIsAlreadyTaken_then_shouldFail() {
        // Arrange
        var command = command();
        var user = User.create(command.name(), command.firstName(), command.emailAddress());

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of(user));
            mock.when(() -> QueryProvider.insert(any(User.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(BusinessErrors.User.UserAlreadyExists));
        }
    }

    @Test
    public void when_domainFails_then_shouldFail() {
        // Arrange
        var command = new CreateUserCommand(null, "test", "test");

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of());
            mock.when(() -> QueryProvider.insert(any(User.class))).thenAnswer(invocation -> null);

            // Act
            var exception = assertThrows(IllegalArgumentException.class, () -> {
                sut().handle(command);
            });

            // Assert
            var actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(DomainErrors.User.NameNullOrEmpty));
        }
    }

    @Test
    public void when_domainSucceeds_then_shouldSucceed() throws ExecutionException, InterruptedException {
        // Arrange
        var command = command();

        try (var mock = Mockito.mockStatic(QueryProvider.class)) {
            mock.when(() -> QueryProvider.getAll(User.class)).thenReturn(List.of());
            mock.when(() -> QueryProvider.insert(any(User.class))).thenAnswer(invocation -> null);

            // Act
            var result = sut().handle(command).get();

            // Assert
            assertTrue(result.getName().equals(command.name()));
            assertTrue(result.getFirstName().equals(command.firstName()));
            assertTrue(result.getEmailAddress().equals(command.emailAddress()));
        }
    }

    private CreateUserCommandHandler sut() {
        return new CreateUserCommandHandler();
    }

    private CreateUserCommand command() {
        return new CreateUserCommand("testname", "testfirstname", "testemailaddress");
    }
}
