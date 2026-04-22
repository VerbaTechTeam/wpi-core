package pl.vtt.wpi.core.domain.model;

import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import java.util.Set;

public record User(String username, Set<UserGroup> groups) {
    public User {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (groups == null) {
            throw new IllegalArgumentException("Groups cannot be null");
        }
        if (groups.isEmpty() || groups.contains(null)) {
            throw new IllegalArgumentException("Groups cannot be empty or contain null elements");
        }
        groups = Set.copyOf(groups);
    }
}
