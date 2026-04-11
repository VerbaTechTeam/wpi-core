package pl.vtt.wpi.core.domain.port;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestartInputPortTest {
    @Test
    void send_usesPostMethod() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;

        RestartInputPort port = new RestartInputPort(requestFactory, requestSender);
        port.send(null);

        assertEquals(Method.POST, sent.get().method());
    }

    @Test
    void send_wrapsException() {
        RestartInputPort port = new RestartInputPort((method, target, payload) -> {
            throw new RuntimeException("boom");
        }, request -> {});

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("Cannot restart device", exception.getMessage());
    }
}
