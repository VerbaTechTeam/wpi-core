package pl.vtt.wpi.core.domain.port;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.domain.dto.PasswordDto;
import pl.vtt.wpi.core.domain.dto.UserCreateRequest;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;
import pl.vtt.wpi.core.domain.port.input.UserCreateInputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserCreateInputPortTest {

    @Test
    void send_success_invokesFactoryAndSender() throws Exception {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        PasswordDto passwordDto = new PasswordDto("secret", "secret");
        UserCreateRequest userCreateRequest = new UserCreateRequest(user, passwordDto);
        AtomicReference<Method> usedMethod = new AtomicReference<>();
        AtomicReference<RequestTarget> usedTarget = new AtomicReference<>();
        AtomicReference<UserCreateRequest> usedPayload = new AtomicReference<>();
        AtomicReference<Request<?>> sentRequest = new AtomicReference<>();

        Request<UserCreateRequest> request = new Request<>(null, Method.POST,
                RequestTarget.USERS_CREATE.url("http://localhost"), null, userCreateRequest);
        RequestFactory<UserCreateRequest> requestFactory = (payload, method, target, _) -> {
            usedMethod.set(method);
            usedTarget.set(target);
            usedPayload.set(payload);
            return request;
        };
        RequestSender requestSender = sentRequest::set;

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        port.send(userCreateRequest);

        assertEquals(Method.POST, usedMethod.get());
        assertEquals(RequestTarget.USERS_CREATE, usedTarget.get());
        assertSame(userCreateRequest, usedPayload.get());
        assertSame(request, sentRequest.get());
    }

    @Test
    void send_nullUser_throwsInputPortException() {
        UserCreateInputPort port = new UserCreateInputPort((_, _, _, _) -> null, _ -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));

        assertTrue(exception.getMessage().contains("User and password data cannot be null"));
    }

    @Test
    void send_factoryException_wrapsWithCause() {
        RuntimeException originalCause = new RuntimeException("factory failure");
        RequestFactory<UserCreateRequest> requestFactory = (_, _, _, _) -> {
            throw originalCause;
        };
        RequestSender requestSender = _ -> {};

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class,
                () -> port.send(new UserCreateRequest(
                        new User("admin", EnumSet.of(UserGroup.ADMIN)),
                        new PasswordDto("secret", "secret")
                )));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertSame(originalCause, exception.getCause());
    }

    @Test
    void send_senderException_wrapsWithCause() {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        PasswordDto passwordDto = new PasswordDto("secret", "secret");
        UserCreateRequest userCreateRequest = new UserCreateRequest(user, passwordDto);
        RuntimeException originalCause = new RuntimeException("send failure");

        RequestFactory<UserCreateRequest> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestSender requestSender = _ -> {
            throw originalCause;
        };

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(userCreateRequest));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertSame(originalCause, exception.getCause());
    }
}
