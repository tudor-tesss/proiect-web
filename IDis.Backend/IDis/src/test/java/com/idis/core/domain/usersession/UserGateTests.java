package com.idis.core.domain.usersession;

import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.constants.UserGateConstants;
import com.idis.shared.time.TimeProviderContext;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserGateTests {
    @Test
    public void given_create_when_notViolatingConstraints_then_shouldCreate() {
        // Arrange
        var userId = UUID.randomUUID();

        // Act
        var userGate = UserGate.create(userId);

        // Assert
        assertNotNull(userGate);
        assertEquals(userId, userGate.getUserId());
    }

    @Test
    public void given_pass_when_userGateExpired_then_shouldThrowException() throws Exception {
        // Arrange
        var userId = UUID.randomUUID();
        var date = new Date(0);
        TimeProviderContext.advanceTimeTo(date);
        var userGate = UserGate.create(userId);

        // Act
        var exception = assertThrows(Exception.class, () -> {
            TimeProviderContext.advanceTimeTo(new Date(date.getTime() + UserGateConstants.UserGateTimeToLive + 1));
            userGate.pass(userGate.getCode());
        });

        // Assert
        assertEquals(DomainErrors.UserGate.Pass.UserGateExpired, exception.getMessage());

        // Clean up
        TimeProviderContext.reset();
    }

    @Test
    public void given_pass_when_invalidCode_then_shouldThrowException() throws Exception {
        // Arrange
        var userId = UUID.randomUUID();
        TimeProviderContext.advanceTimeTo(new Date(0));
        var userGate = UserGate.create(userId);

        // Act
        var exception = assertThrows(Exception.class, () -> {
            userGate.pass("invalidCode");
        });

        // Assert
        assertEquals(DomainErrors.UserGate.Pass.InvalidCode, exception.getMessage());

        // Clean up
        TimeProviderContext.reset();
    }

    @Test
    public void given_pass_when_userGateAlreadyPassed_then_shouldThrowException() throws Exception {
        // Arrange
        var userId = UUID.randomUUID();
        TimeProviderContext.advanceTimeTo(new Date(0));
        var userGate = UserGate.create(userId);
        userGate.pass(userGate.getCode());

        // Act
        var exception = assertThrows(Exception.class, () -> {
            userGate.pass(userGate.getCode());
        });

        // Assert
        assertEquals(DomainErrors.UserGate.Pass.UserGateAlreadyPassed, exception.getMessage());

        // Clean up
        TimeProviderContext.reset();
    }

    @Test
    public void given_pass_when_notViolatingConstraints_then_shouldPass() throws Exception {
        // Arrange
        var userId = UUID.randomUUID();
        var now = TimeProviderContext.getCurrentProvider().now().getTime();
        var userGate = UserGate.create(userId);

        // Act
        userGate.pass(userGate.getCode());

        // Assert
        assertNotNull(userGate.getPassedAt());
        assertEquals(userGate.getPassedAt().getTime(), now);

        // Clean up
        TimeProviderContext.reset();
    }
}
