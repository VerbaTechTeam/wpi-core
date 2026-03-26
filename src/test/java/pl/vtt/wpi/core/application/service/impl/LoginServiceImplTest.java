package pl.vtt.wpi.core.application.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.vtt.wpi.core.application.config.AuthorizationHolder;
import pl.vtt.wpi.core.application.exception.IncorrectUsernameOrPasswordException;
import pl.vtt.wpi.core.application.util.RequestAgent;
import pl.vtt.wpi.core.application.util.ResponseDeserializer;
import pl.vtt.wpi.core.application.util.ResponseProxy;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.Response;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceImplTest {
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test123";
    private static final String TOKEN = "token";

    @BeforeEach
    void setUp() {
        AuthorizationHolder.clear();
    }

    @Test
    @DisplayName("Should login successfully and update AuthorizationHolder")
    void shouldLoginSuccessfully() {
        LoginServiceImpl instance = instanceWithSuccessResponse(new Credentials(USERNAME, TOKEN));

        Credentials credentials = assertDoesNotThrow(() -> instance.login(USERNAME, PASSWORD));

        assertAll(
                () -> assertNotNull(credentials),
                () -> assertEquals(USERNAME, credentials.username()),
                () -> assertEquals(TOKEN, credentials.token()),
                () -> assertNotNull(AuthorizationHolder.get()),
                () -> assertEquals("Basic", AuthorizationHolder.get().type()),
                () -> assertEquals(encoded(USERNAME + ":" + TOKEN), AuthorizationHolder.get().credentials())
        );
    }

    @Test
    @DisplayName("Should propagate exception thrown by agent")
    void shouldPropagateAgentException() {
        LoginServiceImpl instance = new LoginServiceImpl(
                null,
                AuthorizationHolder::get,
                _ -> {
                    throw new IncorrectUsernameOrPasswordException();
                },
                new ResponseDeserializer() {
                    @Override
                    public <R> R deserialize(String responseBody, Class<R> type) {
                        fail("Deserializer should not be called when agent throws an exception");
                        return type.cast(null);
                    }
                }
        );

        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(USERNAME, PASSWORD));
    }

    @ParameterizedTest(name = "Should reject invalid username: ''{0}''")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void shouldRejectInvalidUsername(String invalidUsername) {
        LoginServiceImpl instance = instanceWithSuccessResponse(new Credentials(USERNAME, TOKEN));

        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(invalidUsername, PASSWORD));
    }

    @ParameterizedTest(name = "Should reject invalid password: ''{0}''")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void shouldRejectInvalidPassword(String invalidPassword) {
        LoginServiceImpl instance = instanceWithSuccessResponse(new Credentials(USERNAME, TOKEN));

        assertThrows(IncorrectUsernameOrPasswordException.class, () -> instance.login(USERNAME, invalidPassword));
    }

    @Test
    @DisplayName("Should clear authorization on logout")
    void shouldClearAuthorizationOnLogout() {
        LoginServiceImpl instance = new LoginServiceImpl(null, null, null, null);

        instance.logout();

        assertNull(AuthorizationHolder.get());
    }

    private static LoginServiceImpl instanceWithSuccessResponse(Credentials deserializedCredentials) {
        RequestAgent<Void> requestAgent = _ -> successResponseProxy();
        ResponseDeserializer responseDeserializer = new ResponseDeserializer() {
            @Override
            public <R> R deserialize(String responseBody, Class<R> type) {
                return type.cast(deserializedCredentials);
            }
        };
        return new LoginServiceImpl(null, AuthorizationHolder::get, requestAgent, responseDeserializer);
    }

    private static ResponseProxy successResponseProxy() {
        return () -> new Response("response", null);
    }

    private static String encoded(String value) {
        return new String(java.util.Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8)));
    }
}
