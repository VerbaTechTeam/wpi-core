package pl.vtt.wpi.core.application.util;

import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public interface RequestFactory<T> {
    @Deprecated(since = "0.1.1", forRemoval = true)
    default Request<T> create(RequestTarget target, T payload) {
        throw new UnsupportedOperationException("Deprecated method. It will be removed in future (v0.2.0).");
    }

    default Request<T> create(Method method, RequestTarget target, T payload) {
        throw new UnsupportedOperationException("Not supported yet. Should be overridden.");
    }
}
