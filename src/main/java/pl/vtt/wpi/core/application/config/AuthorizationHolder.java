package pl.vtt.wpi.core.application.config;

import java.util.Objects;
import pl.vtt.wpi.core.domain.model.Authorization;
import pl.vtt.wpi.core.domain.model.Credentials;

public final class AuthorizationHolder {

    private static final ThreadLocal<Authorization> authorization = new ThreadLocal<>();

    private AuthorizationHolder() {}

    public static Authorization get() {
        return authorization.get();
    }

    public static void authorize(String username, String password) {
        authorization.set(Authorization.basic(username, password));
    }

    public static void authorize(Credentials credentials) {
        if (credentials == null || credentials.username() == null || credentials.token() == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        authorize(credentials.username(), credentials.token());
    }

    public static void clear() {
        AuthorizationHolder.authorization.remove();
    }
}
