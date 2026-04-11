package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.output.RuntimeDataOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuntimeDataOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestHandler<Void, RuntimeData> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return new RuntimeData.Builder().nol(10).build();
        };
        RuntimeDataOutputPort port = new RuntimeDataOutputPort(requestFactory, requestHandler);

        assertEquals(10, port.load().nol());
    }
}
