package pl.vtt.wpi.core.infrastructure;

import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public interface RequestFactory<T> {
    Request<T> create(T payload, Method method, RequestTarget target, Object... urlVariable);
}
