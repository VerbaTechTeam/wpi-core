package pl.vtt.wpi.core.application.util;

import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public interface RequestFactory<T> {
    Request<T> create(Method method, RequestTarget target, T payload);
}
