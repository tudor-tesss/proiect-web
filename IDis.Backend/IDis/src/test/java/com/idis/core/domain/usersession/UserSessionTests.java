package com.idis.core.domain.usersession;

import com.idis.core.domain.DomainErrors;
import com.idis.shared.time.TimeProviderContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserSessionTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_userIpAddressIsNullOrEmpty_then_shouldFail(String ipAddress) {
        // Arrange
        var userId = UUID.randomUUID();

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            UserSession.create(userId, ipAddress);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.UserSession.IpAddressNullOrEmpty));

        // Clean up
        TimeProviderContext.reset();
    }

    @Test
    public void given_create_when_userIpAddressIsValid_then_shouldSucceed() {
        // Arrange
        var userId = UUID.randomUUID();
        var ipAddress = "127.0.0.1";
        var now = TimeProviderContext.getCurrentProvider().now().getTime();

        // Act
        var userSession = UserSession.create(userId, ipAddress);

        // Assert
        assertTrue(userSession.getUserId().equals(userId));
        assertTrue(userSession.getUserIpAddress().equals(ipAddress));
        assertTrue(userSession.getCreatedAt().getTime() == now);

        // Clean up
        TimeProviderContext.reset();
    }
}
