package pl.vtt.wpi.core.domain.model;

public record Request<T>(String url, Authorization authorization, T payload) {
}
