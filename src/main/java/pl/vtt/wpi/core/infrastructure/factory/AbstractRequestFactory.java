package pl.vtt.wpi.core.infrastructure.factory;

import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.infrastructure.RequestFactory;

public sealed abstract class AbstractRequestFactory<T> implements RequestFactory<T>
        permits DeleteUserRequestFactory, EmptyUrlVariablesRequestFactory {
    private final String baseUrl;

    AbstractRequestFactory(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Request<T> create(Method method, RequestTarget target, T body) {
        return new Request<>(null, method,
                target.url(baseUrl, getUrlVariables(target)),
                AuthorizationHolder.get(), body);
    }

    protected abstract Object[] getUrlVariables(RequestTarget target);
}
