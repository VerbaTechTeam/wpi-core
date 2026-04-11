package pl.vtt.wpi.core.domain.port;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserCreateInputPortTest {

    @Test
    void send_success_invokesFactoryAndSender() throws Exception {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        AtomicReference<Method> usedMethod = new AtomicReference<>();
        AtomicReference<RequestTarget> usedTarget = new AtomicReference<>();
        AtomicReference<User> usedPayload = new AtomicReference<>();
        AtomicReference<Request<?>> sentRequest = new AtomicReference<>();

        Request<User> request = new Request<>(null, Method.POST, "http://localhost/api/secure/users", null, user);
        RequestFactory<User> requestFactory = (method, target, payload) -> {
            usedMethod.set(method);
            usedTarget.set(target);
            usedPayload.set(payload);
            return request;
        };
        RequestSender requestSender = sentRequest::set;

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        port.send(user);

        assertEquals(Method.POST, usedMethod.get());
        assertEquals(RequestTarget.USERS_CREATE, usedTarget.get());
        assertSame(user, usedPayload.get());
        assertSame(request, sentRequest.get());
    }

    @Test
    void send_nullUser_throwsInputPortException() {
        UserCreateInputPort port = new UserCreateInputPort((method, target, payload) -> null, req -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));

        assertTrue(exception.getMessage().contains("User cannot be null"));
    }

    @Test
    void send_factoryException_wrapsWithCause() {
        RuntimeException originalCause = new RuntimeException("factory failure");
        RequestFactory<User> requestFactory = (method, target, payload) -> {
            throw originalCause;
        };
        RequestSender requestSender = request -> {};

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class,
                () -> port.send(new User("admin", EnumSet.of(UserGroup.ADMIN))));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertSame(originalCause, exception.getCause());
    }

    @Test
    void send_senderException_wrapsWithCause() {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        RuntimeException originalCause = new RuntimeException("send failure");

        RequestFactory<User> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestSender requestSender = request -> {
            throw originalCause;
        };

        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(user));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertSame(originalCause, exception.getCause());
    }
}
