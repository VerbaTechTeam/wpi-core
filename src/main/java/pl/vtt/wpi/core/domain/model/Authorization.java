package pl.vtt.wpi.core.domain.model;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static java.util.Base64.getEncoder;

public record Authorization(String type, String credentials) {
    public Authorization {
        type = Objects.requireNonNullElse(type, "Basic");
        credentials = Objects.requireNonNullElse(credentials, "null");
    }

    public String toString() {
        return "Authorization[type=%s, credentials=<redacted>]".formatted(type);
    }

    public static Authorization basic(String username, String password) {
        Objects.requireNonNull(username, "username must not be null");
        Objects.requireNonNull(password, "password must not be null");
        byte[] credentials = (username + ":" + password).getBytes(StandardCharsets.UTF_8);
        return new Authorization("Basic", getEncoder().encodeToString(credentials));
    }
}
