package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.RuntimeData;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.infrastructure.adapter.http.RuntimeDataOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
