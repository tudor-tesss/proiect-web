package com.idis.core.domain.user;

import com.idis.core.domain.DomainErrors;
import com.nimblej.core.BaseObject;

import java.util.Date;
import java.util.UUID;

public final class UserSession extends BaseObject {
    private UUID userId;
    private String userIpAddress;
    private Date createdAt;

    public UserSession() { }

    public UserSession(UUID userId, String userIpAddress) {
        this.userId = userId;
        this.userIpAddress = userIpAddress;
        this.createdAt = new Date();
    }

    public static UserSession create(UUID userId, String userIpAddress) {
        if (userIpAddress == null) {
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
