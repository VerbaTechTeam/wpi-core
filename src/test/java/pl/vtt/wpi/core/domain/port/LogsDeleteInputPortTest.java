package pl.vtt.wpi.core.domain.port;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogsDeleteInputPortTest {
    @Test
    void send_usesDeleteMethod() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;

        LogsDeleteInputPort port = new LogsDeleteInputPort(requestFactory, requestSender);
        port.send(null);

        assertEquals(Method.DELETE, sent.get().method());
    }
}
