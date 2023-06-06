package com.idis.core.domain.usersession;

import com.idis.core.domain.DomainErrors;
import com.idis.shared.core.AggregateRoot;
import com.idis.shared.time.TimeProviderContext;

import java.util.Date;
import java.util.UUID;

public final class UserSession extends AggregateRoot {
    private UUID userId;
    private String userIpAddress;
    private Date createdAt;

    public UserSession() { }

    public UserSession(UUID userId, String userIpAddress) {
        this.userId = userId;
        this.userIpAddress = userIpAddress;
        this.createdAt = TimeProviderContext.getCurrentProvider().now();
    }

    public static UserSession create(UUID userId, String userIpAddress) {
        if (userIpAddress == null || userIpAddress.isEmpty()) {
            throw new IllegalArgumentException(DomainErrors.UserSession.IpAddressNullOrEmpty);
        }

        return new UserSession(userId, userIpAddress);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserIpAddress() {
        return userIpAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
