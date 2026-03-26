package pl.vtt.wpi.core.application.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.DeserializationException;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestAgent;
import pl.vtt.wpi.core.application.util.ResponseDeserializer;
import pl.vtt.wpi.core.application.util.ResponseProxy;
import pl.vtt.wpi.core.domain.model.Authorization;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;

import static pl.vtt.wpi.core.domain.model.endpoint.Method.POST;
import static pl.vtt.wpi.core.domain.model.endpoint.RequestTarget.AUTH;

public class LoginServiceImpl implements LoginService {
    private final RequestFactory<Void> requestFactory;
    private final RequestAgent<Void> requestAgent;
    private final ResponseDeserializer responseDeserializer;

    @Deprecated(forRemoval = true)
    public LoginServiceImpl(RequestFactory<Void> requestFactory,
                            RequestAgent<Void> requestAgent,
                            ResponseDeserializer responseDeserializer) {
        this.requestFactory = requestFactory;
        this.requestAgent = requestAgent;
        this.responseDeserializer = responseDeserializer;
    }

    public LoginServiceImpl(String url, Supplier<Authorization> authorizationSupplier,
                            RequestAgent<Void> requestAgent,
                            ResponseDeserializer responseDeserializer) {
        this.requestFactory = new LoginRequestFactory(url, authorizationSupplier);
        this.requestAgent = requestAgent;
        this.responseDeserializer = responseDeserializer;
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
            ResponseProxy responseProxy = requestAgent.send(requestFactory.create(POST, AUTH, null));
            String responseBodyString = responseProxy.getResponse().body();
            try {
                responseBody = responseDeserializer.deserialize(responseBodyString, Credentials.class);
            } catch (RuntimeException e) {
                throw new DeserializationException("Failed to deserialize login response", e);
            }
        } catch (IncorrectUsernameOrPasswordException | DeserializationException e) {
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

    private record LoginRequestFactory(String url, Supplier<Authorization> authorizationSupplier)
            implements RequestFactory<Void> {
        @Override
        public Request<Void> create(Method method, RequestTarget target, Void payload) {
            return new Request<>(POST, url, authorizationSupplier.get(), null);
        }
    }
}
