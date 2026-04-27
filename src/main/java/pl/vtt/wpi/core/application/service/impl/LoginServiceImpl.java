package pl.vtt.wpi.core.application.service.impl;

import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.exception.AuthenticationServiceUnavailableException;
import pl.vtt.wpi.core.application.service.LoginService;
import pl.vtt.wpi.core.domain.port.OutputPort;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Credentials;

public class LoginServiceImpl implements LoginService {
    private final OutputPort<Credentials> authPort;

    public LoginServiceImpl(OutputPort<Credentials> authPort) {
        this.authPort = authPort;
    }

    @Override
    public Credentials login(String username, String password)
            throws IncorrectUsernameOrPasswordException, AuthenticationServiceUnavailableException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new IncorrectUsernameOrPasswordException();
        }
        AuthorizationHolder.authorize(username, password);
        try {
            Credentials credentials = getCredentials();
            AuthorizationHolder.authorize(credentials);
            return credentials;
        } catch (IncorrectUsernameOrPasswordException
                 | AuthenticationServiceUnavailableException
                 | RuntimeException e) {
            AuthorizationHolder.clear();
            throw e;
        }
    }

    private Credentials getCredentials()
            throws IncorrectUsernameOrPasswordException, AuthenticationServiceUnavailableException {
        final Credentials responseBody;
        try {
            responseBody = authPort.load();
        } catch (OutputPortException e) {
            if (e.getCause() instanceof IncorrectUsernameOrPasswordException incorrect) {
                throw incorrect;
            }
            Throwable cause = e.getCause() == null ? e : e.getCause();
            throw new AuthenticationServiceUnavailableException("Login failed", cause);
        }
        if (responseBody == null) {
            throw new IncorrectUsernameOrPasswordException();
        }
        return responseBody;
    }
}
