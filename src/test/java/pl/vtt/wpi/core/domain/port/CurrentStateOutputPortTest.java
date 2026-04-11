package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.device.CurrentState;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrentStateOutputPortTest {
    @Test
    void load_usesGetMethod() throws Exception {
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestHandler<Void, CurrentState> requestHandler = request -> {
            assertEquals(Method.GET, request.method());
            return new CurrentState.Builder().alive(true).build();
        };
        CurrentStateOutputPort port = new CurrentStateOutputPort(requestFactory, requestHandler);

        assertEquals(true, port.load().alive());
    }
}
