package pl.vtt.wpi.core.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.Request;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceImplTest {

    @BeforeEach
    void setUp() {
        AuthorizationHolder.clear();
    }

    @Test
    @DisplayName(value = "Tests a casual login")
    void casual_login_ok() {
        String username = "test";
        String password = "test123";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> new Credentials(username, token)
        );
        try {
            Credentials credentials = instance.login(username, password);
            assertNotNull(credentials);
            assertNotNull(credentials.username());
            assertNotNull(credentials.token());
            assertEquals(username, credentials.username());
            assertEquals(token, credentials.token());
            String expectedType = "Basic";
            String expectedCredentials = new String(
                    java.util.Base64.getEncoder().encode((username + ":" + token).getBytes())
            );
            assertEquals(expectedType, AuthorizationHolder.get().type());
            assertEquals(expectedCredentials, AuthorizationHolder.get().credentials());
        } catch (IncorrectUsernameOrPasswordException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Tests a wrong username or password")
    void wrong_username_or_password_login_exception() {
        String username = "test";
        String password = "test123";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> {
                    throw new IncorrectUsernameOrPasswordException();
                }
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
    }

    @Test
    @DisplayName("Tests a empty username")
    void empty_username_exception() {
        String username = "";
        String password = "test123";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
    }

    @Test
    @DisplayName("Tests a empty password")
    void empty_password_exception() {
        String username = "test";
        String password = "";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
    }

    @Test
    @DisplayName("Tests a null username")
    void null_username_exception() {
        String username = null;
        String password = "test123";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
    }

    @Test
    @DisplayName("Tests a null password")
    void null_password_exception() {
        String username = "test";
        String password = null;
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                (target, payload) -> {
                    String url = target.descriptor().resource().url(null);
                    return new Request<>(url, AuthorizationHolder.get(), payload);
                },
                _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
    }

    @Test
    @DisplayName("Tests a logout")
    void logout_ok() {
        LoginServiceImpl instance = new LoginServiceImpl(null, null);
        instance.logout();
        assertNull(AuthorizationHolder.get());
    }
}