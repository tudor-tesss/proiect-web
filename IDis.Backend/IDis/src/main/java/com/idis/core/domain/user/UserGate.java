package com.idis.core.domain.user;

import com.idis.core.domain.DomainErrors;
import com.idis.core.domain.constants.UserGateConstants;
import com.nimblej.core.BaseObject;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Date;
import java.util.UUID;

public final class UserGate extends BaseObject {
    private UUID userId;
    private String code;
    private Date createdAt;
    @Nullable private Date passedAt;

    public UserGate() { }

    public UserGate(UUID userId) {
        super();

        this.userId = userId;
        this.code = generateCode();
        this.createdAt = new Date();
    }

    public static UserGate create(UUID userId) {
        return new UserGate(userId);
    }

    public void pass(String code) throws Exception {
        if (createdAt.getTime() + UserGateConstants.UserGateTimeToLive < new Date().getTime()) {
            throw new Exception(DomainErrors.UserGate.Pass.UserGateExpired);
        }

        if (!this.code.equals(code)) {
            throw new Exception(DomainErrors.UserGate.Pass.InvalidCode);
        }

        if (passedAt != null) {
            throw new Exception(DomainErrors.UserGate.Pass.UserGateAlreadyPassed);
        }

        passedAt = new Date();
    }

    public UUID getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getPassedAt() {
        return passedAt;
    }

    private static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
