package pl.vtt.wpi.core.domain.port;

import java.util.EnumSet;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserCreateInputPortTest {

    @Test
    void send_success_invokesFactoryAndSender() throws Exception {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        Request<User> request = new Request<>(null, Method.POST, "http://localhost/api/secure/users", null, user);

        RecordingFactory requestFactory = new RecordingFactory(request, null);
        RecordingSender requestSender = new RecordingSender(null);
        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        port.send(user);

        assertEquals(Method.POST, requestFactory.method);
        assertEquals(RequestTarget.USERS_CREATE, requestFactory.target);
        assertSame(user, requestFactory.payload);
        assertSame(request, requestSender.request);
    }

    @Test
    void send_nullUser_throwsInputPortException() {
        UserCreateInputPort port = new UserCreateInputPort((method, target, payload) -> null, req -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));

        assertTrue(exception.getMessage().contains("User cannot be null"));
    }

    @Test
    void send_factoryException_wrapsWithCause() {
        RuntimeException cause = new RuntimeException("factory failure");
        RecordingFactory requestFactory = new RecordingFactory(null, cause);
        RecordingSender requestSender = new RecordingSender(null);
        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class,
                () -> port.send(new User("admin", EnumSet.of(UserGroup.ADMIN))));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertNotNull(exception.getCause());
        assertSame(cause, exception.getCause());
    }

    @Test
    void send_senderException_wrapsWithCause() {
        User user = new User("admin", EnumSet.of(UserGroup.ADMIN));
        Request<User> request = new Request<>(null, Method.POST, "http://localhost/api/secure/users", null, user);
        RuntimeException cause = new RuntimeException("send failure");

        RecordingFactory requestFactory = new RecordingFactory(request, null);
        RecordingSender requestSender = new RecordingSender(cause);
        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(user));

        assertTrue(exception.getMessage().contains("Cannot create user"));
        assertNotNull(exception.getCause());
        assertSame(cause, exception.getCause());
    }

    private static final class RecordingFactory implements RequestFactory<User> {
        private final Request<User> request;
        private final RuntimeException toThrow;
        private Method method;
        private RequestTarget target;
        private User payload;

        private RecordingFactory(Request<User> request, RuntimeException toThrow) {
            this.request = request;
            this.toThrow = toThrow;
        }

        @Override
        public Request<User> create(Method method, RequestTarget target, User payload) {
            this.method = method;
            this.target = target;
            this.payload = payload;
            if (toThrow != null) {
                throw toThrow;
            }
            return request;
        }
    }

    private static final class RecordingSender implements RequestSender {
        private final RuntimeException toThrow;
        private Request<?> request;

        private RecordingSender(RuntimeException toThrow) {
            this.toThrow = toThrow;
        }

        @Override
        public void send(Request<?> request) {
            this.request = request;
            if (toThrow != null) {
                throw toThrow;
            }
        }
    }
}
