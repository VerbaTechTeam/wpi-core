package pl.vtt.wpi.core.infrastructure.factory;

import pl.vtt.wpi.core.domain.model.AuthorizationManager;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.infrastructure.RequestFactory;

public class SynchronizedRequestFactory<T> implements RequestFactory<T> {
    private final AuthorizationManager authorizationManager;
    private final String baseUrl;

    public SynchronizedRequestFactory(AuthorizationManager manager, String baseUrl) {
        this.authorizationManager = manager;
        this.baseUrl = baseUrl;
    }

    @Override
    public synchronized Request<T> create(T body, Method method, RequestTarget target,
                                          Object... urlVariable) {
        return new Request<>(null, method, target.url(baseUrl, urlVariable),
                authorizationManager.getAuthorization(), body);
    }
}
