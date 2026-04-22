package pl.vtt.wpi.core.domain.port;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.input.PixelProgramsInputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PixelProgramsInputPortTest {

    @Test
    void send_throwsWhenProgramsNull() {
        RequestFactory<List<PixelProgram>> requestFactory = (_, _, _) -> null;
        RequestSender requestSender = _ -> {};
        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("Pixel programs cannot be null", exception.getMessage());
    }

    @Test
    void send_sendsPutRequest() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<List<PixelProgram>> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;

        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);
        port.send(List.of());

        Request<?> request = sent.get();
        assertNotNull(request);
        assertEquals(Method.PUT, request.method());
    }
}
