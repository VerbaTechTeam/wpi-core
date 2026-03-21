package pl.vtt.wpi.core.domain.model;

import pl.vtt.wpi.core.domain.model.endpoint.Method;

public record Request<T>(Method method, String url, Authorization authorization, T payload) {
    public Request {
        method = method == null ? payload == null ? Method.GET : Method.POST : method;
    }

    @Deprecated(since = "0.1.1", forRemoval = true)
    public Request(String url, Authorization authorization, T payload) {
        this(null, url, authorization, payload);
    }
}
