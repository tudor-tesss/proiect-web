package com.idis.core.business.user;

import com.idis.core.business.BusinessErrors;
import com.idis.core.business.user.commandhandlers.CreateUserCommandHandler;
import com.idis.core.business.user.commands.CreateUserCommand;
import com.idis.core.domain.user.User;
import com.nimblej.networking.database.Database;
import com.nimblej.networking.database.NimbleJQueryProvider;
import org.junit.jupiter.api.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.doNothing;

@PrepareForTest({NimbleJQueryProvider.class, Database.class})
public class CreateUserCommandHandlerTests {
    static {


        try {
            NimbleJQueryProvider.initiate("test", "test2", "test3422");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void when_emailAddressIsAlreadyTaken_then_shouldFail() {
        // Arrange
        var command = command();
        var user = User.create(command.name(), command.firstName(), command.emailAddress());

        try (var mockStatic = mockStatic(NimbleJQueryProvider.class)) {
            mockStatic.when(() -> NimbleJQueryProvider.getAll(User.class)).thenReturn(List.of(user));
            doNothing().when(NimbleJQueryProvider.class);
            NimbleJQueryProvider.insert(user);
        }

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            sut().handle(command);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(BusinessErrors.User.UserAlreadyExists));
    }

    private CreateUserCommandHandler sut() {
        return new CreateUserCommandHandler();
    }

    private CreateUserCommand command() {
        return new CreateUserCommand("testname", "testfirstname", "testemailaddress");
    }
}
