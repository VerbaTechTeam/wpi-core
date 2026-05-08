package pl.vtt.wpi.core.domain.port;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.domain.model.endpoint.RequestTarget;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestSender;
import pl.vtt.wpi.core.domain.port.exception.InputPortException;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.PixelProgram;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.infrastructure.adapter.http.PixelProgramsInputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PixelProgramsInputPortTest {

    @Test
    void send_throwsWhenProgramsNull() {
        RequestFactory<List<PixelProgram>> requestFactory = (_, _, _, _) -> null;
        RequestSender requestSender = _ -> {};
        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class, () -> port.send(null));
        assertEquals("Pixel programs cannot be null", exception.getMessage());
    }

    @Test
    void send_sendsPutRequest() throws Exception {
        AtomicReference<Request<?>> sent = new AtomicReference<>();
        RequestFactory<List<PixelProgram>> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestSender requestSender = sent::set;

        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);
        List<PixelProgram> programs = List.of();
        port.send(programs);

        Request<?> request = sent.get();
        assertNotNull(request);
        assertEquals(Method.PUT, request.method());
        assertEquals(RequestTarget.PIXEL_PROGRAMS_UPDATE.url("http://localhost"), request.url());
        assertEquals(programs, request.payload());
    }

    @Test
    void send_throwsWhenProgramsContainNull() {
        RequestFactory<List<PixelProgram>> requestFactory = (_, _, _, _) -> null;
        RequestSender requestSender = _ -> {};
        PixelProgramsInputPort port = new PixelProgramsInputPort(requestFactory, requestSender);

        InputPortException exception = assertThrows(InputPortException.class,
                () -> port.send(java.util.Collections.singletonList(null))
        );
        assertEquals("Pixel programs list cannot contain null elements", exception.getMessage());
    }
}
