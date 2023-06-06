package com.idis.core.domain.user;

import com.idis.core.domain.DomainErrors;
import com.idis.shared.core.AggregateRoot;

public final class User extends AggregateRoot {
    private String name;
    private String firstName;
    private String emailAddress;

    public User() { }

    private User(String name, String firstName, String emailAddress) {
        super();

        this.name = name;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
    }

    public static User create(String name, String firstName, String emailAddress) throws IllegalArgumentException {
        var nameResult = name != null && !name.isBlank();
        var firstNameResult = firstName != null && !firstName.isBlank();
        var emailAddressResult = emailAddress != null && !emailAddress.isBlank();

        if (!nameResult) {
            throw new IllegalArgumentException(DomainErrors.User.NameNullOrEmpty);
        }

        if (!firstNameResult) {
            throw new IllegalArgumentException(DomainErrors.User.FirstNameNullOrEmpty);
        }

        if (!emailAddressResult) {
            throw new IllegalArgumentException(DomainErrors.User.EmailAddressNullOrEmpty);
        }

        return new User(name, firstName, emailAddress);
    }


    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
