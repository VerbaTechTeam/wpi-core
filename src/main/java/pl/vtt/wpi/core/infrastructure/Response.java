package pl.vtt.wpi.core.infrastructure;

import pl.vtt.wpi.core.domain.model.Request;

import java.time.LocalDateTime;

public record Response<T>(LocalDateTime timestamp, Request<?> request, T body, Exception exception) {
    public Response {
        java.util.Objects.requireNonNull(request, "Request cannot be null");
        if (body != null && exception != null) {
            throw new IllegalArgumentException("Response cannot have both body and exception");
        }
        timestamp = java.util.Objects.requireNonNullElse(timestamp, LocalDateTime.now());
    }
}
