package pl.vtt.wpi.core.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.domain.model.Credentials;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
                null, AuthorizationHolder::get, _ -> new Credentials(username, token)
        );
        try {
            Credentials credentials = instance.login(username, password);
            assertNotNull(credentials);
            assertEquals(username, credentials.username());
            assertEquals(token, credentials.token());
            String expectedType = "Basic";
            String expectedCredentials = Base64.getEncoder()
                    .encodeToString((username + ":" + token).getBytes(StandardCharsets.UTF_8));
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
                null, AuthorizationHolder::get, _ -> {
                    throw new IncorrectUsernameOrPasswordException();
                }
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a empty username")
    void empty_username_exception() {
        String username = "";
        String password = "test123";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a empty password")
    void empty_password_exception() {
        String username = "test";
        String password = "";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a null username")
    void null_username_exception() {
        String username = null;
        String password = "test123";
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a null password")
    void null_password_exception() {
        String username = "test";
        String password = null;
        String token = "token";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> new Credentials(username, token)
        );
        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a null response body from request handler")
    void null_response_body_exception() {
        String username = "test";
        String password = "test123";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> null
        );

        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(username, password));
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests cleanup when request handler throws runtime exception")
    void runtime_exception_clears_authorization() {
        String username = "test";
        String password = "test123";
        LoginServiceImpl instance = new LoginServiceImpl(
                null, AuthorizationHolder::get, _ -> {
                    throw new IllegalStateException("connection lost");
                }
        );

        RuntimeException exception = assertThrows(RuntimeException.class, () -> instance.login(username, password));
        assertEquals("Login failed", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("connection lost", exception.getCause().getMessage());
        assertNull(AuthorizationHolder.get());
    }

    @Test
    @DisplayName("Tests a logout")
    void logout_ok() {
        LoginServiceImpl instance = new LoginServiceImpl(null, null, null);
        instance.logout();
        assertNull(AuthorizationHolder.get());
    }
}
