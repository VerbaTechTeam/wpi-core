package pl.vtt.wpi.core.application.service;

import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.exception.AuthenticationServiceUnavailableException;
import pl.vtt.wpi.core.domain.model.Credentials;

public interface LoginService {
    Credentials login(String username, String password)
            throws IncorrectUsernameOrPasswordException, AuthenticationServiceUnavailableException;

    default void logout() {
        AuthorizationHolder.clear();
    }
}
