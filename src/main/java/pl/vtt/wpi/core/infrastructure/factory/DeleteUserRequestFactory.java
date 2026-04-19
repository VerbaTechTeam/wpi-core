package pl.vtt.wpi.core.infrastructure.factory;

import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

public final class DeleteUserRequestFactory<T> extends AbstractRequestFactory<T> {
    private String username;

    public DeleteUserRequestFactory(String baseUrl) {
        super(baseUrl);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Object[] getUrlVariables(RequestTarget target) {
        if (target != RequestTarget.USER_DELETE) {
            throw new IllegalArgumentException("Request target is illegal: " + target.name());
        }
        if (username == null) {
            throw new IllegalArgumentException("Request target requires username");
        }
        return new Object[] { username };
    }
}
