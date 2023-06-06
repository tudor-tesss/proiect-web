package com.idis.core.domain.user;

import com.idis.core.domain.DomainErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_nameIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String name) {
        // Arrange
        var firstName = "firstName";
        var emailAddress = "emailAddress";

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create(name, firstName, emailAddress);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.User.NameNullOrEmpty));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_firstNameIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String firstName) {
        // Arrange
        var name = "name";
        var emailAddress = "emailAddress";

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create(name, firstName, emailAddress);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.User.FirstNameNullOrEmpty));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void given_create_when_emailAddressIsNullOrEmpty_then_shouldThrowIllegalArgumentException(String emailAddress) {
        // Arrange
        var name = "name";
        var firstName = "firstName";

        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            User.create(name, firstName, emailAddress);
        });

        // Assert
        var actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(DomainErrors.User.EmailAddressNullOrEmpty));
    }

    @Test
    public void given_create_when_notViolatingConstraints_then_shouldCreate() {
        // Arrange
        var name = "name";
        var firstName = "firstName";
        var emailAddress = "emailAddress";

        // Act
        var user = User.create(name, firstName, emailAddress);

        // Assert
        assertTrue(user != null);
        assertTrue(user.getName().equals(name));
        assertTrue(user.getFirstName().equals(firstName));
        assertTrue(user.getEmailAddress().equals(emailAddress));
    }
}
