package pl.vtt.wpi.core.application.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.model.Credentials;

public class LoginServiceImpl implements LoginService {
    private final RequestFactory<Void> requestFactory;
    private final RequestHandler<Void, Credentials> requestHandler;

    public LoginServiceImpl(RequestFactory<Void> requestFactory,
                            RequestHandler<Void, Credentials> requestHandler) {
        this.requestFactory = requestFactory;
        this.requestHandler = requestHandler;
    }

    @Override
    public Credentials login(String username, String password)
            throws IncorrectUsernameOrPasswordException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IncorrectUsernameOrPasswordException();
        }
        authorize(username, password);
        try {
            Credentials credentials = getCredentials();
            authorize(credentials);
            return credentials;
        } catch (IncorrectUsernameOrPasswordException | RuntimeException e) {
            AuthorizationHolder.clear();
            throw e;
        }
    }

    private Credentials getCredentials()
            throws IncorrectUsernameOrPasswordException {
        Credentials responseBody;
        try {
            responseBody = requestHandler.handle(requestFactory.create(RequestTarget.AUTH, null));
        } catch (IncorrectUsernameOrPasswordException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Login failed", e);
        }
        if (responseBody == null) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return responseBody;
    }

    private static void authorize(Credentials credentials) {
        authorize(credentials.username(), credentials.token());
    }

    private static void authorize(String username, String credentials) {
        AuthorizationHolder.authorize("Basic", encode(String.join(":", username, credentials)));
    }

    private static String encode(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes(StandardCharsets.UTF_8));
    }
}
