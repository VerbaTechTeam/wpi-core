package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.exception.OutputPortException;
import pl.vtt.wpi.core.infrastructure.adapter.http.RuntimeDataOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuntimeDataOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestHandler<Void, RuntimeData> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return new RuntimeData.Builder().nol(10).build();
        };
        RuntimeDataOutputPort port = new RuntimeDataOutputPort(requestFactory, requestHandler);

        assertEquals(10, port.load().nol());
    }

    @Test
    void load_preservesInterruptStatusWhenInterrupted() {
        Thread.interrupted();
        RequestFactory<Void> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestHandler<Void, RuntimeData> requestHandler = _ -> {
            throw new InterruptedException("interrupted");
        };
        RuntimeDataOutputPort port = new RuntimeDataOutputPort(requestFactory, requestHandler);

        try {
            OutputPortException exception = assertThrows(OutputPortException.class, port::load);
            assertEquals("interrupted", exception.getCause().getMessage());
            assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }
}
