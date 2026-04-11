package pl.vtt.wpi.core.infrastructure;

import pl.vtt.wpi.core.domain.model.Authorization;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import java.time.LocalDateTime;

public record Request<T>(LocalDateTime timestamp, Method method, String url,
                         Authorization authorization, T payload) {
    public Request {
        java.util.Objects.requireNonNull(method, "Method cannot be null");
        java.util.Objects.requireNonNull(url, "URL cannot be null");
        timestamp = java.util.Objects.requireNonNullElse(timestamp, LocalDateTime.now());
    }
}
