package pl.vtt.wpi.core.domain.model;

public interface AuthorizationManager {
    Authorization getAuthorization();

    void authorize(Authorization authorization);

    default void authorize(String username, String password) {
        authorize(Authorization.basic(username, password));
    }

    default void authorize(Credentials credentials) {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        if (credentials.username() == null || credentials.token() == null) {
            throw new IllegalArgumentException("Credentials must have username and token");
        }
        authorize(credentials.username(), credentials.token());
    }

    default void clear() {
        authorize((Authorization) null);
    }
}
