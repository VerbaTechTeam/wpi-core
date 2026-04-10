package pl.vtt.wpi.core.domain.port;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.User;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.model.endpoint.UserGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserCreateInputPortTest {
    @Test
    void send_throwsWhenUserNull() {
        UserCreateInputPort port = new UserCreateInputPort((method, target, payload) -> null, request -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void send_usesPostMethod() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<User> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;
        UserCreateInputPort port = new UserCreateInputPort(requestFactory, requestSender);

        port.send(new User("admin", Set.of(UserGroup.ADMIN)));

        assertEquals(Method.POST, sent.get().method());
    }
}
