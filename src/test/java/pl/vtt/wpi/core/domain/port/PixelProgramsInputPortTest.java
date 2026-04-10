package pl.vtt.wpi.core.domain.port;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.domain.RequestSender;
import pl.vtt.wpi.core.domain.exception.InputPortException;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PixelProgramsInputPortTest {

    @Test
    void send_throwsWhenProgramsNull() {
        RequestFactory<List<PixelProgram>> requestFactory = (method, target, payload) -> null;
        RequestSender requestSender = request -> {};
        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("Pixel programs cannot be null", exception.getMessage());
    }

    @Test
    void send_sendsPutRequest() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<List<PixelProgram>> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;

        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);
        port.send(List.of());

        Request<?> request = sent.get();
        assertNotNull(request);
        assertEquals(Method.PUT, request.method());
    }
}
