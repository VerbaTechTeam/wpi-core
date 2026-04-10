package pl.vtt.wpi.core.domain.port;

import org.junit.jupiter.api.Test;
import pl.vtt.wpi.core.application.util.RequestFactory;
import pl.vtt.wpi.core.application.util.RequestHandler;
import pl.vtt.wpi.core.domain.exception.OutputPortException;
import pl.vtt.wpi.core.domain.model.Credentials;
import pl.vtt.wpi.core.domain.model.Request;
import pl.vtt.wpi.core.domain.model.endpoint.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthOutputPortTest {
    @Test
    void load_usesPostMethod() throws Exception {
        RequestFactory<Void> requestFactory = (method, target, payload) ->
                new Request<>(null, method, target.descriptor().resource().url("http://localhost"), null, payload);
        RequestHandler<Void, Credentials> requestHandler = request -> {
            assertEquals(Method.POST, request.method());
            return new Credentials("admin", "token");
        };
        AuthOutputPort port = new AuthOutputPort(requestFactory, requestHandler);

        Credentials credentials = port.load();

        assertEquals("admin", credentials.username());
    }

    @Test
    void load_wrapsException() {
        RequestFactory<Void> requestFactory = (method, target, payload) -> null;
        RequestHandler<Void, Credentials> requestHandler = request -> {
            throw new IllegalStateException("boom");
        };
        AuthOutputPort port = new AuthOutputPort(requestFactory, requestHandler);

        OutputPortException exception = assertThrows(OutputPortException.class, port::load);
        assertEquals("Cannot authorize user", exception.getMessage());
    }
}
