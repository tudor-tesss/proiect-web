package com.idis.core.domain.user;

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

    public UUID getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

    private static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
