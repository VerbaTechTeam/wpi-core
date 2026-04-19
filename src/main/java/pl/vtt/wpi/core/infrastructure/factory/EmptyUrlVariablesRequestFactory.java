package pl.vtt.wpi.core.infrastructure.factory;

import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public final class EmptyUrlVariablesRequestFactory<T> extends AbstractRequestFactory<T> {
    public EmptyUrlVariablesRequestFactory(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public Object[] getUrlVariables(RequestTarget target) {
        if (target == RequestTarget.USER_DELETE) {
            throw new IllegalArgumentException("Request target requires URL variables");
        }
        return new Object[0];
    }
}
