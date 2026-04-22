package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.infrastructure.RequestFactory;
import pl.vtt.wpi.core.infrastructure.RequestHandler;
import pl.vtt.wpi.core.infrastructure.Request;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.model.endpoint.Method;
import pl.vtt.wpi.core.domain.port.output.CurrentStateOutputPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CurrentStateOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (payload, method, target, _) ->
                new Request<>(null, method, target.url("http://localhost"), null, payload);
        RequestHandler<Void, CurrentState> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return new CurrentState.Builder().alive(true).build();
        };
        CurrentStateOutputPort port = new CurrentStateOutputPort(requestFactory, requestHandler);

        assertTrue(port.load().alive());
    }
}
