package com.idis.infrastructure.session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SessionStore {
    private static final Map<String, UUID> sessions = new ConcurrentHashMap<>();

    public static UUID getUserId(String sessionId) {
        return sessions.get(sessionId);
    }

    public static String createSession(UUID userId) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, userId);
        return sessionId;
    }
}
