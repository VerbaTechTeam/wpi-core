package pl.vtt.wpi.core.domain.port;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.input.RestartInputPort;

import static org.junit.jupiter.api.Assertions.*;

class RestartInputPortTest {
    @Test
    void send_usesPostMethod() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<Void> requestFactory = (payload, method, target, _) -> {
            assertEquals(RequestTarget.RESTART, target);
            return new Request<>(null, method, target.url("http://localhost"), null, payload);
        };
        RequestSender requestSender = sent::set;

        RestartInputPort port = new RestartInputPort(requestFactory, requestSender);
        port.send(null);

        assertEquals(Method.POST, sent.get().method());
    }

    @Test
    void send_wrapsException() {
        RuntimeException cause = new RuntimeException("boom");
        RestartInputPort port = new RestartInputPort((_, _, _, _) -> {
            throw cause;
        }, _ -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("Cannot restart device", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
