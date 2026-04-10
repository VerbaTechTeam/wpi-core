package pl.vtt.wpi.core.application.service.impl;

import java.time.LocalDateTime;
import java.util.function.Supplier;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.OutputPort;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Authorization;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.port.AuthOutputPort;

import static pl.vtt.wpi.core.domain.model.endpoint.Method.POST;

public class LoginServiceImpl implements LoginService {
    private final OutputPort<Credentials> authPort;

    @Deprecated(forRemoval = true)
    public LoginServiceImpl(RequestFactory<Void> requestFactory,
                            RequestHandler<Void, Credentials> requestHandler) {
        this.authPort = new AuthOutputPort(requestFactory, requestHandler);
    }

    public LoginServiceImpl(String url, Supplier<Authorization> authorizationSupplier,
                            RequestHandler<Void, Credentials> requestHandler) {
        this.authPort = new AuthOutputPort(new LoginRequestFactory(url, authorizationSupplier), requestHandler);
    }

    public LoginServiceImpl(OutputPort<Credentials> authPort) {
        this.authPort = authPort;
    }

    @Override
    public Credentials login(String username, String password)
            throws IncorrectUsernameOrPasswordException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IncorrectUsernameOrPasswordException();
        }
        AuthorizationHolder.authorize(username, password);
        try {
            Credentials credentials = getCredentials();
            AuthorizationHolder.authorize(credentials);
            return credentials;
        } catch (IncorrectUsernameOrPasswordException | RuntimeException e) {
            AuthorizationHolder.clear();
            throw e;
        }
    }

    private Credentials getCredentials()
            throws IncorrectUsernameOrPasswordException {
        final Credentials responseBody;
        try {
            responseBody = authPort.load();
        } catch (OutputPortException e) {
            if (e.getCause() instanceof IncorrectUsernameOrPasswordException incorrect) {
                throw incorrect;
            }
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new RuntimeException("Login failed", cause);
        }
        if (responseBody == null) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return responseBody;
    }

    private record LoginRequestFactory(String url, Supplier<Authorization> supplier)
            implements RequestFactory<Void> {
        @Override
        public Request<Void> create(Method method, RequestTarget target, Void payload) {
            return new Request<>(LocalDateTime.now(), POST, url, supplier.get(), null);
        }
    }
}
