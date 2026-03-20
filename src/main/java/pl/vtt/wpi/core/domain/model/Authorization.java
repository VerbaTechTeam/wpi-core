package pl.vtt.wpi.core.domain.model;

import java.util.Objects;

public record Authorization(String type, String credentials) {
    public Authorization {
        type = Objects.requireNonNullElse(type, "Bearer");
        credentials = Objects.requireNonNullElse(credentials, "null");
    }

    public String toString() {
        return "Authorization[type=%s, credentials=<redacted>]".formatted(type);
    }
}
