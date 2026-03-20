package pl.vtt.wpi.core.application.util;

import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public interface RequestFactory<T> {
    Request<T> create(RequestTarget target, T payload);
}
