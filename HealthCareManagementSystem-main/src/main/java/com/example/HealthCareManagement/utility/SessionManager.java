package com.example.HealthCareManagement.utility;

import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Component
public class SessionManager {

    private final Map<Long, Boolean> activeSessions = new ConcurrentHashMap<>();

    public void startSession(Long userId) {
        activeSessions.put(userId, true);
    }

    public boolean isSessionActive(Long userId) {
        return activeSessions.getOrDefault(userId, false);
    }

    // PATCH: Check if any user is currently logged into this instance
    public boolean hasAnyActiveSession() {
        return !activeSessions.isEmpty();
    }

    public void endSession(Long userId) {
        activeSessions.remove(userId);
    }
}