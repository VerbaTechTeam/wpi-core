package pl.vtt.wpi.core.application.config;

import pl.vtt.wpi.core.domain.model.Authorization;

public final class AuthorizationHolder {

    private static final ThreadLocal<Authorization> authorization = new ThreadLocal<>();

    private AuthorizationHolder() {}

    public static Authorization get() {
        return authorization.get();
    }

    public static void authorize(String type, String credentials) {
        AuthorizationHolder.authorization.set(new Authorization(type, credentials));
    }

    public static void clear() {
        AuthorizationHolder.authorization.remove();
    }
}
